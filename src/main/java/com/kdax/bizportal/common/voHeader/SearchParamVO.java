package com.kdax.bizportal.common.voHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchParamVO extends RequestHeaderVO {
    private String searchKeyword;
    private int page=1;
    private int size=10;
    private String sort;
}
