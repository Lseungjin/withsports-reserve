package com.example.ws.domain.entity;

import com.example.ws.domain.value.ReserveStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * USER의 예약서
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reserve_item")
@Getter
public class ReserveItem extends BaseEntity {

    @Column(name = "reserve_item_id")
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @Enumerated(value = EnumType.STRING)
    private ReserveStatus status = ReserveStatus.COMP;

    @Column(nullable = false)
    private String sportstypeName;
    @Column(nullable = false)
    private String reserveDate;
    @Column(nullable = false)
    private int reserveTime;

    @Builder(builderMethodName = "createReserveItem")
    public ReserveItem(
            User user, Stadium stadium, ReserveStatus status, String reserveDate, int reserveTime, String sportstypeName) {
        this.user = user;
        this.stadium = stadium;
        this.status = status;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.sportstypeName = sportstypeName;
        this.createAt = LocalDateTime.now();
    }

    //==비즈니스 로직==//
    //예약 날짜 및 예약 시간 update
    public void updateDateAndTime(String reserveDate,int reserveTime){
        this.reserveDate=reserveDate;
        this.reserveTime=reserveTime;
    }
}
