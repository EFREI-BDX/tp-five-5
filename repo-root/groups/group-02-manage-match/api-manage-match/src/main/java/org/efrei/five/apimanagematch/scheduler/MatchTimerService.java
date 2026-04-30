package org.efrei.five.apimanagematch.scheduler;

import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.external.IMatchRepository;
import org.efrei.five.apimanagematch.domain.service.IMatchTimerService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;
import org.efrei.five.apimanagematch.external.MatchEventOutboundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MatchTimerService implements IMatchTimerService {

    private static final Logger log = LoggerFactory.getLogger(MatchTimerService.class);

    private final ThreadPoolTaskScheduler taskScheduler;
    private final MatchEventOutboundService outboundService;
    private final IMatchRepository matchRepository;

    private final Map<UUID, List<ScheduledFuture<?>>> scheduledTasks = new ConcurrentHashMap<>();

    public MatchTimerService(ThreadPoolTaskScheduler taskScheduler,
                             MatchEventOutboundService outboundService,
                             IMatchRepository matchRepository) {
        this.taskScheduler = taskScheduler;
        this.outboundService = outboundService;
        this.matchRepository = matchRepository;
    }

    @Override
    public void scheduleMatchTimers(Match match) {
        ScheduledFuture<?> startFuture = taskScheduler.schedule(
                () -> onMatchStart(match),
                match.period().start().atZone(ZoneId.systemDefault()).toInstant()
        );

        ScheduledFuture<?> endFuture = taskScheduler.schedule(
                () -> onMatchEnd(match),
                match.period().end().atZone(ZoneId.systemDefault()).toInstant()
        );

        scheduledTasks.put(match.id().value(), List.of(startFuture, endFuture));
        log.info("Timers scheduled for match {}", match.id().value());
    }

    @Override
    public void cancelMatchTimers(Id matchId) {
        List<ScheduledFuture<?>> futures = scheduledTasks.remove(matchId.value());
        if (futures != null) {
            futures.forEach(f -> f.cancel(false));
            log.info("Timers cancelled for match {}", matchId.value());
        }
    }

    private void onMatchStart(Match match) {
        log.info("Match {} starting", match.id().value());
        matchRepository.updateMatchStatus(match.id(), MatchState.IN_PROGRESS);
        outboundService.notifyMatchStarted(match);
    }

    private void onMatchEnd(Match match) {
        log.info("Match {} ending", match.id().value());
        matchRepository.updateMatchStatus(match.id(), MatchState.FINISHED);
        outboundService.notifyMatchEnded(match);
        scheduledTasks.remove(match.id().value());
    }
}