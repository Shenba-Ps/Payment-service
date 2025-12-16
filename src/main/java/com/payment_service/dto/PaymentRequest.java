package com.payment_service.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long amount;          // smallest currency unit
    private String currency;      // usd, inr, etc.
    private String referenceId;   // Order ID / Booking ID
    private String serviceName;   // Calling service name
    private String description;   // Optional description
    private String successUrl;    // Optional redirect on success
    private String cancelUrl;
}
