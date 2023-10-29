package com.example.ws.repository.custom;

import com.example.ws.domain.entity.AvailableDate;
import com.example.ws.domain.entity.AvailableTime;
import com.example.ws.domain.entity.ReserveItem;
import com.example.ws.domain.entity.Sportstype;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReserveItemCustomRepositoryImpl implements ReserveItemCustomRepository {

    private final EntityManager em;

    @Override
    public List<AvailableDate> findAvailableDatesByStadiumId(Long id) {
        return em.createQuery(
                        "select d " +
                                "from AvailableDate d " +
                                "where d.stadium.id = :id and d.enabled = true", AvailableDate.class
                )
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<AvailableTime> findAvailableTimesByAvailableDateId(Long id) {
        return em.createQuery(
                        "select t " +
                                "from AvailableTime  t " +
                                "where t.availableDate.id = :id and t.enabled = true" , AvailableTime.class
                )
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Sportstype> findAvailableSportstypes(Long stadiumId) {
        return em.createQuery(
                        "select v " +
                                "from Sportstype v " +
                                "where v.stadium.id = :stadiumId and v.quantity > 0 and v.enabled = true", Sportstype.class
                )
                .setParameter("stadiumId", stadiumId)
                .getResultList();
    }

    @Override
    public List<ReserveItem> findAllReserveItem(Long stadiumId){
        return em.createQuery(
                        "select distinct ri " +
                                "from ReserveItem ri " +
                                "join fetch ri.user u " +
                                "where ri.stadium.id = :stadiumId"
                        ,ReserveItem.class)
                .setParameter("stadiumId",stadiumId)
                .getResultList();
    }
}
