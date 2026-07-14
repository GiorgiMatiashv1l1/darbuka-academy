package ge.darbuka.darbuka_academy.repository;

import ge.darbuka.darbuka_academy.domain.Purchase;
import ge.darbuka.darbuka_academy.domain.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    boolean existsByUserIdAndCourseIdAndStatus(Long userId, Long courseId, PurchaseStatus status);
    Optional<Purchase> findByProviderSessionId(String sessionId);
    List<Purchase> findAllByUserIdAndStatus(Long userId, PurchaseStatus status);
}
