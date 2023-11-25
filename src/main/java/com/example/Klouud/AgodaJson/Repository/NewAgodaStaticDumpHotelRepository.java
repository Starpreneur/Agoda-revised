package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.AgodaDetailedHotelInfoObject;
import com.example.Klouud.AgodaJson.Enitity.StaticDumpHotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewAgodaStaticDumpHotelRepository extends MongoRepository<StaticDumpHotel,String> {

    List<StaticDumpHotel> findByInformationCity(String city);

    @Query(value = "{}", fields = "{ '_id' : 1, 'information.name' : 1 }")
    List<StaticDumpHotel> findAllProjected();

}
