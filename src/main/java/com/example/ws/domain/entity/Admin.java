package com.example.ws.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "admin")
@Getter
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"admin"})
    private List<Stadium> stadiums = new ArrayList<>();

    @Builder(builderMethodName = "createAdmin")
    public Admin(String name) {
        this.name = name;
    }
}
