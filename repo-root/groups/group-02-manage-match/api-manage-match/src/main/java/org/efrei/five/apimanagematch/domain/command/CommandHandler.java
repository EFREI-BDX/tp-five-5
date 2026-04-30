package org.efrei.five.apimanagematch.domain.command;

public interface CommandHandler<C> {
    void handle(C command);
}