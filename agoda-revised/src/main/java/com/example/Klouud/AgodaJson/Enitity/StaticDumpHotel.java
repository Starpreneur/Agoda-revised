package com.example.Klouud.AgodaJson.Enitity;

import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Model.staticDumpDTOS.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(exclude = "updateDate")
@Document(collection = "newAgodaStaticDumpHotelV4")
public class StaticDumpHotel {

    @Id
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
