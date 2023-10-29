package com.example.ws.domain.entity;

import com.example.ws.exception.sportstype.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.type.YesNoConverter;


import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sportstype")
@Getter
public class Sportstype extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "sportstype_id")
    private Long id;

    @Column(name = "sportstype_name", nullable = false)
    private String sportstypeName;

    @Column(nullable = false)
    private Integer quantity;

    public void cancel() {
        this.quantity++;
        this.enabled=true;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @Convert(converter = YesNoConverter.class)
    private boolean enabled = true;

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Builder(builderMethodName = "createSportsType")
    public Sportstype(String sportstypeName, Integer quantity) {
        this.sportstypeName = sportstypeName;
        this.quantity = quantity;

        this.createAt = LocalDateTime.now();
    }
    // 연관관계 편의 메서드
    public void addStadium(Stadium stadium) {
        this.stadium = stadium;
        stadium.getSportstypes().add(this);
    }

    //==비즈니스 로직==//

    //예약 취소 시, 사용
    public void addStock(){
        this.quantity+=1;
    }

    //예약 시, 사용
    public void removeStock(){
        int restStock=this.quantity-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약 가능한 수량이 부족합니다.");
        }

        this.quantity=restStock;
    }

    //경기장 수정 시, 사용
    public void updateSportsTypeQty(Integer quantity){
        this.quantity=quantity;
    }
}
