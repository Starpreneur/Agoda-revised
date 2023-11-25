package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestV2 {



    private String token;


    private String key;


    private String tag;


    private String hotelSessionId;


    private String roomSessionId;


    private String promoCode;


    private String buyerKey;


    private String remarks;

    private List<Integer> addOnIds;


    private String paymentStatus;

    private CustomerInformationV2 primaryGuestDetails;

    private List<CustomerInformationV2> otherGuestDetails = new ArrayList<>();

    private String ota;

    //For Zam(B2c Purpose)
    private String paymentType;
    private String loggedInUser;
    private String paymentTransactionId;
    private String agentId;
    private String transactionId;
    private String agentName;
    //If Price Overridden
    private BigDecimal overRiddenTotalAmount;

}
