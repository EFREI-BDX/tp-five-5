package org.efrei.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MatchEntity {
    @Id
    int id;
}