package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "AgodaCityHotelIdsCache")
public class CityHotelIdsCache {

    //it will be a combination of uuid and cityName
    @Id
    String id;
    List<PropertyIds> listOfpropertyIdsList = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyIds {

        Integer pageNumber;

        List<Integer> propertyIdsList = new ArrayList<>();


    }


}


