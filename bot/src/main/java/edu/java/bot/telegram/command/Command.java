package edu.java.bot.telegram.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.exception.command.CommandException;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.dto.utils.exception.LinkException;

public interface Command {

    String getName();

    String getDescription();

    String execute(Update update) throws ParameterException, CommandException, LinkException;
}
