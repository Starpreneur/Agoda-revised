package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Enitity.EncompassCities;
import com.example.Klouud.AgodaJson.Enitity.ScrunchCitiesV2;
import com.example.Klouud.AgodaJson.Service.EncompassFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/encompassFormatter")
public class EncompassFormatController {

    @Autowired
    private EncompassFormatter encompassFormatter;

    @PostMapping("/formatCitiesInEncompassFormatter/{cityId1}/{cityId2}")
    public ResponseEntity encompassCityFormatter(@PathVariable Integer cityId1,@PathVariable Integer cityId2){

        List<EncompassCities> citiesSaved = encompassFormatter.saveEncompassCities(cityId1,cityId2);
        return new ResponseEntity(citiesSaved, HttpStatus.OK);

    }

    @PostMapping("/saveScrunchCitiesV2/{cityId1}/{cityId2}")
    public ResponseEntity saveScrunchCitiesV2(@PathVariable Integer cityId1,@PathVariable Integer cityId2){

        List<ScrunchCitiesV2> ctiesSaved = encompassFormatter.saveScrunchCitiesV2(cityId1,cityId2);
        return new ResponseEntity(ctiesSaved,HttpStatus.OK);
    }

}
