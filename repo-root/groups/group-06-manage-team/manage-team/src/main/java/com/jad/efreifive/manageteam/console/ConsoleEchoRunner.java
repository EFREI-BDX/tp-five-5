package com.jad.efreifive.manageteam.console;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.console.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class ConsoleEchoRunner implements SmartLifecycle {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executorService;
    private Scanner scanner;

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        scanner = new Scanner(System.in);
        executorService = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable, "console-echo-thread");
            thread.setDaemon(true);
            return thread;
        });
        executorService.submit(this::runLoop);
        log.info("Console echo thread started");
    }

    private void runLoop() {
        while (running.get()) {
            try {
                if (!scanner.hasNextLine()) {
                    break;
                }
                final String input = scanner.nextLine();
                System.out.println("console> " + input);
            } catch (final IllegalStateException exception) {
                log.info("Console scanner closed, stopping loop");
                break;
            } catch (final RuntimeException exception) {
                log.error("Unexpected error in console thread", exception);
                break;
            }
        }
        running.set(false);
        log.info("Console echo thread stopped");
    }

    @Override
    public void stop() {
        running.set(false);
        if (scanner != null) {
            scanner.close();
        }
        if (executorService != null) {
            executorService.shutdownNow();
            try {
                if (!executorService.awaitTermination(2L, TimeUnit.SECONDS)) {
                    log.warn("Console thread did not terminate within timeout");
                }
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while waiting for console thread shutdown");
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}

