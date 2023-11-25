package com.example.Klouud.AgodaJson.Utils;

import com.example.Klouud.AgodaJson.Enitity.AgodaCountriesV4;
import com.example.Klouud.AgodaJson.Enitity.Cities;
import com.example.Klouud.AgodaJson.Enitity.StaticDumpHotel;
import com.example.Klouud.AgodaJson.Repository.AgodaCountryV4Repository;
import com.example.Klouud.AgodaJson.Repository.CitiesRepository;
import com.example.Klouud.AgodaJson.Repository.NewAgodaStaticDumpHotelRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Component
public class StaticDataUtils {

    @Autowired
    private  CitiesRepository citiesRepository;

    @Autowired
    private AgodaCountryV4Repository agodaCountryV4Repository;

    @Autowired
    private NewAgodaStaticDumpHotelRepository staticDumpHotelRepository;

    //Method to fetch Agoda cities List and store in a map
    public  HashMap<Integer,String> cityMap(){
        List<Cities> citiesList = citiesRepository.findAll();
        Map<Integer,String> citiesMap = citiesList.stream()
                .collect(Collectors.toMap(Cities::getCityId,Cities::getCityName));

        return (HashMap<Integer, String>) citiesMap;
    }

    public HashMap<Integer,String> countryMap(){
        List<AgodaCountriesV4> countriesV4List = agodaCountryV4Repository.findAll();
        Map<Integer,String> countriesMap = countriesV4List.stream().collect(Collectors.toMap(AgodaCountriesV4::getCountryId,AgodaCountriesV4::getCountryName));
        return (HashMap<Integer, String>) countriesMap;
    }

    public String countryCode(String countryOfBookingReq){
        List<AgodaCountriesV4> countriesList = agodaCountryV4Repository.findAll();
      String countryCode = countriesList.stream().filter(country -> country.getCountryName()
              .equalsIgnoreCase(countryOfBookingReq)).map(AgodaCountriesV4::getCountryIso2).findFirst().get().toString();

      return countryCode;
    }

    public String buildStaticDumpId(Integer hotelId){
        StringBuilder builder = new StringBuilder();
        builder.append(hotelId.toString()).append("_").append("AGODA");
        String staticDumpId = builder.toString();
        return staticDumpId;
    }

    public HashMap<String,String> buildStaticDumpMap(){
        List<StaticDumpHotel> staticDumpHotelList = staticDumpHotelRepository.findAllProjected();
        HashMap<String,String> map = new HashMap<>();
        for(StaticDumpHotel hotel : staticDumpHotelList){
            if(hotel != null && hotel.getInformation() !=null && hotel.getInformation().getName() != null){
                map.put(hotel.getId(),hotel.getInformation().getName());
            }
        }
        return map;
    }
}
