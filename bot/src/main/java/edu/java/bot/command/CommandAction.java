package edu.java.bot.command;

@FunctionalInterface
public interface CommandAction<U, P, R, E extends Exception> {
    R apply(U u, P p) throws E;
}
