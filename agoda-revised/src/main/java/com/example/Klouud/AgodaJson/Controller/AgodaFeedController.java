package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Enitity.AgodaDetailedHotelInfoObject;
import com.example.Klouud.AgodaJson.Enitity.CityHotelIdsCache;
import com.example.Klouud.AgodaJson.Enitity.RoomTypeFacilityV4;
import com.example.Klouud.AgodaJson.Enitity.StaticDumpHotel;
import com.example.Klouud.AgodaJson.Model.*;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.OTARequest;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.SearchResponse;
import com.example.Klouud.AgodaJson.Model.staticDumpDTOS.RoomAmenity;
import com.example.Klouud.AgodaJson.Repository.AgodaDetailedInfoForHotelRepository;
import com.example.Klouud.AgodaJson.Repository.NewAgodaStaticDumpHotelRepository;
import com.example.Klouud.AgodaJson.Service.AgodaFeedService;
import com.example.Klouud.AgodaJson.Service.AgodaStaticDumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AgodaFeedController {

    @Autowired
    AgodaFeedService agodaFeedService;

    @Autowired
    AgodaStaticDumpService agodaStaticDumpService;

    @Autowired
    NewAgodaStaticDumpHotelRepository newAgodaStaticDumpHotelRepository;

    @Autowired
    AgodaDetailedInfoForHotelRepository agodaDetailedInfoForHotelRepository;


    @PostMapping("/saveHotelListByCities")
    public ResponseEntity saveHotelListByCities() throws IOException {
        return agodaFeedService.saveHotelsByCities();
    }

    @GetMapping("/getHotelListByCity/{mcityId}")
    public ResponseEntity getHotelByCityList(@PathVariable Integer mcityId) throws IOException {
        HotelInformationByCity hotelInformationByCity =  agodaFeedService.getHotelsListByCity(mcityId);
        return new ResponseEntity<>(hotelInformationByCity,HttpStatus.OK);
    }

    @GetMapping("/getAllHotelInfo/{mhotelId}")
    public ResponseEntity getAllHotelInformation(@PathVariable Integer mhotelId) throws IOException{
        HotelFullInformation hotelFullInformation = agodaFeedService.getFullHotelInformation(mhotelId);
        return new ResponseEntity<>(hotelFullInformation,HttpStatus.OK);
    }

    @PostMapping("/saveCityListFromAgoda")
    public ResponseEntity saveCityList() throws IOException {
        return agodaFeedService.saveCitiesListV3();
    }

    @PostMapping("/saveAgodaCountryList")
    public ResponseEntity saveAgodaCountryList() throws IOException {
        return agodaFeedService.saveCountriesListV4();
    }

    @PostMapping("/saveHotelListByCityBetween/{cityId1}/{cityId2}")
    private ResponseEntity saveHotelListByCityBetween(@PathVariable Integer cityId1,@PathVariable Integer cityId2){
       return agodaFeedService.saveHotelListsByCityBetween(cityId1,cityId2);
    }

    @PostMapping("/saveHotelIdsListByCitiesV2/{pageNumber}/{pageSize}")
    public ResponseEntity saveHotelIdsListByCitiesV2(@PathVariable Integer pageNumber,@PathVariable Integer pageSize){
        return agodaFeedService.saveHotelIdsListByCitiesV2(pageNumber,pageSize);
    }

//    @PostMapping("/saveDetailedHotelInfo/{city1Id}/{city2Id}")
//    public ResponseEntity saveDetailedHotelInfo(@PathVariable Integer city1Id,@PathVariable Integer city2Id){
//        return agodaFeedService.saveDetailedInfoForHotels(city1Id,city2Id);
//    }
    @PostMapping("/saveDetailedHotelInfoV2/{pageNumber}/{pageSize}")
    public ResponseEntity saveDetailedHotelInfo(@PathVariable Integer pageNumber,@PathVariable Integer pageSize){
        return agodaFeedService.saveDetailedInfoForHotels(pageNumber,pageSize);
    }

//    @PostMapping("/propertyAvailabilitySearch")
//    public ResponseEntity propertyAvailabilitySearch(@RequestBody PropertyAvailabilityReq propertyAvailabilityReq) throws IOException {
//
//        return new ResponseEntity(agodaFeedService.getPropertyAvailability(propertyAvailabilityReq),HttpStatus.OK);
//    }

    @PostMapping("/preBookingCheck")
    public ResponseEntity preBookingCheck(@RequestBody PreCheckReq preCheckReq) throws IOException {
        PreCheckRes res = agodaFeedService.preBookingCheckService(preCheckReq);
        return new ResponseEntity(res,HttpStatus.OK);
    }

    @PostMapping("/bookService")
    public ResponseEntity bookingService(@RequestBody BookPropertyReq bookPropertyReq) throws IOException {
        BookPropertyRes bookPropertyRes = agodaFeedService.bookService(bookPropertyReq);
        return new ResponseEntity(bookPropertyRes,HttpStatus.OK);
    }

    @PostMapping("/agodaSearchHotels")
    public ResponseEntity agodaSearchHotels(@RequestBody OTARequest otaRequest) throws IOException {
        SearchResponse searchResponse = agodaFeedService.agodaSearchHotels(otaRequest);
        if(searchResponse != null)
        return new ResponseEntity(searchResponse,HttpStatus.OK);
        else
            return new ResponseEntity(null,HttpStatus.OK);
    }

    @PostMapping("/agodaRoomAvailability")
    public ResponseEntity agodaRoomAvailability(@RequestBody OTARequest otaRequest) throws IOException {
        RoomAvailabilityResponseV2 roomAvailabilityResponseV2 = agodaFeedService.agodaRoomAvailabilitySearch(otaRequest);
        if(roomAvailabilityResponseV2 != null){
            return new ResponseEntity(roomAvailabilityResponseV2,HttpStatus.OK);
        }
        else{
            return new ResponseEntity(null,HttpStatus.OK);
        }
    }

    @PostMapping("/getTimeForHotelSearch/{city}/{country}")
    public ResponseEntity getTime(@PathVariable String city,@PathVariable String country){
        long startTime = System.currentTimeMillis();
        agodaFeedService.findHotelListByCityAndCountry(city,country);
        long endTime = System.currentTimeMillis();

        long timeTaken =endTime-startTime;
        return new ResponseEntity(timeTaken,HttpStatus.OK);
    }


    @GetMapping("/getPropertyIds/{id}/{pageNumber}")
    public CityHotelIdsCache.PropertyIds getPropertyIds(@PathVariable String id,@PathVariable Integer pageNumber){
        CityHotelIdsCache.PropertyIds propertyIds = agodaFeedService.getHotelIdsByPageNumberAndId(pageNumber,id);
        return propertyIds;
    }

    @GetMapping("/getRoomTypeFacilities/{hotelId}/{roomTypeId}")
    public ResponseEntity getRoomTypeFacilities(@PathVariable Integer hotelId,@PathVariable Integer roomTypeId) throws IOException {
        RoomTypeFacilityV4 roomTypeFacilityV4 = agodaFeedService.getRoomTypeFacilities(hotelId);
       List<RoomTypeFacilityV4.RoomTypeFacilityFeed.RoomTypeFacilities.RoomTypeFacility> roomTypeFacilityV4List = new ArrayList<>();
        roomTypeFacilityV4.getRoomTypeFacilityFeed().getRoomTypeFacilities().getRoomTypeFacility().stream()
               .forEach(roomTypeFacility -> {
                   if(roomTypeFacility.getHotelRoomTypeId().equals(roomTypeId)){
                       roomTypeFacilityV4List.add(roomTypeFacility);
                   }
               });
        return new ResponseEntity(roomTypeFacilityV4List,HttpStatus.OK);
    }

    @GetMapping("/fetchStaticDumpByCity/{city}")
    public ResponseEntity getStaticDumpByCity(@PathVariable String city){
        List<StaticDumpHotel> staticDumpHotel = newAgodaStaticDumpHotelRepository.findByInformationCity(city);
        for(StaticDumpHotel staticDumpHotel1 : staticDumpHotel){
            staticDumpHotel1.getRoomInformations().stream().forEach(roomInformation -> {
                String id = staticDumpHotel1.getId();
                String[] hotelId = (id.split("_"));
                String mhotel_Id = hotelId[0];
                try {
                    RoomTypeFacilityV4 roomTypeFacilityV4 = agodaFeedService.getRoomTypeFacilities(Integer.valueOf(mhotel_Id));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(roomInformation.getAmenities() != null) {
                    List<RoomAmenity> roomAmenityList = new ArrayList<>();

                    roomInformation.setAmenities(roomAmenityList);
                }

             });
        }
        return new ResponseEntity(staticDumpHotel,HttpStatus.OK);
    }


    @PostMapping("/updateRoomAmenities/{hotelId}")
     public ResponseEntity updateStaticDumpRoomAmenitiesByHotel(@PathVariable String hotelId) throws IOException {

        StaticDumpHotel staticDumpHotel = agodaStaticDumpService.updateStaticDumpRoomAmenitiesByHotel(hotelId);
        return new ResponseEntity(staticDumpHotel,HttpStatus.OK);
     }
}

















