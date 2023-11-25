package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Enitity.AgodaCountriesV4;
import com.example.Klouud.AgodaJson.Enitity.AgodaDetailedHotelInfoObject;
import com.example.Klouud.AgodaJson.Repository.AgodaDetailedInfoForHotelRepository;
import com.example.Klouud.AgodaJson.Service.AgodaStaticDumpService;
import com.example.Klouud.AgodaJson.Utils.StaticDataUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/staticDumpIngest")
public class AgodaStaticDumpController {

    @Autowired
    private AgodaStaticDumpService agodaStaticDumpService;

    @Autowired
    private StaticDataUtils staticDataUtils;

    @Autowired
    private AgodaDetailedInfoForHotelRepository agodaDetailedInfoForHotelRepository;

    @PostMapping("/saveNewStaticDumpHotel/{pageSize}/{pageNumber}")
    public ResponseEntity saveNewStaticDumpHotel(@PathVariable Integer pageSize,@PathVariable Integer pageNumber){

        List<AgodaCountriesV4> countriesV4List = agodaStaticDumpService.getCountriesList();
        HashMap<Integer, String> countriesCodeMap = agodaStaticDumpService.countriesCodeMap(countriesV4List);
        HashMap<String,String> staticDumpMap = staticDataUtils.buildStaticDumpMap();
        Pageable pageable = PageRequest.of(0,50);
        Page<AgodaDetailedHotelInfoObject> page = agodaDetailedInfoForHotelRepository.findAll(pageable);
        for(int i = pageNumber; i < page.getTotalPages() ;i++) {
            Pageable pageable1 = PageRequest.of(i,50);
            Page<AgodaDetailedHotelInfoObject> detailedHotelInfoObjects = agodaDetailedInfoForHotelRepository.findAll(pageable1);
            List<AgodaDetailedHotelInfoObject> listToSave = new ArrayList<>();
            if(detailedHotelInfoObjects.getContent().size() > 0 && !detailedHotelInfoObjects.getContent().isEmpty()) {
                listToSave = detailedHotelInfoObjects.getContent();
                agodaStaticDumpService.saveStaticDumpHotel(listToSave, countriesCodeMap,staticDumpMap);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/viewDetails")
    public ResponseEntity viewStaticDumpHotelDetails(@RequestParam String hotelId){
        return new ResponseEntity(agodaStaticDumpService.fetchStaticDumpHotel(hotelId),HttpStatus.OK);
    }
}
