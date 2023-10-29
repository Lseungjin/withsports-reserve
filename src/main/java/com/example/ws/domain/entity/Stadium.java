package com.example.ws.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.ws.exception.sportstype.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stadium")
@Getter
public class Stadium extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "stadium_id")
    private Long id;

    @Column(name = "stadium_name")
    private String stadiumName;

    // 양방향
    @OneToMany(mappedBy = "stadium",cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"stadium"})
    private List<AvailableDate> availableDates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
        admin.getStadiums().add(this);
    }

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    public void cancel() {
        this.totalQuantity++;
    }

    @Column(name = "date_accept")
    private Integer dateAccept;
    @Column(name = "time_accept")
    private Integer timeAccept;

    public void setTotalSportstypeQuantity(Integer qty) {
        this.totalQuantity = qty;
    }

    public void removeStock() {
        int restStock=this.totalQuantity-1;
        if(restStock==0){
            setEnabled(false);
        }
        if(restStock<0){
            throw new NotEnoughStockException("예약 가능한 수량이 부족합니다.");
        }

        this.totalQuantity=restStock;
    }

    public void updateDateAccept(Integer dateAccept){this.dateAccept=dateAccept;}

    public void updateTimeAccept(Integer timeAccept){this.timeAccept=timeAccept;}

    // true: y, false: n
    @Convert(converter = YesNoConverter.class)
    private Boolean enabled = true; // 예약 가능 여부

    @OneToMany(mappedBy = "stadium", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnoreProperties({"stadium"})
    private List<Sportstype> sportstypes = new ArrayList<>();

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    // 연관관계 편의 메서드
    private void addAdmin(Admin admin) {
        this.admin = admin;
        admin.getStadiums().add(this);
    }


    @Builder(builderMethodName = "createStadium")
    public Stadium(String stadiumName, String address, String detailAddress,Integer dateAccept,Integer timeAccept) {
        this.stadiumName = stadiumName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.createAt = LocalDateTime.now();
        this.dateAccept=dateAccept;
        this.timeAccept=timeAccept;
    }

}
