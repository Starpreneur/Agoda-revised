package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.HotelsListByCities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgodaHotelsListByCitiesV4Repository extends MongoRepository<HotelsListByCities, String> {

    HotelsListByCities findByCityNameIgnoreCaseAndCountryNameIgnoreCase(String city,String country);

    List<HotelsListByCities> findByCityIdBetween(Integer city1,Integer city2);

    @Override
    Page<HotelsListByCities> findAll(Pageable pageable);
}
