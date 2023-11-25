package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Tolerate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class RoomInformation {
    private String roomTypeId;//code
    private String roomType;
    private String description;//descriptions
    private String maxAdultOccupancy;
    private String maxChildOccupancy;
    private String maxInfantOccupancy;
    private String maxGuestOccupancy;
    private String roomTypeImagePath;
    private List<RoomAmenity> amenities;
    private String view;

    private String smokingPermitted;
    private String bedType;
    private String numberOfBedRooms;
    private String numberOfBathRooms;
    private String numberOfLivingRooms;

}
