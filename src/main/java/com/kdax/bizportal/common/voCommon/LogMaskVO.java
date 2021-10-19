package com.kdax.bizportal.common.voCommon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public
class LogMaskVO {
    private List<LogMaskVOItem> logMaskVOItem;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public
    static class LogMaskVOItem {
        private String descript;
        private String pattern;
        private String regex;
        private String subst;
    }
}