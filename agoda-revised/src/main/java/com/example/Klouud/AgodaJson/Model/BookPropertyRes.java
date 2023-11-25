package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPropertyRes {

    private String status;
    private List<BookingDetails> bookingDetails;
    private PaymentResult paymentResult;

    @Data
    public static class BookingDetails {

        private String id;
        private String itineraryID;
        private String selfService;
        private boolean processing;
        private ErrorMessage errorMessage;

        @Data
        public static class ErrorMessage {
            private String id;
            private String subId;
            private String message;

        }
    }

    @Data
    public static class PaymentResult{

        private String referenceId;
        private Continuation continuation;

        @Data
        public static class Continuation{

            private Integer gatewayId;
            private Integer gatewayInfoId;
            private String transactionId;
            private String redirectPaymentToken;
            private String paymentTokenValue;
            private PaymentToken paymentToken;
            private Payment3DSResponse payment3DSResponse;

            @Data
            public static class PaymentToken{
                private Integer tokenType;
                private String tokenValue;
                private AdditionalInfo additionalInfo;

                @Data
                public static class AdditionalInfo{
                   private String key;
                   private String value;
                }
            }

            @Data
            public static class Payment3DSResponse{
                private String issueUrl;
                private List<String> require3DFields;
                private String returnUrlField;
                private String referenceToken;
                private String internalToken;
                private Post3DFields post3DFields;

                @Data
                public static class Post3DFields{
                    private String key;
                    private String value;
                }
            }
        }
    }

}
