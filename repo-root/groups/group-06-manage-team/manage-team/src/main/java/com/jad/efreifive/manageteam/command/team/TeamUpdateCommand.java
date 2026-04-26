package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.dto.TeamDto;

public record TeamUpdateCommand(TeamDto teamDto) implements TeamCommand {
}
