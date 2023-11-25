package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.RoomTypeFacilitiesByHotel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomTypeFacilitiesV4Repository extends MongoRepository<RoomTypeFacilitiesByHotel,Integer> {


    Optional<RoomTypeFacilitiesByHotel> findById(String id);


}
