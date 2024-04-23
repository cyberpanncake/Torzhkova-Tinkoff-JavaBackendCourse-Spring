package edu.java.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.api.exception.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import edu.java.dto.api.scrapper.AddLinkRequest;
import edu.java.dto.api.scrapper.ApiErrorResponse;
import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.dto.api.scrapper.ListLinksResponse;
import edu.java.dto.api.scrapper.RemoveLinkRequest;
import lombok.NonNull;
import lombok.Setter;
import reactor.util.retry.Retry;

public class TestScrapperClient extends ScrapperClient {
    @Setter
    private boolean isChatRegistered;
    @Setter
    private List<String> links;

    public TestScrapperClient(@NonNull String baseUrl, ObjectMapper mapper, Retry retry) {
        super(baseUrl, mapper, retry);
    }

    @Override
    public void registerChat(long id) throws ScrapperApiException {
        if (isChatRegistered) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "ChatAlreadyRegisteredException", "", new String[0]));
        }
    }

    @Override
    public void deleteChat(long id) throws ScrapperApiException {
        if (!isChatRegistered) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "ChatNotFoundException", "", new String[0]));
        }
    }

    @Override
    public ListLinksResponse getLinks(long id) throws ScrapperApiException {
        if (!isChatRegistered) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "ChatNotFoundException", "", new String[0]));
        }
        return new ListLinksResponse(links.stream()
            .map(l -> {
                try {
                    return new LinkResponse(null, new URI(l));
                } catch (URISyntaxException e) {
                    return new LinkResponse(null, null);
                }
            })
            .toArray(LinkResponse[]::new),
            links.size());
    }

    @Override
    public LinkResponse addLink(long id, AddLinkRequest request) throws ScrapperApiException {
        if (!isChatRegistered) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "ChatNotFoundException", "", new String[0]));
        }
        if (links.contains(request.link())) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "LinkAdditionException", "", new String[0]));
        }
        try {
            return new LinkResponse(null, new URI(request.link()));
        } catch (URISyntaxException e) {
            return new LinkResponse(null, null);
        }
    }

    @Override
    public LinkResponse deleteLink(long id, RemoveLinkRequest request) throws ScrapperApiException {
        if (!isChatRegistered) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "ChatNotFoundException", "", new String[0]));
        }
        if (!links.contains(request.link())) {
            throw new ScrapperApiException(new ApiErrorResponse("", "",
                "LinkNotFoundException", "", new String[0]));
        }
        try {
            return new LinkResponse(null, new URI(request.link()));
        } catch (URISyntaxException e) {
            return new LinkResponse(null, null);
        }
    }
}
