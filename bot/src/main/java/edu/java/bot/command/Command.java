package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.command.CommandException;
import edu.java.bot.exception.link.LinkException;
import edu.java.bot.exception.parameter.ParameterException;

public interface Command {

    String getName();

    String getDescription();

    String execute(Update update) throws UnregisteredUserException, ParameterException, CommandException, LinkException;
}
