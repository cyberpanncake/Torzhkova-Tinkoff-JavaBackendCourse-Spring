package edu.java.bot.telegram.command;

import edu.java.bot.client.service.ScrapperService;

public abstract class AbstractServiceCommand extends AbstractCommand {
    protected ScrapperService service;

    public AbstractServiceCommand(ScrapperService service) {
        this.service = service;
    }

    public void setService(ScrapperService service) {
        this.service = service;
    }
}
