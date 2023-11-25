package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.ScrunchCitiesV2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrunchCitiesV2Repository extends MongoRepository<ScrunchCitiesV2,String> {
}
