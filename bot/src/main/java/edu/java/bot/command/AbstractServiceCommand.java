package edu.java.bot.command;

import edu.java.bot.service.ScrapperService;

public abstract class AbstractServiceCommand extends AbstractCommand {
    protected ScrapperService service;

    public AbstractServiceCommand(ScrapperService service) {
        this.service = service;
    }

    public void setService(ScrapperService service) {
        this.service = service;
    }
}
