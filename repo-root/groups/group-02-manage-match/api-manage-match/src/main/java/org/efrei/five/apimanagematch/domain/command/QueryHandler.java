package org.efrei.five.apimanagematch.domain.command;

public interface QueryHandler<C extends Query<R>, R> {
    R handle(C command);
}
