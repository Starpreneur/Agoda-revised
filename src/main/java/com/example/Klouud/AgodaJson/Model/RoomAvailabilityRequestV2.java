package com.example.Klouud.AgodaJson.Model;

import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.RoomBreakUp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomAvailabilityRequestV2 {

    private String token;
    private String key;
    private String city;
    private String country;
    private String currency;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer noOfRooms;
    private List<RoomBreakUp> rooms = new ArrayList<>();
    private OTA ota;
    private String hotelId;
    private String hotelName;
    private String buyerKey;
    private String nationality;
    private List<Integer> filterIds = new ArrayList<>();
    private String priceSort;
    private String starRating;
    private String source;
    private Boolean isAutoApply;
    private String sessionCode;
    private String agencyReference;

}
