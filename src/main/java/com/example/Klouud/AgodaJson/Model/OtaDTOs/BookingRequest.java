package com.example.Klouud.AgodaJson.Model.OtaDTOs;

import com.example.Klouud.AgodaJson.Enum.OTA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {


    private String token;


    private String key;


    private String agentId;


    private String panNumber;


   // private Tenant tenancyInfo;


    private String tripId;


    private OTA ota;


//    private BookingOperation bookingOperation;


    private String hotelId;


    private Integer noOfRooms;



    private LocalDate checkInDate;



    private LocalDate checkOutDate;


    private BigDecimal amount;


 //   private List<Room> rooms = new ArrayList<>();


    private String reasonForCancellation;


    private String currency = "INR";


    private String customerUserAgent;


    private String customerIpAddress;


    private String customerSessionId;


  //  private ExpediaRateType expediaRateType;


    private String supplierType;

   // private CustomerInformation customerInformation;

  //  private CardInformation cardInformation;

    //Please do not fill this value. The Post Booking Tool is the only one using it.
   // private Source source;


  //  private PriceType priceType = PriceType.WEB;

    //this field will be sent by Goomo to store data for booking which will be used later to display same in PBT .

    private List<String> cancellationPolicyDescription;

   // private CancelPolicyInfoList cancelPolicyInfoList;

   // private PaymentDetails paymentDetails;

   // private RoomDetails roomDetails;

    private String bookingSource;


    private String paymentSource;


 //   private StatusUpdateInfo statusUpdateInfo;


  //  private Price price;


 //   private TaxDetail taxDetail;


    private String promoCode;


    private boolean pesBooking;


   // private PaybackDetails paybackDetails;

    //required at refund process time
    private String bookingToken;

    //required for Agoda Confirm Booking
    private String searchId;

    //Unique id for Agoda's each room result
    private String lineItemId;

    //Agoda - Unique ID for the room
    private String blockId;

    private String userId;


  //  private ForceFailUpdateInfo forceFailUpdateInfo;


    private String forceFailBookingAmount;

    private String forceFailTripId;


    private String buyerKey;


    private String remarks;

    private Deal deal;


    private List<Integer> addOnIds;


    private String paymentStatus;

    private String bookingEmailId;


}
