package ge.darbuka.darbuka_academy.services;

import ge.darbuka.darbuka_academy.domain.Purchase;
import ge.darbuka.darbuka_academy.domain.PurchaseStatus;
import ge.darbuka.darbuka_academy.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchases;

    @Transactional
    public void markPaid(String providerSessionId) {
        Purchase purchase = purchases.findByProviderSessionId(providerSessionId).orElse(null);

        System.out.println("LOOKUP " + providerSessionId + " -> "
                + (purchase == null ? "NOT FOUND" : "id " + purchase.getId()));

        if (purchase == null) {
            return;
        }
        if (purchase.getStatus() == PurchaseStatus.PAID) {
            System.out.println("ALREADY PAID, skipping");
            return;
        }

        purchase.setStatus(PurchaseStatus.PAID);
        purchase.setPaidAt(Instant.now());
        purchases.save(purchase);
        System.out.println("SET PAID id " + purchase.getId());
    }
}