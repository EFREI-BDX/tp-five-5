package com.jad.efreifive.manageteam.admin;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.service.ITeamAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teams")
public class TeamAdminController {

    private final ITeamAdminService teamService;

    @Autowired
    public TeamAdminController(ITeamAdminService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/massive-create-players")
    public ResponseEntity<Void> massiveCreatePlayers() {
        this.teamService.massiveCreatePlayers();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/massive-update-players")
    public ResponseEntity<Void> massiveUpdatePlayers() {
        this.teamService.massiveUpdatePlayers();
        return ResponseEntity.ok().build();
    }

}
