package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.dto.StatisticDto;
import fr.efreifive.manageplayer.dto.StatisticRequest;
import fr.efreifive.manageplayer.service.StatisticService;
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
@RequestMapping("/api/statistics")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<List<StatisticDto>> findAll() {
        return ResponseEntity.ok(this.statisticService.findAll());
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<StatisticDto> findByPlayerId(@PathVariable("playerId") UUID playerId) {
        return ResponseEntity.ok(this.statisticService.findByPlayerId(playerId));
    }

    @PostMapping("/{playerId}")
    public ResponseEntity<StatisticDto> create(@PathVariable("playerId") UUID playerId, @RequestBody StatisticRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.statisticService.create(playerId, request));
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<StatisticDto> update(@PathVariable("playerId") UUID playerId, @RequestBody StatisticRequest request) {
        return ResponseEntity.ok(this.statisticService.update(playerId, request));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> delete(@PathVariable("playerId") UUID playerId) {
        this.statisticService.delete(playerId);
        return ResponseEntity.noContent().build();
    }
}
