package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.OTA;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class NewAgodaStaticDumpHotelDTO {

    private String id;
    private OTA ota;
    private Information information;
    private List<HotelAmenity> hotelAmenities = new ArrayList<>();
    private List<RoomAmenity> roomAmenities;
    private List<RoomInformation> roomInformations = new ArrayList<>();
    private List<PointOfInterest> pointsOfInterest = null;
    private List<String> hotelPolicies = null;
    private List<Image> images = null;
    private Instant updateDate;
    private String renovationDetails;
    private ChildPolicy childPolicy;

}
