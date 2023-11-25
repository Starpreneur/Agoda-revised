package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Enum.OTA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    private LinkedHashMap<String, HotelDetails> hotelDetails;
    private String message;
    private OTA ota;
    private List<Integer> propertyIds = new ArrayList<>();

 }
