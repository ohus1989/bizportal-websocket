package com.kdax.bizportal.common.voCommon;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageParamVO {
    private int startRow, endRow;

    // row group columns
    private List<Map> rowGroupCols;

    // value columns
    private List<Map> valueCols;

    // pivot columns
    private List<Map> pivotCols;

    // true if pivot mode is one, otherwise false
    private boolean pivotMode;

    // what groups the user is viewing
    private List<String> groupKeys;

    // if filtering, what the filter model is
    private Map<String, Map> filterModel;

    // if sorting, what the sort model is
    private List<Map> sortModel;

    private int nextRow;

    public int getNextRow() {
        return endRow - startRow + 1;
    }
}
