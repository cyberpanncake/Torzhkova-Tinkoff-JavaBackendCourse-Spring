package edu.java.bot.telegram.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.exception.UnregisteredUserException;
import edu.java.bot.telegram.exception.command.CommandException;
import edu.java.bot.telegram.exception.link.LinkException;
import edu.java.bot.telegram.exception.parameter.ParameterException;

public interface Command {

    String getName();

    String getDescription();

    String execute(Update update) throws UnregisteredUserException, ParameterException, CommandException, LinkException;
}
