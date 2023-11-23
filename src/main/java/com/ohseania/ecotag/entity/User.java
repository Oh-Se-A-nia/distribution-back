package com.ohseania.ecotag.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile")
    private Photo photo;

    @ColumnDefault("0")
    private Long cumulativeAction;

}
