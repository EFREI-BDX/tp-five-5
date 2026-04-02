package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.mapper.TeamMapper;
import com.jad.efreifive.manageteam.repository.TeamRepository;
import com.jad.efreifive.manageteam.repository.result.OperationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findAll() {
        return this.teamRepository.findAll().stream().map(this.teamMapper::entityToDto).toList();
    }

    @Transactional(readOnly = true)
    public TeamDto findById(UUID id) {
        return this.teamRepository.findById(id.toString())
                .map(this.teamMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));
    }

    @Transactional
    public void create(UUID id, String label, String tag, LocalDate creationDate) {
        this.assertSuccess(this.teamRepository.create(id.toString(), label, tag, creationDate), "Team create failed");
    }

    @Transactional
    public void dissolve(UUID id, LocalDate dissolutionDate) {
        this.assertSuccess(this.teamRepository.dissolve(id.toString(), dissolutionDate), "Team dissolve failed");
    }

    @Transactional
    public void restore(UUID id) {
        this.assertSuccess(this.teamRepository.restore(id.toString()), "Team restore failed");
    }

    @Transactional
    public void changeName(UUID id, String newLabel) {
        this.assertSuccess(this.teamRepository.changeName(id.toString(), newLabel), "Team rename failed");
    }

    @Transactional
    public void changeTag(UUID id, String newTag) {
        this.assertSuccess(this.teamRepository.changeTag(id.toString(), newTag), "Team change tag failed");
    }

    private void assertSuccess(OperationResult result, String defaultMessage) {
        if (!result.success()) {
            throw new ServiceOperationException(result.message() == null || result.message().isBlank() ? defaultMessage : result.message());
        }
    }
}

