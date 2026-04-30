package fr.efreifive.manageplayer.admin;

import fr.efreifive.manageplayer.config.AdminProperties;
import fr.efreifive.manageplayer.service.IPlayerAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/players")
public class PlayerAdminController {
    private final IPlayerAdminService playerAdminService;
    private final AdminProperties adminProperties;

    public PlayerAdminController(IPlayerAdminService playerAdminService, AdminProperties adminProperties) {
        this.playerAdminService = playerAdminService;
        this.adminProperties = adminProperties;
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        requireAdminEnabled();
        return ResponseEntity.ok(playerAdminService.count());
    }

    @DeleteMapping
    public ResponseEntity<Void> reset() {
        requireAdminEnabled();
        playerAdminService.reset();
        return ResponseEntity.noContent().build();
    }

    private void requireAdminEnabled() {
        if (!adminProperties.enabled()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin endpoints are disabled");
        }
    }
}
