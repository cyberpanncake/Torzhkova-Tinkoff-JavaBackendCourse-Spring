package edu.java.scrapper.shedule.update.sources;

import edu.java.dto.api.exception.ApiException;
import edu.java.dto.utils.sources.info.GithubInfo;
import edu.java.dto.utils.sources.info.SourceInfo;
import edu.java.dto.utils.sources.info.StackoverflowInfo;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.configuration.ClientConfig;
import edu.java.scrapper.shedule.update.dto.Update;
import java.util.Optional;

public abstract class SourceUpdater {
    protected final ClientConfig config;

    protected SourceUpdater(ClientConfig config) {
        this.config = config;
    }

    public abstract Optional<Update> getUpdate(SourceInfo sourceInfo) throws ResponseException, ApiException;

    public static SourceUpdater getUpdaterForSource(ClientConfig config, SourceInfo sourceInfo) {
        if (sourceInfo instanceof GithubInfo) {
            return new GithubUpdater(config);
        } else if (sourceInfo instanceof StackoverflowInfo) {
            return new StackoverflowUpdater(config);
        }
        return null;
    }
}
