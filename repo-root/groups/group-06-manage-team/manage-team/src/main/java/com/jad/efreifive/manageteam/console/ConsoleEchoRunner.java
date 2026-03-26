package com.jad.efreifive.manageteam.console;

import jakarta.annotation.PreDestroy;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.console.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class ConsoleEchoRunner {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executorService;
    private Scanner scanner;

    @EventListener(ApplicationReadyEvent.class)
    public void startConsole() {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }
        this.scanner = new Scanner(System.in);
        this.executorService = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable, "console-echo-thread");
            thread.setDaemon(true);
            return thread;
        });
        this.executorService.submit(this::runLoop);
        ConsoleEchoRunner.log.info("Console echo thread started");
    }

    private void runLoop() {
        while (this.running.get()) {
            try {
                if (!this.scanner.hasNextLine()) {
                    break;
                }
                final String input = this.scanner.nextLine();
                System.out.println("console> " + input);
            } catch (final IllegalStateException exception) {
                ConsoleEchoRunner.log.info("Console scanner closed, stopping loop");
                break;
            } catch (final RuntimeException exception) {
                ConsoleEchoRunner.log.error("Unexpected error in console thread", exception);
                break;
            }
        }
        this.running.set(false);
        ConsoleEchoRunner.log.info("Console echo thread stopped");
    }

    @PreDestroy
    public void stopConsole() {
        this.running.set(false);
        if (this.scanner != null) {
            this.scanner.close();
        }
        if (this.executorService != null) {
            this.executorService.shutdownNow();
            try {
                if (!this.executorService.awaitTermination(2L, TimeUnit.SECONDS)) {
                    ConsoleEchoRunner.log.warn("Console thread did not terminate within timeout");
                }
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                ConsoleEchoRunner.log.warn("Interrupted while waiting for console thread shutdown");
            }
        }
        this.scanner = null;
        this.executorService = null;
    }

    public boolean isRunning() {
        return this.running.get();
    }
}

