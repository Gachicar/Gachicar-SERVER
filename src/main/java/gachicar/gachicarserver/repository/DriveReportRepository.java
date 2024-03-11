package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.DriveReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class DriveReportRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(DriveReport driveReport) {
        em.persist(driveReport);
    }

    // 사용자의 가장 최근 주행 리포트 조회
    public DriveReport findRecentByUser(Long userId) {
        try {
            String jpql = "SELECT dr FROM DriveReport dr " +
                    "WHERE dr.user.id = :userId " +
                    "ORDER BY dr.startTime DESC";

            return em.createQuery(jpql, DriveReport.class)
                    .setParameter("userId", userId)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
