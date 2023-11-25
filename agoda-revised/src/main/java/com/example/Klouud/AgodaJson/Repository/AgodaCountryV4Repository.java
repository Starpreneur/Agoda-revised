package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.AgodaCountriesV4;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgodaCountryV4Repository extends MongoRepository<AgodaCountriesV4,String> {


    @Query(value = "{ 'countryName' : ?0 }", fields = "{ 'countryIso2' : 1,'_id' : 0 }")
    String findCountryIso2ByCountryNameIgnoreCase(String countryName);

    Optional<AgodaCountriesV4> findByCountryId(Integer countryId);


}
