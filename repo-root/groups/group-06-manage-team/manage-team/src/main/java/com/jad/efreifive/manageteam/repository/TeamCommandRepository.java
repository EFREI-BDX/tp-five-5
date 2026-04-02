package com.jad.efreifive.manageteam.repository;

import java.time.LocalDate;

public interface TeamCommandRepository {
    void create(String id, String label, String tag, LocalDate creationDate);
    void dissolve(String id, LocalDate dissolutionDate);
    void restore(String id);
    void changeName(String id, String newLabel);
    void changeTag(String id, String newTag);
}

