package com.kdax.bizportal.common.voCommon;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MenuVO implements Serializable {
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
    private String btnAuthLevel;
    private String iconClass;
    private String btnAuthText;

    private List<MenuVO> subMenu;

}
