package com.jad.efreifive.manageteam.aggregate;

import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Name;

public record PlayerAggregate(
        Id id,
        Name displayName,
        Id idTeam
) {
}