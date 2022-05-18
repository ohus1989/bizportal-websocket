package com.kdax.bizportal.common.voCommon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String codeId;
    private String account;
    private String password;
    private String userName;
    private String gender;
    private String deptId;
    private String birthday;
    private String calendarClassification;
    private String address;
    private String serviceLocale;
    private String mobileNumber;
    private String homeNumber;
    private String email;
    private String description;
    private String googleSecret;
    private String useYn;
    private String createTime;
    private String createCodeId;
    private String updateTime;
    private String updateCodeId;
    //
    private String eSignOnId;
    private String eSignOnPwd;
    private String fcmToken;
    private String officersNumber;
    private String rcmndNumber;
    private String workType;
    private String jobCode;
    private String jobCodeName;
    private String employeeNumber;
    private String passwordChangeDate;
    private String department;
    private String departmentName;
    private String changePwdDate;
    private String changePwd;
    private String otpSecretKey;
    private String expireTime;

}
