package com.jad.efreifive.managefield.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_active_fields")
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveFieldViewRecord {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status_id", nullable = false, length = 36)
    private String statusId;
}
