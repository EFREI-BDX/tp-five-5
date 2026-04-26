package com.jad.efreifive.manageteam.aggregate;

import com.jad.efreifive.manageteam.valueobject.*;

public record TeamAggregate(
        Id id,
        Label label,
        Tag tag,
        Period period,
        Id idTeamLeader,
        TeamState state
) {
}