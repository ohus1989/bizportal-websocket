package com.kdax.bizportal.common.voHeader;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchParamVO extends RequestHeaderVO {

    @ApiModelProperty(value = "문자검색 키워드", example = "")
    private String searchTextKeyword;
    @ApiModelProperty(value = "문자검색 값", example = "")
    private String searchTextValue;
    @ApiModelProperty(value = "날짜검색 키워드", example = "")
    private String searchDateKeyword;
    @ApiModelProperty(value = "검색 시작일")
    private String searchDateStart;
    @ApiModelProperty(value = "검색 종료일")
    private String searchDateEnd;
    @ApiModelProperty(value = "페이지")
    private int page=1;
    @ApiModelProperty(value = "페이지당 목록수")
    private int limit=10;
    private String sort;
}
