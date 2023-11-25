package com.example.Klouud.AgodaJson.Service;

import com.example.Klouud.AgodaJson.Enitity.AgodaCountriesV4;
import com.example.Klouud.AgodaJson.Enitity.Cities;
import com.example.Klouud.AgodaJson.Enitity.EncompassCities;
import com.example.Klouud.AgodaJson.Enitity.ScrunchCitiesV2;
import com.example.Klouud.AgodaJson.Repository.AgodaCountryV4Repository;
import com.example.Klouud.AgodaJson.Repository.CitiesRepository;
import com.example.Klouud.AgodaJson.Repository.EncompassCitiesRepository;
import com.example.Klouud.AgodaJson.Repository.ScrunchCitiesV2Repository;
import com.example.Klouud.AgodaJson.Utils.StaticDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EncompassFormatter {

    @Autowired
    private StaticDataUtils staticDataUtils;


    @Autowired
    private EncompassCitiesRepository encompassCitiesRepository;

    @Autowired
    private CitiesRepository citiesRepository;

    @Autowired
    private AgodaCountryV4Repository agodaCountryV4Repository;

    @Autowired
    private ScrunchCitiesV2Repository scrunchCitiesV2Repository;

    public List<EncompassCities> saveEncompassCities(Integer cityId1,Integer cityId2){

        List<Cities> citiesList = citiesRepository.findByCityIdBetween(cityId1,cityId2);
        List<EncompassCities> listToSave = new ArrayList<>();
        citiesList.stream().forEach(cities -> {
           EncompassCities encompassCity = new EncompassCities();
           encompassCity.setCity(cities.getCityName());
           //set country
           EncompassCities.Country country = new EncompassCities.Country();
           Optional<AgodaCountriesV4> countriesV4 = agodaCountryV4Repository.findByCountryId(cities.getCountryId());
           if(countriesV4.isPresent()) {
               country.setName(countriesV4.get().getCountryName());
               country.setNumericCode(countriesV4.get().getCountryId().toString());
               country.setAlpha2Code(countriesV4.get().getCountryIso2());
               country.setAlpha3Code(countriesV4.get().getCountryIso());
           }
           encompassCity.setCountry(country);
           listToSave.add(encompassCity);
        });

        List<EncompassCities> toReturn = encompassCitiesRepository.saveAll(listToSave);
        log.info("cities saved between "+cityId1+" and "+cityId2);
        return toReturn;

    }

    public List<ScrunchCitiesV2> saveScrunchCitiesV2(Integer cityId1,Integer cityId2){

        List<Cities> citiesList = citiesRepository.findByCityIdBetween(cityId1,cityId2);
        List<ScrunchCitiesV2> toSave = new ArrayList<>();

        citiesList.stream().forEach(city -> {
            ScrunchCitiesV2 scrunchCitiesV2 = new ScrunchCitiesV2();
            scrunchCitiesV2.setCity(city.getCityName());
            Optional<AgodaCountriesV4> country = agodaCountryV4Repository.findByCountryId(city.getCountryId());
            if(country.isPresent()) {
                scrunchCitiesV2.setCountry(country.get().getCountryName());
            }
            toSave.add(scrunchCitiesV2);
        });

        List<ScrunchCitiesV2> toReturn = scrunchCitiesV2Repository.saveAll(toSave);
        return toReturn;
    }

}
