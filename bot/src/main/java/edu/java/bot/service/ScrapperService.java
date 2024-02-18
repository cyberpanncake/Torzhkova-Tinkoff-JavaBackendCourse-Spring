package edu.java.bot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScrapperService {
    private ScrapperService() {
    }

    public static boolean isUserRegistered(Long userId) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Проверка регистрации пользователя %d".formatted(LocalDateTime.now(), userId));
        return false;
    }

    public static void registerUser(Long userId) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Пользователь %d зарегистрирован".formatted(LocalDateTime.now(), userId));
    }

    public static boolean isLinkRegistered(Long userId, String link) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Проверка регистрации ссылки \"%s\" у пользователя %d"
            .formatted(LocalDateTime.now(), link, userId));
        return false;
    }

    public static void addLink(Long userId, String link) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Пользователь %d добавил ссылку \"%s\"".formatted(LocalDateTime.now(), userId, link));
    }

    public static void deleteLink(Long userId, String link) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Пользователь %d удалил ссылку \"%s\"".formatted(LocalDateTime.now(), userId, link));
    }

    public static List<String> getLinks(Long userId) {
        // TODO: добавить работу с БД через scrapper
        log.info("%s. Пользователь %d запросил список ссылок".formatted(LocalDateTime.now(), userId));
        return new ArrayList<>();
    }
}
