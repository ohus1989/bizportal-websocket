package com.kdax.bizportal.common.util.setter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
@Builder
public class ExcelUtilConfig {
    @Builder.Default
    private Boolean notIncludeAllSheetTitle = false;
    @Builder.Default
    private Boolean notIncludeAllSheetHeader = false;
    @Builder.Default
    private Boolean notIncludeAllSheetSummary = false;

    @Builder.Default
    private Boolean existAllSheetTitle = false; // true : row index 0
    @Builder.Default
    private Boolean existAllSheetHeader = false; // true : title next row
    @Builder.Default
    private Boolean existAllSheetSummary = false; // true : last row

    //Not referenced if existAllSheetTitle is true
    private List<Integer> sheetTitleIndex; // default not exist sheet title next row
    //Not referenced if existAllSheetHeader is true
    private List<Integer> sheetHeaderIndex; // default not exist sheet title next row
    //Not referenced if existAllSheetSummary is true
    private List<Integer> sheetSummaryIndex; // default sheet last row

    private List<List<String>> sheetHeaders; //not exist return col_(col_index)

    private List<Integer> sheetAccountIndex;

    public ExcelUtilConfig(Boolean notIncludeAllSheetTitle, Boolean notIncludeAllSheetHeader, Boolean notIncludeAllSheetSummary, Boolean existAllSheetTitle, Boolean existAllSheetHeader, Boolean existAllSheetSummary, List<Integer> sheetTitleIndex, List<Integer> sheetHeaderIndex, List<Integer> sheetSummaryIndex, List<List<String>> sheetHeaders, List<Integer> sheetAccountIndex) {
        this.notIncludeAllSheetTitle = notIncludeAllSheetTitle;
        this.notIncludeAllSheetHeader = notIncludeAllSheetHeader;
        this.notIncludeAllSheetSummary = notIncludeAllSheetSummary;
        this.existAllSheetTitle = existAllSheetTitle;
        this.existAllSheetHeader = existAllSheetHeader;
        this.existAllSheetSummary = existAllSheetSummary;
        this.sheetTitleIndex = sheetTitleIndex;
        this.sheetHeaderIndex = sheetHeaderIndex;
        this.sheetSummaryIndex = sheetSummaryIndex;
        this.sheetHeaders = sheetHeaders;
        this.sheetAccountIndex = sheetAccountIndex;
    }

    public void setSheetHeaders(String sheetHeaders) throws IOException {
        this.sheetHeaders = new ObjectMapper().readValue(sheetHeaders, List.class);
    }

    public void setSheetHeaders(List<List<String>> sheetHeaders) {
        this.sheetHeaders = sheetHeaders;
    }
}
