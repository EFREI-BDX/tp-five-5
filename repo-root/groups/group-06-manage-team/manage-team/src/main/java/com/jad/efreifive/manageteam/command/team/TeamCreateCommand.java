package com.jad.efreifive.manageteam.command.team;

import com.jad.efreifive.manageteam.dto.TeamDto;

public record TeamCreateCommand(TeamDto teamDto) implements TeamCommand {
}
