package com.example.ws.repository.custom;

import com.example.ws.domain.entity.Sportstype;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@RequiredArgsConstructor
@Repository
public class SportsTypeCustomRepositoryImpl implements SportsTypeCustomRepository{

    private final EntityManager em;

    @Override
    public Sportstype findSportstype(Long stadiumId, String sportstypeName) {
        return em.createQuery(
                        "select v " +
                                "from Sportstype v  join v.stadium h " +
                                "where h.id = :stadiumId and v.sportstypeName = :sportstypeName and v.enabled = true", Sportstype.class
                )
                .setParameter("stadiumId", stadiumId)
                .setParameter("sportstypeName", sportstypeName)
                .getSingleResult();
    }

    @Override
    public Sportstype findSportstypeDisabled(Long stadiumId, String sportstypeName) {
        return em.createQuery(
                        "select v " +
                                "from Sportstype v  join v.stadium h " +
                                "where h.id = :stadiumId and v.sportstypeName = :sportstypeName", Sportstype.class
                )
                .setParameter("stadiumId", stadiumId)
                .setParameter("sportstypeName", sportstypeName)
                .getSingleResult();
    }
}
