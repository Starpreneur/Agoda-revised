package com.example.Klouud.AgodaJson.Enitity;

import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Model.RoomAvailabilityRequestV2;
import com.example.Klouud.AgodaJson.Model.RoomAvailabilityResponseV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roomAvailabilitySessionDetailsV2")
public class RoomAvailabilitySessionDetails {

    @Id
    private String id;
    private OTA ota;
    private RoomAvailabilityRequestV2 clientRequest;
    private RoomAvailabilityResponseV2 clientResponse;
    private Object otaRequest;
    private Object otaResponse;
    private LocalDateTime createdDateTime;

}
