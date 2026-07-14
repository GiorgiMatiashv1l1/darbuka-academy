package ge.darbuka.darbuka_academy.services;

import ge.darbuka.darbuka_academy.domain.Lesson;
import ge.darbuka.darbuka_academy.domain.PurchaseStatus;
import ge.darbuka.darbuka_academy.domain.Role;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final PurchaseRepository purchases;

    public boolean canAccess(User user, Lesson lesson){
        if(lesson.isFreePreview()) return true;
        if(user == null) return false;
        if(user.getRole() == Role.ADMIN) return true;
        return owns(user, lesson.getCourse().getId());
    }

    public boolean owns(User user, Long courseId){
        if(user == null) return false;
        if(user.getRole() == Role.ADMIN) return true;
        return purchases.existsByUserIdAndCourseIdAndStatus(
                user.getId(), courseId, PurchaseStatus.PAID);
    }
}
