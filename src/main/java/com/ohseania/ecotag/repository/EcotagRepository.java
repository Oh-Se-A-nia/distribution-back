package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.domain.ecotagVO.response.EcoTypeCountInterface;
import com.ohseania.ecotag.entity.Ecotag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EcotagRepository extends JpaRepository<Ecotag, Long> {

    @Query(value = "with RankedTrash as (\n" +
            "    select\n" +
            "        region_id,\n" +
            "        ecotag_type,\n" +
            "        count(*) as trash_count,\n" +
            "        ROW_NUMBER() over (partition by region_id order by count(*) desc) as Ran\n" +
            "    from ecotag\n" +
            "    group by region_id, ecotag_type\n" +
            ")\n" +
            "select region_name, ecotag_type, trash_count\n" +
            "from RankedTrash \n" +
            "inner join region  on region_id = region_id\n" +
            "where Ran = 1;",
            nativeQuery = true)
    List<EcoTypeCountInterface> findByMostTrash();

    List<Ecotag> findByLatitudeAndLongitude(Double latitude, Double longitude);

}
