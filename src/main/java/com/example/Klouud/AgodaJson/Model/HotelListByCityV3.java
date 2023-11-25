package com.example.Klouud.AgodaJson.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelListByCityV3 {

    private String id;

    private Integer cityId;

    private String cityName;

    private Integer countryId;

    List<Integer> hotelList = new ArrayList<>();
}
