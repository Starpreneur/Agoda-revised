package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseV2 {

    private String tag;
    private String bookingReferenceId;
    private String bookingStatus;
    private String otaBookingReferenceId;

}
