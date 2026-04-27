package org.efrei.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.efrei.domain.entities.Match;
import org.efrei.domain.valueobjects.Id;
import org.efrei.domain.valueobjects.Period;
import org.efrei.dto.CreateMatchRequest;
import org.efrei.dto.MatchResponse;
import org.efrei.services.MatchService;

import java.util.UUID;

@Controller("/matches")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @Get("/{id}")
    public HttpResponse<MatchResponse> getMatch(@PathVariable UUID id) {
        return matchService.getMatch(new Id(id))
                .map(match -> HttpResponse.ok(MatchResponse.fromDomain(match)))
                .orElse(HttpResponse.notFound());
    }

    @Post
    public HttpResponse<MatchResponse> createMatch(@Body CreateMatchRequest request) {
        Match match = matchService.createMatch(
                new Id(request.teamAUuid()),
                new Id(request.teamBUuid()),
                new Id(request.fieldUuid()),
                new Period(request.start(), request.end())
        );

        return HttpResponse.created(MatchResponse.fromDomain(match));
    }

    @Post("/{id}/cancel")
    public HttpResponse<?> cancelMatch(@PathVariable UUID id) {
        try {
            matchService.cancelMatch(new Id(id));
            return HttpResponse.ok();
        } catch (IllegalStateException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return HttpResponse.notFound();
        }
    }
}
