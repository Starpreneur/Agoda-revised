package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Controller.RoomTypeFacilityV4Controller;
import com.example.Klouud.AgodaJson.Enitity.*;
import com.example.Klouud.AgodaJson.Repository.AgodaHotelsListByCitiesV4Repository;
import com.example.Klouud.AgodaJson.Repository.CitiesRepository;
import com.example.Klouud.AgodaJson.Repository.RoomTypeFacilitiesV4Repository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomFacilityV4Service {

    @Autowired
    RoomTypeFacilityV4Controller roomTypeFacilityV4Controller;

    @Autowired
    CitiesRepository citiesRepository;

    @Autowired
    AgodaHotelsListByCitiesV4Repository agodaHotelsListByCitiesV4Repository;

    @Autowired
    AgodaFeedService agodaFeedService;

    @Autowired
    RoomTypeFacilitiesV4Repository roomTypeFacilitiesV4Repository;

    public ResponseEntity saveRoomFacility(Integer pageNumber,Integer pageSize){
        long startTime = System.currentTimeMillis();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        for(int i=pageNumber;i<13;i++) {
            Pageable pageable = PageRequest.of(i, pageSize);
            Page<HotelsListByCities> citiesRecords = agodaHotelsListByCitiesV4Repository.findAll(pageable);
            log.info("Page Number : "+i);
            List<Integer> hotelIds = new ArrayList<>();
            citiesRecords.getContent().stream().forEach(city -> {
                hotelIds.addAll(city.getHotelList());
            });
            List<List<Integer>> partitionedList = Lists.partition(hotelIds,50);
            List<String> hotelIdsSaved = new ArrayList<>();
            for(List hotelIdList : partitionedList){
                List<RoomTypeFacilitiesByHotel> roomTypeFacilityByHotelList = new ArrayList<>();
                hotelIdList.parallelStream().forEach(hotelId -> {
                    try {
                        Optional<RoomTypeFacilitiesByHotel> optionalFacility = roomTypeFacilitiesV4Repository.findById(buildIdForRoomFacilityByHotel((Integer)hotelId));
                        if(!optionalFacility.isPresent()) {
                            RoomTypeFacilityV4 roomTypeFacilityV4 = agodaFeedService.getRoomTypeFacilities((Integer) hotelId);
                            count.getAndSet(count.get() + 1);
                            log.info("Number of calls to Agoda : "+count);
                            synchronized (roomTypeFacilityByHotelList) {
                                RoomTypeFacilitiesByHotel roomTypeFacilitiesByHotel = buildRoomTypeFacilitiesObjectV2(roomTypeFacilityV4, (Integer) hotelId);
                                roomTypeFacilityByHotelList.add(roomTypeFacilitiesByHotel);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
             List<RoomTypeFacilitiesByHotel> savedList = roomTypeFacilitiesV4Repository.saveAll(roomTypeFacilityByHotelList);
             hotelIdsSaved =  savedList.stream().map(hotel -> {
                 return hotel.getId();
             }).collect(Collectors.toList());
              log.info("Room Type Facility built for HotelIds :"+hotelIdsSaved);
              long endTime = System.currentTimeMillis();
              long timeTaken = ((endTime-startTime)/1000)/60;
              log.info("Time taken in execution :"+timeTaken+" minutes");
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }



    public RoomTypeFacilitiesByHotel buildRoomTypeByFacilitiesObject(RoomTypeFacilityV4 roomTypeFacilityV4,Integer hotelId) {
        RoomTypeFacilitiesByHotel roomTypeFacilitiesByHotel = new RoomTypeFacilitiesByHotel();
        try {
            String id = buildIdForRoomFacilityByHotel(hotelId);
            roomTypeFacilitiesByHotel.setId(id);
            List<RoomTypeFacilitiesByHotel.RoomTypeFacility> listToSave = new ArrayList<>();
            if(roomTypeFacilityV4 != null && roomTypeFacilityV4.getRoomTypeFacilityFeed() != null
                    && roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities() != null &&
            roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility() != null) {
                roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility().stream().forEach(roomTypeFacility1 -> {
                    RoomTypeFacilitiesByHotel.RoomTypeFacility toAdd = new RoomTypeFacilitiesByHotel.RoomTypeFacility();
                    toAdd.setHotelRoomTypeId(roomTypeFacility1.getHotelRoomTypeId() != null ? roomTypeFacility1.getHotelRoomTypeId() : null);
                    toAdd.setPropertyId(roomTypeFacility1.getPropertyId() != null ? roomTypeFacility1.getPropertyId() : null);
                    toAdd.setPropertyName(roomTypeFacility1.getPropertyName() != null ? roomTypeFacility1.getPropertyName() : null);
                    toAdd.setTranslatedName(roomTypeFacility1.getTranslatedName() != null ? roomTypeFacility1.getTranslatedName() : null);
                    listToSave.add(toAdd);
                });
            }
        roomTypeFacilitiesByHotel.setRoomTypeFacilityList(listToSave);
    }catch(NullPointerException e){
            e.printStackTrace();
        }
        return roomTypeFacilitiesByHotel;
    }

    //Building RoomTypeFacilityObject using Builder Pattern
    public RoomTypeFacilitiesByHotel buildRoomTypeFacilitiesObjectV2(RoomTypeFacilityV4 roomTypeFacilityV4,Integer hotelId){
        RoomTypeFacilitiesByHotel roomTypeFacilitiesByHotel = new RoomTypeFacilitiesByHotel();
        String id = buildIdForRoomFacilityByHotel(hotelId);
        roomTypeFacilitiesByHotel.setId(id);
        List<RoomTypeFacilitiesByHotel.RoomTypeFacility> listToSave = new ArrayList();
        if(roomTypeFacilityV4 != null && roomTypeFacilityV4.getRoomTypeFacilityFeed() != null
                && roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities() != null &&
                roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility() != null){
            listToSave = roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility().stream()
                    .map(roomTypeFacility -> {
                        return RoomTypeFacilitiesByHotel.RoomTypeFacility.builder()
                                .hotelRoomTypeId(roomTypeFacility.getHotelRoomTypeId() != null ? roomTypeFacility.getHotelRoomTypeId() : null)
                                .propertyId(roomTypeFacility.getPropertyId() != null ? roomTypeFacility.getPropertyId() : null)
                                .propertyName(roomTypeFacility.getPropertyName() != null ? roomTypeFacility.getPropertyName() : null)
                                .translatedName(roomTypeFacility.getTranslatedName() != null ? roomTypeFacility.getTranslatedName() : null)
                                .build();
                    }).collect(Collectors.toList());
            roomTypeFacilitiesByHotel.setRoomTypeFacilityList(listToSave);
        }
        return roomTypeFacilitiesByHotel;
    }

    public String buildIdForRoomFacilityByHotel(Integer hotelId){
        StringBuilder sb = new StringBuilder();
        sb.append(hotelId).append("_").append("AGODA");
        String id = sb.toString();
        return id;
    }
    RoomTypeFacilitiesByHotel fetchRoomTypeFacilitiesForHotel(Integer hotelId){
        StringBuilder builder = new StringBuilder();
        builder.append(hotelId.toString()).append("_").append("AGODA");
        String id = builder.toString();
        Optional<RoomTypeFacilitiesByHotel> optional = roomTypeFacilitiesV4Repository.findById(id);
        RoomTypeFacilitiesByHotel toReturn = new RoomTypeFacilitiesByHotel();
        if(optional.isPresent()) {
            toReturn = optional.get();
        }
        return toReturn;
    }
}



