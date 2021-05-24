package com.kdax.bizportal.common.voCommon;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelToVoConvertOptionVO {
    private String tableName;
    private String loginId;
    private ExcelOption excelOption;
    private List<DataConvertMapper> dataConvertMapper;
    @Getter
    @Setter
    public class ExcelOption{
        private int headerStartIndex;
    }
    @Getter
    @Setter
    public class DataConvertMapper{
        private int dataColumnIndex;
        private String convertFiledId;
        private String []convertFiledIds;
        private String convertDataType;
        private String [] dateFormats;
        private String dateFormat;
        private String splitStr;
        private Boolean multipleId;
        private Boolean stringToNumber;
        private String oriDBKey;
        private Boolean addValue = false;
        private String addValueType;
        private String applyValue;
        private Boolean notEmpty;
        private String replaceRegex;
        private Boolean isPk;
        private Boolean insertOnly;
    }
}
