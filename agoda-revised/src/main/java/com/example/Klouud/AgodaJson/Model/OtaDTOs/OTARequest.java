package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Model.BookingRequestV2;
import com.example.Klouud.AgodaJson.Model.RoomAvailabilityRequestV2;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTARequest {

    private SearchRequest searchRequest;
    private OTADetail otaDetail;
    private CityMapping cityMapping;
    private BookingRequest bookingRequest;
    private RoomAvailabilityRequestV2 roomAvailabilityRequestV2;
    private BookingRequestV2 bookingRequestV2;

}
