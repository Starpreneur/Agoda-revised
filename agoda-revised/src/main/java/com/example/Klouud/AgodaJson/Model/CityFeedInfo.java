package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityFeedInfo {

    private CityFeed cityFeed;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityFeed {


        private Cities cities;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Cities {
            List<City> city = new ArrayList<>();

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class City {

                private Integer cityId;

                private String cityName;

                private Integer countryId;

                private String cityTranslated;

                private Integer activeHotels;

                private Double longitude;

                private Double latitude;

                private Integer noArea;
            }
        }
    }
}
