package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.Group;
import gachicar.gachicarserver.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * 공유차량 관련 레포지토리
 */
@Service
@RequiredArgsConstructor
public class CarRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Car car) {
        em.persist(car);
    }

    public Car findById(Long carId) {
        return em.find(Car.class, carId);
    }

    public Car findByGroupId(Long groupId) {
        return em.find(Car.class, groupId);
    }

    public void delete(Car car) {
        em.remove(car);
    }

    public Car findByNumber(String carNum) {
        try {
            return em.createQuery("select c from Car c where c.carNumber = :carNum", Car.class)
                .setParameter("carNum", carNum)
                .getSingleResult();
        } catch (NoResultException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

}
