package com.kdax.bizportal.common.util.setter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ExcelSheetConfig {
    @Builder.Default
    private Boolean notIncludeSheetTitle = false;
    @Builder.Default
    private Boolean notIncludeSheetHeader = false;
    @Builder.Default
    private Boolean notIncludeSheetSummary = false;

    @Builder.Default
    private Boolean existSheetTitle = false; // true : row index 0
    @Builder.Default
    private Boolean existSheetHeader = false; // true : title next row
    @Builder.Default
    private Boolean existSheetSummary = false; // true : last row

    @Builder.Default
    private int sheetTitleIndex = -1;
    @Builder.Default
    private int sheetHeaderIndex = -1;
    @Builder.Default
    private int sheetSummaryIndex = -1;

    private List<String> sheetHeaders;

    public ExcelSheetConfig(){
    }

    public ExcelSheetConfig(Boolean notIncludeSheetTitle, Boolean notIncludeSheetHeader, Boolean notIncludeSheetSummary, Boolean existSheetTitle, Boolean existSheetHeader, Boolean existSheetSummary, int sheetTitleIndex, int sheetHeaderIndex, int sheetSummaryIndex, List<String> sheetHeaders) {
        this.notIncludeSheetTitle = notIncludeSheetTitle;
        this.notIncludeSheetHeader = notIncludeSheetHeader;
        this.notIncludeSheetSummary = notIncludeSheetSummary;
        this.existSheetTitle = existSheetTitle;
        this.existSheetHeader = existSheetHeader;
        this.existSheetSummary = existSheetSummary;
        this.sheetTitleIndex = sheetTitleIndex;
        this.sheetHeaderIndex = sheetHeaderIndex;
        this.sheetSummaryIndex = sheetSummaryIndex;
        this.sheetHeaders = sheetHeaders;
    }

    public ExcelSheetConfig(ExcelUtilConfig config, int index ){
        this.notIncludeSheetTitle = config.getNotIncludeAllSheetTitle();
        this.notIncludeSheetHeader = config.getNotIncludeAllSheetHeader();
        this.notIncludeSheetSummary = config.getNotIncludeAllSheetSummary();

        this.existSheetTitle = config.getExistAllSheetTitle();
        this.existSheetHeader = config.getExistAllSheetHeader();
        this.existSheetSummary = config.getExistAllSheetSummary();

        if(config.getSheetTitleIndex() !=null && config.getSheetTitleIndex().size()>0){
            this.sheetTitleIndex = config.getSheetTitleIndex().get(index);
            this.existSheetTitle = true;
        }
        if(config.getSheetHeaderIndex() !=null && config.getSheetHeaderIndex().size()>0){
            this.sheetHeaderIndex = config.getSheetHeaderIndex().get(index);
            this.existSheetHeader = true;
        }
        if(config.getSheetSummaryIndex() !=null && config.getSheetSummaryIndex().size()>0){
            this.sheetSummaryIndex = config.getSheetSummaryIndex().get(index);
            this.existSheetSummary = true;
        }

        if(config.getSheetHeaders() !=null)
            this.sheetHeaders = config.getSheetHeaders().get(index);
    }
}
