package edu.java.scrapper.api.controller;

import edu.java.api_dto.scrapper.AddLinkRequest;
import edu.java.api_dto.scrapper.ApiErrorResponse;
import edu.java.api_dto.scrapper.LinkResponse;
import edu.java.api_dto.scrapper.ListLinksResponse;
import edu.java.api_dto.scrapper.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылки успешно получены",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ListLinksResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader(name = "Tg-Chat-Id") long chatId) {
        /*
        TODO: получение списка ссылок
         */
        return ResponseEntity.ok(new ListLinksResponse(new LinkResponse[0], 1));
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = LinkResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader(name = "Tg-Chat-Id") long chatId,
        @Valid @RequestBody AddLinkRequest request
    ) throws URISyntaxException {
        /*
        TODO: добавление ссылки
        может быть брошено LinkAdditionException
         */
        return ResponseEntity.ok(new LinkResponse(0L, new URI("")));
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = LinkResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ApiErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Ссылка не найдена",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader(name = "Tg-Chat-Id") long chatId,
        @Valid @RequestBody RemoveLinkRequest request
    ) throws URISyntaxException {
        /*
        TODO: удаление ссылки
        может быть брошено LinkNotFoundException
         */
        return ResponseEntity.ok(new LinkResponse(0L, new URI("")));
    }
}
