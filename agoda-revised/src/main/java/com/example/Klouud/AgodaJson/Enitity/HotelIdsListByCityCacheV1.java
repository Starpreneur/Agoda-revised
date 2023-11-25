package com.example.Klouud.AgodaJson.Enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "HotelIdsListByCityCacheV1")
public class HotelIdsListByCityCacheV1 {

    private String id;
    private Integer startCityId;
    private Integer endCityId;
    private Integer totalNumberOfCitiesSentInRequest;
    private List<CitiesSavedInSystem> citiesSavedInSystemList = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CitiesSavedInSystem {
        private Integer batchNumber;
        private List<Integer> cityIdsSavedList = new ArrayList<>();
    }

}
