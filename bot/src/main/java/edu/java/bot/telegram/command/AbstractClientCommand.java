package edu.java.bot.telegram.command;

import edu.java.bot.client.ScrapperClient;

public abstract class AbstractClientCommand extends AbstractCommand {
    protected ScrapperClient client;

    public AbstractClientCommand(ScrapperClient client) {
        this.client = client;
    }

    public void setClient(ScrapperClient client) {
        this.client = client;
    }
}
