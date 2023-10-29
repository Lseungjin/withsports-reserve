package com.example.ws.repository.custom;

import com.example.ws.domain.entity.AvailableDate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class AvailableDateCustomRepositoryImpl implements AvailableDateCustomRepository{

    private final EntityManager em;

    @Override
    public AvailableDate findAvailableDateByStadiumIdAndDate(Long stadiumId, String date) {
        return em.createQuery(
                        "select d " +
                                "from AvailableDate d " +
                                "where d.stadium.id = :stadiumId and d.date = :date", AvailableDate.class
                )
                .setParameter("stadiumId", stadiumId)
                .setParameter("date", date)
                .getSingleResult();

    }
}