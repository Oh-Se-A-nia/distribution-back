package com.ohseania.ecotag.domain.ecotagVO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EcotagForm {

    @ApiModelProperty(value = "쓰레기 신고를 한 사람의 아이디", required = true)
    private String userId;

    @ApiModelProperty(value = "글에 해당하는 쓰레기의 사진", required = true)
    private MultipartFile picture;

    @ApiModelProperty(value = "글에 해당하는 쓰레기의 위치", required = true)
    private String location;

    @ApiModelProperty(value = "글에 해당하는 쓰레기의 타입", required = true)
    private String type;

    @ApiModelProperty(value = "글에 해당하는 쓰레기의 경도", required = true)
    private String longitude;

    @ApiModelProperty(value = "글에 해당하는 쓰레기의 위도", required = true)
    private String latitude;

}
