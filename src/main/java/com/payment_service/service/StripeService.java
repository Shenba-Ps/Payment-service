package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeService {

    Logger logger = LoggerFactory.getLogger(StripeService.class);
    @Value("${app.default.success-url:http://localhost:3000/success}")
    private String defaultSuccessUrl;

    @Value("${app.default.cancel-url:http://localhost:3000/cancel}")
    private String defaultCancelUrl;

    public PaymentResponse createCheckoutSession(PaymentRequest request) {
        try {

            // Line item with price only
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setUnitAmount(request.getAmount())
                            .setCurrency(
                                    request.getCurrency() != null
                                            ? request.getCurrency()
                                            : "usd"
                            )
                            .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(request.getDescription() != null
                                                    ? request.getDescription()
                                                    : "Payment for " + request.getReferenceId())
                                            .build()
                            )

                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build();

            // Session creation
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(
                                    request.getSuccessUrl() != null
                                            ? request.getSuccessUrl()
                                            : defaultSuccessUrl
                            )
                            .setCancelUrl(
                                    request.getCancelUrl() != null
                                            ? request.getCancelUrl()
                                            : defaultCancelUrl
                            )
                            .addLineItem(lineItem)
                            .putAllMetadata(Map.of(
                                    "referenceId", request.getReferenceId(),
                                    "serviceName", request.getServiceName()
                            ))
                            .build();

            Session session = Session.create(params);

            String paymentIntentId = session.getPaymentIntent();
            logger.info("paymentIntentId: " + paymentIntentId);
            return PaymentResponse.builder()
                    .status("SUCCESS")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .paymentId(paymentIntentId)
                    .message("Checkout session created")
                    .build();

        } catch (StripeException e) {
            return PaymentResponse.builder()
                    .status("FAILURE")
                    .message(e.getMessage())
                    .build();
        }
    }
}
