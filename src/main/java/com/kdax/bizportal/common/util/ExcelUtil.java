package com.kdax.bizportal.common.util;

import com.kdax.bizportal.common.util.setter.ExcelSheetConfig;
import com.kdax.bizportal.common.util.setter.ExcelUtilConfig;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ExcelUtil {
    private static final String preColname = "col_";

    public static List fileToList(MultipartFile file) throws Exception{
        return fileToList(file, ExcelUtilConfig.builder().build());
    }

    public List fileToList(MultipartFile file, ExcelUtilConfig config) throws Exception{

        List returnList = new ArrayList();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        for(int sIndex =0; sIndex < workbook.getNumberOfSheets(); sIndex++){
            //config
            ExcelSheetConfig sConfig = new ExcelSheetConfig(config , sIndex);

            List<Map> dataList = new ArrayList<>();
            Map sheetMap = new HashMap();
            Sheet worksheet = workbook.getSheetAt(sIndex);

            Boolean titleFlag = true, headerFlag = true, summaryFlag = true ;

            int nullRows = 0;
            for (int i = 0; i < worksheet.getPhysicalNumberOfRows() + nullRows; i++) {
                Row row = worksheet.getRow(i);

                if(row ==null){
                    nullRows++;
                    continue;
                }

                // title header summary
                if(excelTitleValid(titleFlag, sConfig, i, nullRows)
                        ||excelHeaderValid(headerFlag, sConfig, i, nullRows)
                        ||excelSummaryValid(summaryFlag, sConfig, worksheet, i, nullRows)
                        ||row.getLastCellNum()<0){
                    continue;
                }

                Map data = new HashMap();
                for(int j = 0; j < row.getLastCellNum();j++){
                    if(row.getCell(j) ==null){
                        data.put(createColName(sConfig,j),"");
                        continue;
                    }
                    CellType ctype = row.getCell(j).getCellType();
                    switch (ctype){
                        case STRING:

                            data.put(createColName(sConfig,j),row.getCell(j).getStringCellValue());
                            break;
                        case NUMERIC:
                            //cell style format get (doc이 나오는 데이터와 상이 하므로 추후 수정 가능
                            int dataFormat = row.getCell(j).getCellStyle().getDataFormat();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                            if(dataFormat ==20 || dataFormat ==21){
                                data.put(createColName(sConfig,j), row.getCell(j).getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                                break;
                            }else if(DateUtil.isCellDateFormatted(row.getCell(j)) || dataFormat ==14 || dataFormat ==55 || dataFormat ==178){
                                // 기존 date format
                                // excel style 중 선별
                                data.put(createColName(sConfig,j),dateFormat.format(row.getCell(j).getDateCellValue()));
                                break;
                            }else{
                                data.put(createColName(sConfig,j),row.getCell(j).getNumericCellValue());
                                break;
                            }
                        case BOOLEAN:
                            data.put(createColName(sConfig,j),row.getCell(j).getBooleanCellValue());
                            break;
                        case BLANK:
                            data.put(createColName(sConfig,j),"");
                            break;
                        default:
                            if(createColName(sConfig,j).indexOf(preColname)<0){
                                int createDataFormat = row.getCell(j).getCellStyle().getDataFormat();
                                if(createDataFormat ==4){
                                    data.put(createColName(sConfig,j),row.getCell(j).getNumericCellValue());
                                }else{
                                    data.put(createColName(sConfig,j),"");
                                }
                            }else{
                                data.put(createColName(sConfig,j),"");
                            }
                            break;
                    }
                }
                dataList.add(data);
            }

            sheetMap.put("SHEET_NAME",worksheet.getSheetName());
            sheetMap.put("SHEET_DATA",dataList);
            returnList.add(sheetMap);
        }

        return returnList;
    }

    private String createColName(ExcelSheetConfig config, int index){
        if(config != null && config.getSheetHeaders() != null && config.getSheetHeaders().get(index) !=null && !config.getSheetHeaders().get(index).toString().equals("")){
            return config.getSheetHeaders().get(index).toString();
        }else{
            return preColname+String.format("%03d",index);
        }
    }

    private Boolean excelTitleValid(Boolean titleFlag, ExcelSheetConfig sConfig , int i, int nullRows){
        Boolean returnFlag = false;
        if(titleFlag && sConfig.getExistSheetTitle() && sConfig.getNotIncludeSheetTitle()){
            if(sConfig.getSheetTitleIndex() > -1){
                if(sConfig.getSheetTitleIndex() == i){
                    titleFlag = false;
                    returnFlag = true;
                }
            }else{
                if(i-nullRows ==0){
                    titleFlag = false;
                    returnFlag = true;
                }
            }
        }else{
            titleFlag = false;
        }
        return  returnFlag;
    }

    private Boolean excelHeaderValid(Boolean headerFlag, ExcelSheetConfig sConfig , int i, int nullRows){
        Boolean returnFlag = false;
        if(headerFlag && sConfig.getExistSheetHeader() && sConfig.getNotIncludeSheetHeader()){
            if(sConfig.getSheetHeaderIndex() > -1){
                if(sConfig.getSheetHeaderIndex() >= i){
                    headerFlag = false;
                    returnFlag = true;
                }
            }else{
                if(i-nullRows ==1){
                    headerFlag = false;
                    returnFlag = true;
                }
            }
        }else{
            headerFlag = false;
        }
        return  returnFlag;
    }

    private Boolean excelSummaryValid(Boolean summaryFlag, ExcelSheetConfig sConfig, Sheet worksheet, int i, int nullRows){
        Boolean returnFlag = false;
        if(summaryFlag && sConfig.getExistSheetSummary() && sConfig.getNotIncludeSheetSummary()){
            if(sConfig.getSheetSummaryIndex() > -1){
                if(sConfig.getSheetSummaryIndex() == i){
                    summaryFlag = false;
                    returnFlag = true;
                }
            }else{
                if(i == worksheet.getPhysicalNumberOfRows() + nullRows ){
                    summaryFlag = false;
                    returnFlag = true;
                }
            }
        }else{
            summaryFlag = false;
        }
        return  returnFlag;
    }
}
