package com.example.Klouud.AgodaJson.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.util.List;

@Data
public class PropertyAvailabilityReq {

    private Integer waitTime;
    private Criteria criteria;
    private Features features;

    @Data
    public static class Criteria {

        private List<Integer> propertyIds;
        private String checkIn;
        private String checkOut;
        private String language;
        private Integer rooms;
        private Integer adults;
        private Integer children;
        private List<Integer> childrenAges;
        private String currency;
        //UserCountry is not mandatory in search,if Nationality comes null from Encompass it will be ignored in search request
        @JsonIgnore
        private String userCountry;
        @JsonIgnore
        private String ratePlan;
        @JsonIgnore
        private String platform;

    }

    @Data
    public static class Features{


        private Integer ratesPerProperty;
        private List<String> extra;

    }
}
