package ge.darbuka.darbuka_academy.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.Purchase;
import ge.darbuka.darbuka_academy.domain.PurchaseStatus;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.PurchaseRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final PurchaseRepository purchases;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostConstruct
    void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Transactional
    public String createSession(User user, Course course) throws StripeException {

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCourse(course);
        purchase.setStatus(PurchaseStatus.PENDING);
        purchase.setAmountCents(course.getPriceCents());
        purchase.setCurrency(course.getCurrency());
        purchases.save(purchase);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/courses/" + course.getLevel())
                .setCustomerEmail(user.getEmail())
                .putMetadata("purchase_id", String.valueOf(purchase.getId()))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(course.getCurrency().toLowerCase())
                                                .setUnitAmount(course.getPriceCents().longValue())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(course.getTitle())
                                                                .setDescription(course.getDescription())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);

        purchase.setProviderSessionId(session.getId());
        purchases.save(purchase);

        return session.getUrl();
    }
}