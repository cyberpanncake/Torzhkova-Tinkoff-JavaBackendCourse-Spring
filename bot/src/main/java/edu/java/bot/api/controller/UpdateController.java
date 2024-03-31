package edu.java.bot.api.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.api.exception.ChatsNotFoundException;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.TelegramBotConfig;
import edu.java.dto.api.bot.ApiErrorResponse;
import edu.java.dto.api.bot.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@Slf4j
public class UpdateController {
    private final TelegramBot bot;

    @Autowired
    public UpdateController(ApplicationConfig config, TelegramBotConfig botConfig) {
        bot = botConfig.telegramBot(config);
    }

    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = {@Content(mediaType = "application/json",
                                         schema = @Schema(implementation = ApiErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdateRequest request) throws
        ChatsNotFoundException {
        List<Long> errorIds = new ArrayList<>();
        for (Long id : request.tgChatIds()) {
            SendResponse response = bot.execute(new SendMessage(id, request.description()));
            if (!response.isOk()) {
                log.error("%s. Чат %d".formatted(LocalDateTime.now(), id));
                errorIds.add(id);
            }
        }
        if (!errorIds.isEmpty()) {
            throw new ChatsNotFoundException(errorIds);
        }
        return ResponseEntity.ok().build();
    }
}
