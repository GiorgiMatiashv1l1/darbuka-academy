package ge.darbuka.darbuka_academy.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import ge.darbuka.darbuka_academy.services.PurchaseService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final PurchaseService purchaseService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/webhooks/stripe")
    public ResponseEntity<String> handle(@RequestBody String payload,
                                         @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("WEBHOOK SIG FAILED: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        System.out.println("EVENT RECEIVED: " + event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            com.google.gson.JsonObject root =
                    com.google.gson.JsonParser.parseString(payload).getAsJsonObject();
            String sessionId = root.getAsJsonObject("data")
                    .getAsJsonObject("object")
                    .get("id").getAsString();

            System.out.println("MARKING PAID, session id = " + sessionId);
            purchaseService.markPaid(sessionId);
        }

        return ResponseEntity.ok("ok");
    }

    @PostConstruct
    void check() {
        System.out.println("USING WEBHOOK SECRET " + webhookSecret);
    }
}