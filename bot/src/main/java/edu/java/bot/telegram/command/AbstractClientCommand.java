package edu.java.bot.telegram.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.CommandConfig;

public abstract class AbstractClientCommand extends AbstractCommand {
    protected ScrapperClient client;
    protected final CommandConfig config;

    public AbstractClientCommand(ScrapperClient client, CommandConfig config) {
        this.client = client;
        this.config = config;
    }

    public void setClient(ScrapperClient client) {
        this.client = client;
    }
}
