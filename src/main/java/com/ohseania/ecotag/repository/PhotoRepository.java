package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByEcotagId(Long ecotagId);

    Photo findByType(String type);

}