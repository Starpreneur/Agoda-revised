package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "BookingCancellationDetailsAgoda")
public class CancellationBookingResponseDetailsFromAgoda {
    @Id
    private String id;
    private CancellationBookingResponseDetailsFromAgoda.CancellationSummary cancellationSummary;

    @Data
    public static class CancellationSummary{

        private Integer bookingId;
        private Integer reference;
        private List<CancellationBookingResponseDetailsFromAgoda.CancellationSummary.CancellationPolicy> cancellationPolicy = new ArrayList<>();
        private List<CancellationBookingResponseDetailsFromAgoda.CancellationSummary.PaymentRate> paymentRate = new ArrayList<>();
        private List<CancellationBookingResponseDetailsFromAgoda.CancellationSummary.RefundRate> refundRate = new ArrayList<>();

        @Data
        public static class PaymentRate{
            private String currency;
            private Double inclusive;
            private Double exclusive;
            private Double tax;
            private Double fees;
            private Double taxDueSupplier;
            private String 	method;
            private String 	localCurrency;
            private Double localCurrencyAmount;
            private Double agxReferenceCommission;
            private Double agpReferenceCommission;
        }

        @Data
        public static class RefundRate{
            private String currency;
            private Double inclusive;
            private Double exclusive;
            private Double tax;
            private Double fees;
            private Double taxDueSupplier;
            private String 	method;
            private String 	localCurrency;
            private Double localCurrencyAmount;
            private Double agxReferenceCommission;
            private Double agpReferenceCommission;

        }

        @Data
        private static class CancellationPolicy{
            private String language;
            private String policyText;
        }

    }

}
