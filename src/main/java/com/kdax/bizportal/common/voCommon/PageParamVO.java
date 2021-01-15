package com.kdax.bizportal.common.voCommon;

import com.kdax.bizportal.common.util.StringUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PageParamVO {
    private int startRow;
    private int endRow;

    // row group columns
    private List<Map<String, Object>> rowGroupCols;

    // value columns
    private List<Map<String, Object>> valueCols;

    // pivot columns
    private List<Map<String, Object>> pivotCols;

    // true if pivot mode is one, otherwise false
    private boolean pivotMode;

    // what groups the user is viewing
    private List<String> groupKeys;

    // if filtering, what the filter model is
    private Map<String, Map<String, Object>> filterModel;

    void setFilterModel(Map<String, Map<String, Object>> filterModel){
        Map<String, Map<String, Object>> temp = new HashMap<>();
        for(Map.Entry<String, Map<String, Object>> entry : filterModel.entrySet()){
            temp.put(StringUtil.camelCaseToSnakeCase(entry.getKey()), entry.getValue());
        }
        this.filterModel = temp;
    }
    // if sorting, what the sort model is
    private List<Map<String, Object>> sortModel;

    void setSortModel(List<Map<String, Object>> sortModel) {
        final String COL_ID = "colId";
        for (Map<String, Object> item : sortModel) {
            if (item.containsKey(COL_ID)) {
                item.put(COL_ID, StringUtil.camelCaseToSnakeCase(item.get(COL_ID).toString()));
            }
        }
        this.sortModel = sortModel;
    }


    private int nextRow;

    public int getNextRow() {
        return endRow - startRow + 1;
    }

}
