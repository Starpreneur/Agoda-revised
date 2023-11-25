package com.example.Klouud.AgodaJson.Enitity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Negative;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgodaCountries {

    private CountryFeed countryFeed;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CountryFeed{

        private Countries countries;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Countries{

             List<Country> country = new ArrayList<>();

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Country{
                private Integer countryId;
                private Integer continentId;
                private String countryName;
                private String countryTranslated;
                private String activeHotels;
                private String countryIso;
                private String countryIso2;
                private Double longitude;
                private Double latitude;
            }
        }
    }

}
