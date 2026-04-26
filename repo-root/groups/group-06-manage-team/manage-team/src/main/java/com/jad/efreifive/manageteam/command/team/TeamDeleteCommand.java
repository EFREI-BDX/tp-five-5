package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.valueobject.Id;

public record TeamDeleteCommand(Id id) implements TeamCommand {
}
