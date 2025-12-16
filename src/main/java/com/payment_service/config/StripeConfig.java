package com.payment_service.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
Logger logger = LoggerFactory.getLogger(StripeConfig.class);
    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        boolean isRender = System.getenv("RENDER") != null;

        logger.info("hostname:{}",isRender);
        logger.info("Stripe Secret Key: " + secretKey);
        // Set Stripe API key once during service startup
        Stripe.apiKey = secretKey;
    }
}
