package com.kdax.bizportal.common.voCommon;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MenuVO {
    private String userCodeId;
    private String roleLevel;
    private String roleId;
    private String menuVersion;
    private String menuId;
    private String parentId;
    private String menuLrgCode;
    private String menuMdlCode;
    private String menuSmlCode;
    private String menuDtlCode;
    private int menuLevel;
    private String menuName;
    private String webPgmName;
    private String webPgmParam;
    private String useYn;
    private String remark;
    private Date createTime;
    private String createCodeId;
    private Date updateTime;
    private String updateCodeId;

    private List<MenuVO> subMenu;

}
