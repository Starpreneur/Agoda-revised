package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.RoomAvailabilitySessionDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAvailabilitySessionDetailsRepository extends MongoRepository<RoomAvailabilitySessionDetails,String> {




}
