package com.example.Klouud.AgodaJson.Controller;

import com.example.Klouud.AgodaJson.Model.BookingResponseV2;
import com.example.Klouud.AgodaJson.Model.CancellationBookingResponseDTO;
import com.example.Klouud.AgodaJson.Model.CancellationRequestV4;
import com.example.Klouud.AgodaJson.Model.OtaDTOs.OTARequest;
import com.example.Klouud.AgodaJson.Model.OtaRequest;
import com.example.Klouud.AgodaJson.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/createAgodaBooking")
    public BookingResponseV2 createAgodaBooking(@RequestBody OTARequest otaRequest) throws Exception {
        BookingResponseV2 bookingResponseV2 = bookingService.agodaBookingService(otaRequest);
        return bookingResponseV2;
    }

    @PostMapping("/cancelAgodaBooking")
    public ResponseEntity cancelBooking(@RequestBody CancellationRequestV4 cancellationRequestV4) throws IOException {
        CancellationBookingResponseDTO cancellationBookingResponseDTO = bookingService.cancelBookingService(cancellationRequestV4);
        return new ResponseEntity(cancellationBookingResponseDTO, HttpStatus.OK);
    }


}
