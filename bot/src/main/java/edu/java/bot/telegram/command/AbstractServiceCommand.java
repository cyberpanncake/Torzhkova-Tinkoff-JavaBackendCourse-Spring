package edu.java.bot.telegram.command;

import edu.java.bot.client.service.ScrapperService;
import edu.java.bot.configuration.CommandConfig;

public abstract class AbstractServiceCommand extends AbstractCommand {
    protected ScrapperService service;
    protected final CommandConfig config;

    public AbstractServiceCommand(ScrapperService service, CommandConfig config) {
        this.service = service;
        this.config = config;
    }

    public void setService(ScrapperService service) {
        this.service = service;
    }
}
