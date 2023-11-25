package com.example.Klouud.AgodaJson.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancellationBookingResponseDTO {

    private CancellationSummary cancellationSummary;

    @Data
    public static class CancellationSummary{
        private Integer bookingId;
        private Integer reference;
        private List<CancellationPolicy> cancellationPolicy = new ArrayList<>();
        private List<PaymentRate> paymentRate = new ArrayList<>();
        private List<RefundRate> refundRate = new ArrayList<>();

        @Data
        private static class CancellationPolicy{
            private String language;
            private String policyText;
        }

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

    }
}
