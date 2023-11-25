package com.example.Klouud.AgodaJson.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPropertyReq {

    private Integer waitTime;
    private BookingDetails bookingDetails;
    private CustomerDetail customerDetail;
    private PaymentDetails paymentDetails;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BookingDetails{

        private String userCountry;
        private Long searchId;
        private String tag;
        private boolean allowDuplication;
        private String checkIn;
        private String checkOut;
        private String language;
        private Property property;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Property{

            private Integer propertyId;
            private List<Rooms> rooms;

            @Data
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class Rooms{

                private String blockId;
                private Rate rate;
                private List<Surcharges> surcharges;
                private List<GuestDetails> guestDetails;

                @JsonInclude(JsonInclude.Include.NON_NULL)
                private List<Integer> childrenAges;

                private String specialRequest;
                private String currency;
                private String paymentModel;
                //private String bookNowPayLaterDate;
                private Integer count;
                private Integer adults;
                private Integer children;

                @Data
                public static class Rate{
                    private Double inclusive;
                }

                @Data
                public static class Surcharges{

                    private Long id;
                    private Rate rate;

                    @Data
                    public static class Rate{
                       private Double inclusive;
                       private Double exclusive;
                       private Double tax;
                       private Double fees;
                       private String margin;
                    }
                }

                @Data
                public static class GuestDetails{

                    private String title;
                    private String firstName;
                    private String lastName;
                    private String countryOfResidence;
                    private String gender;
                    private Integer age;
                    private Boolean isChild;
                    private Boolean primary;
                }

            }
        }
    }

    @Data
    public static class CustomerDetail{

       private String language;
       private String title;
       private String firstName;
       private String lastName;
       private String email;
       private boolean newsletter;
       private Phone phone;
        @Data
        public static class Phone {
            private String countryCode;
            private String areaCode;
            private String number;
        }
    }

    @Data
    public static class PaymentDetails{

        private CreditCardInfo creditCardInfo;

        @Data
        public static class CreditCardInfo{

            private String cardType;
            private String number;
            private String expiryDate;
            private String cvc;
            private String holderName;
            private String countryOfIssue;
            private String issuingBank;
        }
    }
}
