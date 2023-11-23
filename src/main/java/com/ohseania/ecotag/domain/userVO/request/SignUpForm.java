package com.ohseania.ecotag.domain.userVO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class SignUpForm {

    @ApiModelProperty(value = "로그인한 유저의 아이디", required = true)
    private Long id;

    @ApiModelProperty(value = "로그인한 유저의 아이디", required = true)
    private String nickname;

}