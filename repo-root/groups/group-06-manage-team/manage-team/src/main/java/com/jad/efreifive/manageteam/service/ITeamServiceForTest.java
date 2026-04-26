package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Label;
import com.jad.efreifive.manageteam.valueobject.Period;
import com.jad.efreifive.manageteam.valueobject.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

public interface ITeamServiceForTest extends ITeamService {
    @Transactional
    Id create(Label label, Tag tag, Period period);

    @Transactional
    void changeName(UUID id, String newLabel);

    @Transactional
    void changeTag(UUID id, String newTag);

    @Transactional
    boolean dissolve(UUID id, LocalDate dissolutionDate);

    @Transactional
    void restore(UUID id);
}
