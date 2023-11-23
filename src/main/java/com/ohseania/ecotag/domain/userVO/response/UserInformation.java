package com.ohseania.ecotag.domain.userVO.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInformation {

    @ApiModelProperty(value = "로그인되어 있는 유저의 pk 값")
    private Long userId;    // 클라이언트 단에서 들고 다닐 유저의 pk 값

}