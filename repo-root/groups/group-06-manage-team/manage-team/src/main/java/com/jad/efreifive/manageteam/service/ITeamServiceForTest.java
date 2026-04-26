package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Label;
import com.jad.efreifive.manageteam.valueobject.Period;
import com.jad.efreifive.manageteam.valueobject.Tag;
import org.springframework.transaction.annotation.Transactional;

public interface ITeamServiceForTest extends ITeamService {
    @Transactional
    Id create(final Label label, final Tag tag, final Period period);

    @Transactional
    void changeName(final Id id, final Label newLabel);

    @Transactional
    void changeTag(final Id id, final Tag newTag);

    @Transactional
    boolean dissolve(final Id id, final Period dissolutionDate);

    @Transactional
    void restore(final Id id);
}
