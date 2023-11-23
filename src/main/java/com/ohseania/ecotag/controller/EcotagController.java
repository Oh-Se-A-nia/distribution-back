package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.ecotagVO.request.EcotagForm;
import com.ohseania.ecotag.domain.ecotagVO.response.EcotagCoordinate;
import com.ohseania.ecotag.service.ecotag.EcotagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"Ecotag API"}, description = "에코택 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/ecotag")
public class EcotagController {

    private final EcotagService ecotagService;

    @ApiOperation(value = "에코택 등록 API")
    @PostMapping("/post")
    public HttpStatus uploadEcotag(@RequestParam(value = "picture") MultipartFile picture,
                                   @RequestParam(value = "latitude") String latitude,
                                   @RequestParam(value = "location") String location,
                                   @RequestParam(value = "longitude") String longitude,
                                   @RequestParam(value = "type") String type,
                                   @RequestParam(value = "userId") String userId
    ) {
        EcotagForm ecotag = EcotagForm.builder()
                .userId(userId)
                .longitude(longitude)
                .latitude(latitude)
                .location(location)
                .picture(picture)
                .type(type)
                .build();
        return ecotagService.uploadEcotag(ecotag);
    }

    @ApiOperation(value = "지역별 가장 많은 쓰레기 조회")
    @PostMapping("/most-trash")
    public ResponseEntity checkMostTrashByLocation() {
        return new ResponseEntity(ecotagService.trackMostTrash(), HttpStatus.OK);
    }

    @ApiOperation(value = "쓰레기 정보들의 좌표 값 조회")
    @PostMapping("/coordinate")
    public ResponseEntity<List<EcotagCoordinate>> returnEcotagCoordinates () {
        return ecotagService.getCoordinate();
    }

}
