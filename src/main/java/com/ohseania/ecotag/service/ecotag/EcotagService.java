package com.ohseania.ecotag.service.ecotag;

import com.ohseania.ecotag.domain.ecotagVO.request.EcotagForm;
import com.ohseania.ecotag.domain.ecotagVO.response.EcoTypeCountInterface;
import com.ohseania.ecotag.domain.ecotagVO.response.EcotagCoordinate;
import com.ohseania.ecotag.entity.Ecotag;
import com.ohseania.ecotag.entity.Region;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EcotagService {

    HttpStatus uploadEcotag(EcotagForm EcotagForm);

    Ecotag createEcotag(EcotagForm ecotagForm, Region region);

    List<EcoTypeCountInterface> trackMostTrash();

    Ecotag updateCumulativeCount(Ecotag originEcotag);

    ResponseEntity<List<EcotagCoordinate>> getCoordinate();

}
