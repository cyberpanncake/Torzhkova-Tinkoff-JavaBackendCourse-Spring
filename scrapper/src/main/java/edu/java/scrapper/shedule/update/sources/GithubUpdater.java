package edu.java.scrapper.shedule.update.sources;

import edu.java.dto.utils.sources.info.GithubInfo;
import edu.java.dto.utils.sources.info.SourceInfo;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.client.sources.dto.SourceUpdate;
import edu.java.scrapper.configuration.ClientConfig;
import edu.java.scrapper.shedule.update.dto.Update;
import java.util.Optional;

public class GithubUpdater extends SourceUpdater {
    public GithubUpdater(ClientConfig config) {
        super(config);
    }

    @Override
    public Optional<Update> getUpdate(SourceInfo sourceInfo) throws ResponseException {
        GithubInfo info = (GithubInfo) sourceInfo;
        Optional<SourceUpdate> response = config.githubClient().getUpdate(info.getOwner(), info.getRepo());
        return response.map(r -> new Update(r.getDate(), r.getDetailsDescription()));
    }
}
