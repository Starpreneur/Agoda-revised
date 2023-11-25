package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.Cities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CitiesRepository extends MongoRepository<Cities, String> {


    List<Cities> findByCityIdBetween(Integer cityId1,Integer cityId2);

    @Override
    Page<Cities> findAll(Pageable pageable);
}
