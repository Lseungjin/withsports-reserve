package com.example.ws.domain.entity;

import com.example.ws.exception.sportstype.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.type.YesNoConverter;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "available_time")
@Getter
public class AvailableTime {

    @Id
    @GeneratedValue
    @Column(name = "available_time_id")
    private Long id;

    @Column(nullable = false)
    private int time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "available_date_id")
    private AvailableDate availableDate;

    // 한 타임동안 수용 가능한 인원
    private Integer acceptCount;

    public void cancel() {
        this.acceptCount++;
        this.enabled=true;
        this.availableDate.cancel();
    }

    public void decreaseCount() {
        int restStock=this.acceptCount-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약이 가득찼습니다.");
        }
        this.availableDate.decreaseCount();
        this.acceptCount=restStock;
    }

    @Convert(converter = YesNoConverter.class)
    private Boolean enabled = true;

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    // 양방향 연관관계 편의 메서드
    public void addAvailableDate(AvailableDate availableDate) {
        this.availableDate = availableDate;
        availableDate.getAvailableTimes().add(this);
    }

    private void setAvailableTime(int time) {
        this.time = time;
    }

    @Builder(builderMethodName = "createAvailableTime")
    public AvailableTime(int time, Integer acceptCount) {
        this.time = time;
        this.acceptCount = acceptCount;
    }

    public void updateAcceptCount(Integer acceptCount){
        this.acceptCount=acceptCount;
    }
}
