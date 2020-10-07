package com.kdax.bizportal.common.voHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchParamVO extends RequestHeaderVO {

    private String searchTextKeyword;
    private String searchTextValue;
    private String searchDateKeyword;
    private String searchDateStart;
    private String searchDateEnd;
    private int page=1;
    private int limit=10;
    private String sort;
}
