package edu.java.scrapper.shedule.update.sources;

import edu.java.dto.utils.sources.info.SourceInfo;
import edu.java.dto.utils.sources.info.StackoverflowInfo;
import edu.java.scrapper.client.sources.ResponseException;
import edu.java.scrapper.client.sources.dto.StackoverflowResponse;
import edu.java.scrapper.configuration.ClientConfig;
import edu.java.scrapper.shedule.update.dto.Update;
import java.util.Optional;

public class StackoverflowUpdater extends SourceUpdater {
    public StackoverflowUpdater(ClientConfig config) {
        super(config);
    }

    @Override
    public Optional<Update> getUpdate(SourceInfo sourceInfo) throws ResponseException {
        StackoverflowInfo info = (StackoverflowInfo) sourceInfo;
        Optional<StackoverflowResponse> response = config.stackoverflowClient().getUpdate(info.getQuestionId());
        return response.map(stackoverflowResponse -> new Update(stackoverflowResponse.lastActivityDate()));
    }
}
