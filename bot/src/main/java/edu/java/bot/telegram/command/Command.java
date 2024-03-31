package edu.java.bot.telegram.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.command.CommandException;
import edu.java.bot.telegram.command.exception.link.LinkException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.dto.utils.exception.UrlException;

public interface Command {

    String getName();

    String getDescription();

    String execute(Update update)
        throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException;
}
