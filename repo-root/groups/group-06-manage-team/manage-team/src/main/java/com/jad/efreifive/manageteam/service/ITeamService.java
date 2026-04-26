package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.controller.command.TeamCommand;
import com.jad.efreifive.manageteam.controller.command.TeamCommandResult;
import com.jad.efreifive.manageteam.dto.TeamDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ITeamService {
    @Transactional(readOnly = true)
    List<TeamDto> findAll();

    @Transactional(readOnly = true)
    TeamDto findById(UUID id);

    @Transactional
    TeamCommandResult executeCommand(TeamCommand command);
}
