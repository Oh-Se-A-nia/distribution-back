package com.ohseania.ecotag.service.region;

import com.ohseania.ecotag.entity.Region;
import com.ohseania.ecotag.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private static final String DETAIL_LOCATION = "동";
    private final RegionRepository regionRepository;

    public Region formatRegion(String location) {
        System.out.println(location);
        StringTokenizer st = new StringTokenizer(location);
        List<String> locationToken = new ArrayList<>();

        while (st.hasMoreTokens()) {
            locationToken.add(st.nextToken());
        }

        String detailRegion = parseLocation(locationToken);

        validateLocationFormat(detailRegion);
        return saveRegion(detailRegion);
    }

    private String parseLocation(List<String> location) {
        for (int i = location.size() - 1; i >= 0; i--) {
            if (location.get(i).contains(DETAIL_LOCATION)) {
                return location.get(i);
            }
        }

        return location.get(location.size() - 2);
    }

    private void validateLocationFormat(String location) {
        if (location == null) {
            throw new IllegalArgumentException("주소가 잘못됐어요");
        }
    }

    private Region saveRegion(String location) {
        Region region = regionRepository.findByRegionName(location);

        if (region == null) {
            region = Region.builder()
                    .regionName(location)
                    .build();

            regionRepository.save(region);
            System.out.println(regionRepository.findByRegionName(location));
        }

        return region;
    }

}
