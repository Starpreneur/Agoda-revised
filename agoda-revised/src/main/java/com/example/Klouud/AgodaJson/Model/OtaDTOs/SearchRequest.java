package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchRequest {


    private String token;


    private String key;


    private String city;


    private String currency = "INR";


    private String nationality;



    private LocalDate checkInDate;



    private LocalDate checkOutDate;


    private Integer noOfRooms;


    private List<RoomBreakUp> rooms = new ArrayList<>();


    private String country;


    private String searchId;


    private String latitude;


    private String longitude;

    // The default countryCode is India

    private String countryCode = "IN";

    //Id of AgodaCityHotelIdsCache generated at the time of fresh hotel search
    private String cacheId;

    //Page number to fetch list of hotels for that particular page saved in cached city
    private Integer pageNumber;

    private List<Integer> propertyIdsList = new ArrayList<>();


    private String state;


    private String customerUserAgent;


    private String customerIpAddress;


    private String customerSessionId;


    //private ExpediaRateType expediaRateType;


    //private OTA ota;


    private String hotelId;


    private String hotelName;


    //private PriceType priceType = PriceType.WEB;


    private Boolean isForceFail = false;


    private String buyerKey;


    private List<String> rateKeys = new ArrayList<>();

}
