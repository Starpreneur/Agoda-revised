package com.example.Klouud.AgodaJson.Enitity;

import com.example.Klouud.AgodaJson.Enum.OTA;
import com.example.Klouud.AgodaJson.Model.BookingRequestV2;
import com.example.Klouud.AgodaJson.Model.BookingResponseV2;
import com.example.Klouud.AgodaJson.Model.CancelBookingRequestV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookingV2")
public class BookingV2 {

    @Id
    private String id;
    private OTA ota;
    private String tag;
    private CreateBookingDetails createBooking=new CreateBookingDetails();
    private CancelBookingDetails cancelBooking=new CancelBookingDetails();
    private String bookingStatus;
    private LocalDateTime createdDateTime;
    private LocalDateTime cancelledDateTime;
    private String roomAvailabilityArchivesId;
    private String cancelBookingArchivesId;
    private String loggedInUser;
    private String source;
    private String supplierConfirmationId;
    private String supplierConfirmationNote;
    private String transactionId;
    //Booking assigned to User in triplaze
    private String userLoginId;
    private String userName;
    private String paymentGatewayURL;

    @Data
    public static class CreateBookingDetails {
        private BookingRequestV2 clientRequest;
        private BookingResponseV2 clientResponse;
        private String otaRequestXML;
        private String otaResponseXML;
        private Object otaRequest;
        private Object otaResponse;
    }

    @Data
    public static class CancelBookingDetails {
        private CancelBookingRequestV2 clientRequest;
        private BookingResponseV2 clientResponse;
        private String otaRequestXML;
        private String otaResponseXML;
        private Object otaRequest;
        private Object otaResponse;
    }
}
