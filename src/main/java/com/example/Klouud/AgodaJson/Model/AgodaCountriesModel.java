package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgodaCountriesModel {

    private String id;
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
