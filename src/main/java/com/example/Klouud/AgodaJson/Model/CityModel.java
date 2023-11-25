package com.example.Klouud.AgodaJson.Model;

import lombok.Data;

@Data
public class CityModel {

    private String id;

    private Integer cityId;

    private String cityName;

    private Integer countryId;

    private String cityTranslated;

    private Integer activeHotels;

    private Double longitude;

    private Double latitude;

    private Integer noArea;
}
