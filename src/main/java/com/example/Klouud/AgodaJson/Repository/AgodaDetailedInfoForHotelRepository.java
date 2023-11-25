package com.example.Klouud.AgodaJson.Repository;

import com.example.Klouud.AgodaJson.Enitity.AgodaDetailedHotelInfoObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgodaDetailedInfoForHotelRepository extends MongoRepository<AgodaDetailedHotelInfoObject,String> {

    List<AgodaDetailedHotelInfoObject> findByHotelsCityId(Integer cityId);



    Optional<AgodaDetailedHotelInfoObject> findByHotelsHotelId(Integer hotelId);

    @Query(value = "{}", fields = "{ 'hotels.hotelId' : 1, 'hotels.hotelName' : 1 }")
    List<AgodaDetailedHotelInfoObject> findAllProjected();

    @Override
    Page<AgodaDetailedHotelInfoObject> findAll(Pageable pageable);
}
