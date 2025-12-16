package com.payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
