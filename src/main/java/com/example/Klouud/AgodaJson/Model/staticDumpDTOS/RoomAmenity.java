package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.AmenityType;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class RoomAmenity {

    private String id;
    private String name;
    private String description;
    private String category;
    private String roomId;
    private AmenityType type;

    //Below attributes added based on HOTEL_BEDS RESPONSE
    //is Active Or Not
    private Boolean isAvailable;
    //Is Extra cost for facility or free
    private Boolean isFeeApplicable;
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
