package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.dto.TeamDto;
import fr.efreifive.manageplayer.dto.TeamRequest;
import fr.efreifive.manageplayer.service.TeamService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> findAll() {
        return ResponseEntity.ok(this.teamService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.teamService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TeamDto> create(@RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.teamService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> update(@PathVariable("id") UUID id, @RequestBody TeamRequest request) {
        return ResponseEntity.ok(this.teamService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        this.teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
