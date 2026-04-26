package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.valueobject.Id;

public record TeamUndeleteCommand(Id id) implements TeamCommand {
}
