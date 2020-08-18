package com.kdax.bizportal.common.voHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseHeaderVO {
    private int code;       //
    private int status;
    private String message;         // 정상일 경우 사용 안함
    private String msgTypCod;       //Q : Question, C : Critical, I : Information, E : Exclamation , B : system
}
