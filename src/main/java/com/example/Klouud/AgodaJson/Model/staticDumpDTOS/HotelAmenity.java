package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.AmenityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelAmenity {

    private String name;
    private String description;
    private String category;
    private AmenityType amenityType;
    private Boolean isAvailable;
    //Below attributes added based on HOTEL_BEDS RESPONSE
    //is Active Or Not
    //Is Extra cost for facility or free
    private Boolean isFeeApplicable;
    //Distance to the facility
    private Integer distance;
    //facility can be used in the age  group of
    private Integer ageFrom;
    private Integer ageTo;
    //facility applicable between dates
    private Date dateFrom;
    private Date dateTo;
    //facility timimg
    private String timeFrom;
    private String timeTo;
    //is facility mandatory
    private Boolean isMandatory;
    //charge For Facility
    private BigDecimal amount;
    //currency OF the fee
    private String currency;
    //facility Rate Type
    private String applicationType;
    //PN – Per Person & Night
    //PS – Per Person & Stay
    //UH – Per Unit & Hour
    //UN – Per Unit & Night
    //US – Per Unit & Stay

}
