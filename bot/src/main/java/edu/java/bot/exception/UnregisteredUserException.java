package edu.java.bot.exception;

public class UnregisteredUserException extends Exception {

    public UnregisteredUserException() {
        super("Сначала нужно зарегистрироваться. Введите команду /start");
    }
}
