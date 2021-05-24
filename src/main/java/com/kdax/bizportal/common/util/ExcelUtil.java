package com.kdax.bizportal.common.util;

import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.exception.ErrorType;
import com.kdax.bizportal.common.util.setter.ExcelSheetConfig;
import com.kdax.bizportal.common.util.setter.ExcelUtilConfig;
import com.kdax.bizportal.common.voCommon.ExcelToVoConvertOptionVO;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ExcelUtil {
    private static final String PRE_COLNAME = "col_";

    public static List fileToList(MultipartFile file) throws IOException {
        return fileToList(file, ExcelUtilConfig.builder().build());
    }

    public List fileToList(MultipartFile file, ExcelUtilConfig config) throws IOException {

        List returnList = new ArrayList();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        Workbook workbook = getSheets(file, extension);
        // 1번 시트만 사용

        for (int sIndex = 0; sIndex < 1; sIndex++) {
            //config
            ExcelSheetConfig sConfig = new ExcelSheetConfig(config, sIndex);

            List<Map> dataList = new ArrayList<>();
            Map sheetMap = new HashMap();
            Sheet worksheet = workbook.getSheetAt(sIndex);

            Boolean titleFlag = true;
            Boolean headerFlag = true;
            Boolean summaryFlag = true;

            int nullRows = 0;
            for (int i = 0; i < worksheet.getPhysicalNumberOfRows() + nullRows; i++) {
                Row row = worksheet.getRow(i);

                if (row == null) {
                    nullRows++;
                    continue;
                }

                // title header summary
                if (Boolean.TRUE.equals(excelTitleValid(titleFlag, sConfig, i, nullRows)
                        || excelHeaderValid(headerFlag, sConfig, i, nullRows)
                        || excelSummaryValid(summaryFlag, sConfig, worksheet, i, nullRows)
                        || row.getLastCellNum() < 0)) {
                    continue;
                }

                dataListSetToCellData(sConfig, dataList, row);
            }

            sheetMap.put("SHEET_NAME", worksheet.getSheetName());
            sheetMap.put("SHEET_DATA", dataList);
            returnList.add(sheetMap);
        }

        return returnList;
    }

    public String getColumnData(MultipartFile file, ExcelUtilConfig config) throws IOException {
        String returnStr = "";

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        Workbook workbook = getSheets(file, extension);
        // 1번 시트만 사용
        List<Integer> accountList;
        if (config != null && config.getSheetAccountIndex() != null) {
            accountList = config.getSheetAccountIndex();
        } else {
            return returnStr;
        }
        if (accountList.isEmpty() || accountList.isEmpty() || accountList.size() != 2) {
            return returnStr;
        }

        for (int sIndex = 0; sIndex < 1; sIndex++) {
            Sheet worksheet = workbook.getSheetAt(sIndex);

            Cell targetCell = worksheet.getRow(accountList.get(0)).getCell(accountList.get(1));

            CellType ctype = targetCell.getCellType();
            if (ctype.equals(CellType.STRING)) {
                return targetCell.getStringCellValue();
            } else {
                return returnStr;
            }
        }

        return returnStr;
    }

    private void dataListSetToCellData(ExcelSheetConfig sConfig, List<Map> dataList, Row row) {
        Map data = new HashMap();
        for (int j = 0; j < row.getLastCellNum(); j++) {
            if (row.getCell(j) == null) {
                data.put(createColName(sConfig, j), "");
                continue;
            }
            setDataByCellType(sConfig, row, data, j);
        }
        dataList.add(data);
    }

    private void setDataByCellType(ExcelSheetConfig sConfig, Row row, Map data, int j) {
        CellType ctype = row.getCell(j).getCellType();
        if(ctype.equals(CellType.FORMULA)){
            ctype = row.getCell(j).getCachedFormulaResultType();
        }
        switch (ctype) {
            case STRING:
                data.put(createColName(sConfig, j), row.getCell(j).getStringCellValue());
                break;
            case NUMERIC:
                //cell style format get (doc이 나오는 데이터와 상이 하므로 추후 수정 가능
                int dataFormat = row.getCell(j).getCellStyle().getDataFormat();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                if (dataFormat == 20 || dataFormat == 21) {
                    data.put(createColName(sConfig, j), row.getCell(j).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    break;
                } else if (DateUtil.isCellDateFormatted(row.getCell(j)) || dataFormat == 14 || dataFormat == 55 || (dataFormat == 178 && !createColName(sConfig, j).equals("coinQty"))) {
                    // 기존 date format
                    // excel style 중 선별
                    data.put(createColName(sConfig, j), dateFormat.format(row.getCell(j).getDateCellValue()));
                    break;
                } else {
                    BigDecimal tempBig = BigDecimal.valueOf(row.getCell(j).getNumericCellValue());
                    data.put(createColName(sConfig, j), tempBig.toString());
                    break;
                }
            case BOOLEAN:
                data.put(createColName(sConfig, j), row.getCell(j).getBooleanCellValue());
                break;
            case BLANK:
                data.put(createColName(sConfig, j), "");
                break;
            default:
                if (createColName(sConfig, j).indexOf(PRE_COLNAME) < 0) {
                    int createDataFormat = row.getCell(j).getCellStyle().getDataFormat();
                    if (createDataFormat == 4 || createDataFormat == 41 || createDataFormat == 15) {
                        data.put(createColName(sConfig, j), row.getCell(j).getNumericCellValue());
                    } else {
                        data.put(createColName(sConfig, j), "");
                    }
                } else {
                    data.put(createColName(sConfig, j), "");
                }
                break;
        }
    }

    private Workbook getSheets(MultipartFile file, String extension) throws IOException {
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        return workbook;
    }

    private String createColName(ExcelSheetConfig config, int index) {
        if (config != null && config.getSheetHeaders() != null && config.getSheetHeaders().get(index) != null && !config.getSheetHeaders().get(index).equals("")) {
            return config.getSheetHeaders().get(index);
        } else {
            return PRE_COLNAME + String.format("%03d", index);
        }
    }

    private Boolean excelTitleValid(Boolean titleFlag, ExcelSheetConfig sConfig, int i, int nullRows) {
        Boolean returnFlag = false;
        if (titleFlag && sConfig.getExistSheetTitle() && Boolean.TRUE.equals(sConfig.getNotIncludeSheetTitle())) {
            if (sConfig.getSheetTitleIndex() > -1) {
                if (sConfig.getSheetTitleIndex() == i) {
                    titleFlag = false;
                    returnFlag = true;
                }
            } else {
                if (i - nullRows == 0) {
                    titleFlag = false;
                    returnFlag = true;
                }
            }
        } else {
            titleFlag = false;
        }
        return returnFlag;
    }

    private Boolean excelHeaderValid(Boolean headerFlag, ExcelSheetConfig sConfig, int i, int nullRows) {
        Boolean returnFlag = false;
        if (headerFlag && sConfig.getExistSheetHeader() && Boolean.TRUE.equals(sConfig.getNotIncludeSheetHeader())) {
            if (sConfig.getSheetHeaderIndex() > -1) {
                if (sConfig.getSheetHeaderIndex() >= i) {
                    headerFlag = false;
                    returnFlag = true;
                }
            } else {
                if (i - nullRows == 1) {
                    headerFlag = false;
                    returnFlag = true;
                }
            }
        } else {
            headerFlag = false;
        }
        return returnFlag;
    }

    private Boolean excelSummaryValid(Boolean summaryFlag, ExcelSheetConfig sConfig, Sheet worksheet, int i, int nullRows) {
        Boolean returnFlag = false;
        if (summaryFlag && sConfig.getExistSheetSummary() && Boolean.TRUE.equals(sConfig.getNotIncludeSheetSummary())) {
            if (sConfig.getSheetSummaryIndex() > -1) {
                if (sConfig.getSheetSummaryIndex() == i) {
                    summaryFlag = false;
                    returnFlag = true;
                }
            } else {
                if (i == worksheet.getPhysicalNumberOfRows() + nullRows) {
                    summaryFlag = false;
                    returnFlag = true;
                }
            }
        } else {
            summaryFlag = false;
        }
        return returnFlag;
    }

    public <T> List<T> convertExcelToVo(MultipartFile file, ExcelToVoConvertOptionVO options, Class clazz) throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        Workbook workbook = getSheets(file, extension);

        // 첫번째 시트만
        Sheet worksheet = workbook.getSheetAt(0);
        int nullRows = 0;
        // row for 문
        if(options == null ){
            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_OPTION_IS_NULL);
        }
        if(options.getExcelOption() == null ){
            throw new BizExceptionMessage(ErrorType.EXCEL_OPTION_IS_NULL);
        }
        for (int i = options.getExcelOption().getHeaderStartIndex(); i < worksheet.getPhysicalNumberOfRows() + nullRows; i++) {
            Row row = worksheet.getRow(i);
            if (row == null) {
                nullRows++;
                continue;
            }
            T rowVo = (T) clazz.newInstance();
            Boolean useExcelData = false;
            for(ExcelToVoConvertOptionVO.DataConvertMapper dataConvertMapper : options.getDataConvertMapper()){
                Method [] tempMethods = rowVo.getClass().getMethods();
                if(dataConvertMapper !=null && dataConvertMapper.getDataColumnIndex() <= row.getLastCellNum()){
                    int colIndex = dataConvertMapper.getDataColumnIndex();
                    if(row.getCell(colIndex) == null || CellType.BLANK.equals(row.getCell(colIndex).getCellType())){
                        continue;
                    }
                    try {
                        if(dataConvertMapper.getMultipleId() != null && dataConvertMapper.getMultipleId()){
                            for(int m = 0; m < tempMethods.length; m ++) {
                                Method targetMethod = tempMethods[m];
                                String[] splitStrs = row.getCell(colIndex).getStringCellValue().split(dataConvertMapper.getSplitStr());
                                for(int s = 0; s < dataConvertMapper.getConvertFiledIds().length; s ++){
                                    if(targetMethod.getName().equals("set"+TypeConvertUtil.firstOnlyUpperCase(dataConvertMapper.getConvertFiledIds()[s]))){
                                        if(dataConvertMapper.getDateFormats()[s].equals("YYYY-mm-DD")){
                                            targetMethod.invoke(rowVo, new Object[] {splitStrs[s].replaceAll("[/-]","")});
                                            useExcelData = true;
                                        }else if(dataConvertMapper.getDateFormats()[s].equals("HH:mm:ss")){
                                            targetMethod.invoke(rowVo, new Object[] {splitStrs[s].replaceAll("[/:]","")});
                                            useExcelData = true;
                                        }else if(dataConvertMapper.getDateFormats()[s].equals("YYYY.mm.DD")){
                                            targetMethod.invoke(rowVo, new Object[] {splitStrs[s].replaceAll("[/.]","")});
                                            useExcelData = true;
                                        }
                                    }
                                }
                            }
                        }else{
                            for(int m = 0; m < tempMethods.length; m ++){
                                Method targetMethod = tempMethods[m];
                                if(targetMethod.getName().equals("set"+TypeConvertUtil.firstOnlyUpperCase(dataConvertMapper.getConvertFiledId()))){
                                    String dataType = dataConvertMapper.getConvertDataType();
                                    CellType ctype = row.getCell(colIndex).getCellType();
                                    if(ctype.equals(CellType.FORMULA)){
                                        ctype = row.getCell(colIndex).getCachedFormulaResultType();
                                    }
                                    switch (dataType){
                                        case "string":
                                            targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getStringCellValue()});
                                            useExcelData = true;
                                            break;
                                        case "number":
                                            if(dataConvertMapper.getStringToNumber() != null && dataConvertMapper.getStringToNumber()){
                                                String tempStr = row.getCell(colIndex).getStringCellValue();
                                                if(tempStr != null){
                                                    String exceptStr = tempStr.replaceAll("[,]","");
                                                    if(exceptStr.matches("^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$")){
                                                        targetMethod.invoke(rowVo, new Object[] {BigDecimal.valueOf(Double.parseDouble(exceptStr))});
                                                        useExcelData = true;
                                                    }else{
                                                        throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                    }
                                                }else{
                                                    throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                }
                                            }else{
                                                targetMethod.invoke(rowVo, new Object[] {BigDecimal.valueOf(row.getCell(colIndex).getNumericCellValue())});
                                                useExcelData = true;
                                            }
                                            break;
                                        case "date":
                                            if(ctype.equals(CellType.NUMERIC)){
                                                int dataFormat = row.getCell(colIndex).getCellStyle().getDataFormat();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                                                if (DateUtil.isCellDateFormatted(row.getCell(colIndex)) || dataFormat == 14 || dataFormat == 55 || (dataFormat == 178 && !dataConvertMapper.getConvertFiledId().equals("coinQty"))) {
                                                    // 기존 date format
                                                    // excel style 중 선별
                                                    targetMethod.invoke(rowVo, new Object[] {dateFormat.format(row.getCell(colIndex).getDateCellValue())});
                                                    break;
                                                } else {
                                                    throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                }
                                            }else{
                                                if(dataConvertMapper.getDateFormat().equals("YYYY-mm-DD")){
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getStringCellValue().replaceAll("[/-]","")});
                                                    useExcelData = true;
                                                }else if(dataConvertMapper.getDateFormat().equals("YYYY.mm.DD")){
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getStringCellValue().replaceAll("[/.]","")});
                                                    useExcelData = true;
                                                }else{
                                                    throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                }
                                            }
                                            break;
                                        case "time":
                                            if(ctype.equals(CellType.NUMERIC)){
                                                int dataFormat = row.getCell(colIndex).getCellStyle().getDataFormat();
                                                if (dataFormat == 20 || dataFormat == 21) {
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern(dataConvertMapper.getDateFormat()))});
                                                    break;
                                                }else{
                                                    throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                }
                                            }else{
                                                if(dataConvertMapper.getDateFormat().equals("HH:mm:ss")){
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getStringCellValue().replaceAll("[/:]","")});
                                                    useExcelData = true;
                                                }else{
                                                    throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                                }
                                            }
                                            break;
                                        default:{
                                            switch (ctype){
                                                case STRING:
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getStringCellValue()});
                                                    useExcelData = true;
                                                    break;
                                                case NUMERIC:
                                                    targetMethod.invoke(rowVo, new Object[] {BigDecimal.valueOf(row.getCell(colIndex).getNumericCellValue())});
                                                    useExcelData = true;
                                                    break;
                                                case BOOLEAN:
                                                    targetMethod.invoke(rowVo, new Object[] {row.getCell(colIndex).getBooleanCellValue()});
                                                    useExcelData = true;
                                                    break;
                                                case BLANK:
                                                default:
                                                    targetMethod.invoke(rowVo, new Object[] {""});
                                                    useExcelData = true;
                                                    break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }catch (Exception e){
                        String addMessage = i+" 번째 줄 "+ colIndex+" 컬럼 변환중 에러가 발생했습니다.";
                        throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION, addMessage);
                    }
                }
            }
            if(useExcelData){
                result.add(rowVo);
            }
        }
        return result;
    }

    public List<Map<String, String>> convertExcelToVo(MultipartFile file, ExcelToVoConvertOptionVO options) throws IOException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Map<String, String>> result = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        Workbook workbook = getSheets(file, extension);

        // 첫번째 시트만
        Sheet worksheet = workbook.getSheetAt(0);
        int nullRows = 0;
        // row for 문
        if(options == null ){
            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_OPTION_IS_NULL);
        }
        if(options.getExcelOption() == null ){
            throw new BizExceptionMessage(ErrorType.EXCEL_OPTION_IS_NULL);
        }
        for (int i = options.getExcelOption().getHeaderStartIndex(); i < worksheet.getPhysicalNumberOfRows() + nullRows; i++) {
            Row row = worksheet.getRow(i);
            if (row == null) {
                nullRows++;
                continue;
            }
            Boolean useExcelData = false;
            Map<String, String> rowMap = new HashMap<>();
            Boolean emptyChk = true;
            for(ExcelToVoConvertOptionVO.DataConvertMapper dataConvertMapper : options.getDataConvertMapper()){
                String key = dataConvertMapper.getOriDBKey();
                String value = "";
                if(dataConvertMapper.getAddValue() != null && dataConvertMapper.getAddValue()){
                    if(dataConvertMapper.getAddValueType() != null && !"".equals(dataConvertMapper.getAddValueType())){
                        switch (dataConvertMapper.getAddValueType()){
                            case "APPLY_VALUE" :
                                rowMap.put(key, dataConvertMapper.getApplyValue());
                            break;
                            case "LOGIN_ID":
                                rowMap.put(key, options.getLoginId());
                            break;
                        }
                    }else{
                        throw new BizExceptionMessage(ErrorType.EXCEL_EMPTY_ADD_VALUE_TYPE);
                    }
                    continue;
                }
                if(dataConvertMapper !=null){
                    int colIndex = dataConvertMapper.getDataColumnIndex();
                    if(row.getCell(colIndex) == null || CellType.BLANK.equals(row.getCell(colIndex).getCellType())){
                        rowMap.put(key, "");
                        continue;
                    }
                    try {
                        if(dataConvertMapper.getMultipleId() != null && dataConvertMapper.getMultipleId()){
                            String[] splitStrs = row.getCell(colIndex).getStringCellValue().split(dataConvertMapper.getSplitStr());
                            for(int s = 0; s < dataConvertMapper.getConvertFiledIds().length; s ++){
                                if(dataConvertMapper.getDateFormats()[s].equals("YYYY-mm-DD")){
                                    rowMap.put(dataConvertMapper.getConvertFiledIds()[s], splitStrs[s].replaceAll("[/-]",""));
                                    useExcelData = true;
                                }else if(dataConvertMapper.getDateFormats()[s].equals("HH:mm:ss")){
                                    rowMap.put(dataConvertMapper.getConvertFiledIds()[s], splitStrs[s].replaceAll("[/:]",""));
                                    useExcelData = true;
                                }else if(dataConvertMapper.getDateFormats()[s].equals("YYYY.mm.DD")){
                                    rowMap.put(dataConvertMapper.getConvertFiledIds()[s], splitStrs[s].replaceAll("[/.]",""));
                                    useExcelData = true;
                                }
                            }
                        }else{
                            String dataType = dataConvertMapper.getConvertDataType();
                            CellType ctype = row.getCell(colIndex).getCellType();
                            if(ctype.equals(CellType.FORMULA)){
                                ctype = row.getCell(colIndex).getCachedFormulaResultType();
                            }
                            switch (dataType){
                                case "string":
                                    value = row.getCell(colIndex).getStringCellValue();
                                    useExcelData = true;
                                    break;
                                case "number":
                                    if(dataConvertMapper.getStringToNumber() != null && dataConvertMapper.getStringToNumber()){
                                        String tempStr = row.getCell(colIndex).getStringCellValue();
                                        if(tempStr != null){
                                            String exceptStr = tempStr.replaceAll("[,]","");
                                            if(exceptStr.matches("^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$")){
                                                value = BigDecimal.valueOf(Double.parseDouble(exceptStr)).toString();
                                                useExcelData = true;
                                            }else{
                                                throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                            }
                                        }else{
                                            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                        }
                                    }else{
                                        value = BigDecimal.valueOf(row.getCell(colIndex).getNumericCellValue()).toString();
                                        useExcelData = true;
                                    }
                                    break;
                                case "date":
                                    if(ctype.equals(CellType.NUMERIC)){
                                        int dataFormat = row.getCell(colIndex).getCellStyle().getDataFormat();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                                        if (DateUtil.isCellDateFormatted(row.getCell(colIndex)) || dataFormat == 14 || dataFormat == 55 || (dataFormat == 178 && !dataConvertMapper.getConvertFiledId().equals("coinQty"))) {
                                            // 기존 date format
                                            // excel style 중 선별
                                            value = dateFormat.format(row.getCell(colIndex).getDateCellValue());
                                            break;
                                        } else {
                                            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                        }
                                    }else{
                                        if(dataConvertMapper.getDateFormat().equals("YYYY-mm-DD")){
                                            value = row.getCell(colIndex).getStringCellValue().replaceAll("[/-]","");
                                            useExcelData = true;
                                        }else if(dataConvertMapper.getDateFormat().equals("YYYY.mm.DD")){
                                            value = row.getCell(colIndex).getStringCellValue().replaceAll("[/.]","");
                                            useExcelData = true;
                                        }else{
                                            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                        }
                                    }
                                    break;
                                case "time":
                                    if(ctype.equals(CellType.NUMERIC)){
                                        int dataFormat = row.getCell(colIndex).getCellStyle().getDataFormat();
                                        if (dataFormat == 20 || dataFormat == 21 || dataFormat == 179) {
                                            value = row.getCell(colIndex).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern(dataConvertMapper.getDateFormat()));
                                            break;
                                        }else{
                                            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                        }
                                    }else{
                                        if(dataConvertMapper.getDateFormat().equals("HH:mm:ss")){
                                            value = row.getCell(colIndex).getStringCellValue().replaceAll("[/:]","");
                                            useExcelData = true;
                                        }else{
                                            throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION);
                                        }
                                    }
                                    break;
                                default:{
                                    switch (ctype){
                                        case STRING:
                                            value = row.getCell(colIndex).getStringCellValue();
                                            useExcelData = true;
                                            break;
                                        case NUMERIC:
                                            value = BigDecimal.valueOf(row.getCell(colIndex).getNumericCellValue()).toString();
                                            useExcelData = true;
                                            break;
                                        case BOOLEAN:
                                            value = row.getCell(colIndex).getBooleanCellValue()+"";
                                            useExcelData = true;
                                            break;
                                        case BLANK:
                                        default:
                                            value = "";
                                            useExcelData = true;
                                            break;
                                    }
                                }
                            }
                            if(dataConvertMapper.getReplaceRegex() != null && !"".equals(dataConvertMapper.getReplaceRegex())){
                                value = value.replaceAll(dataConvertMapper.getReplaceRegex(),"");
                            }
                            if(dataConvertMapper.getNotEmpty() != null && dataConvertMapper.getNotEmpty()){
                                emptyChk = emptyChk(value);
                            }
                            rowMap.put(key,value);
                        }
                    }catch (Exception e){
                        String addMessage = i+" 번째 줄 "+ colIndex+" 컬럼 변환중 에러가 발생했습니다.";
                        throw new BizExceptionMessage(ErrorType.EXCEL_CONVERT_EXCEPTION, addMessage);
                    }
                }
            }
            if(useExcelData && emptyChk){
                result.add(rowMap);
            }
        }
        return result;
    }

    private Boolean emptyChk(String str){
        return str != null && !"".equals(str) ;
    }
}
