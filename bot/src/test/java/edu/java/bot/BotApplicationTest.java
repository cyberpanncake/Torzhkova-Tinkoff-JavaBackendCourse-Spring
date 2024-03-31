package edu.java.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.telegram.command.AbstractCommand;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.Command;
import edu.java.bot.configuration.TelegramBotConfig;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatAlreadyRegisteredException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.chat.ChatNotFoundException;
import edu.java.bot.telegram.command.exception.command.CommandException;
import edu.java.bot.telegram.command.exception.command.CommandNotExistException;
import edu.java.bot.telegram.command.exception.command.NotCommandException;
import edu.java.bot.telegram.command.exception.link.LinkAlreadyAddedException;
import edu.java.bot.telegram.command.exception.link.LinkException;
import edu.java.bot.telegram.command.exception.link.LinkNotFoundException;
import edu.java.dto.utils.exception.BadSourceUrlException;
import edu.java.dto.utils.exception.UrlException;
import edu.java.dto.utils.exception.NotUrlException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.bot.telegram.command.exception.parameter.WrongNumberParametersException;
import java.util.List;
import edu.java.dto.utils.exception.SourceNotSupportedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {BotApplication.class})
class BotApplicationTest {
    private final TelegramBotConfig config;
    private final TestScrapperClient client;
    @Mock
    private Update updateMock;
    @Mock
    private Message messageMock;
    @Mock
    private User userMock;

    @Autowired BotApplicationTest(TelegramBotConfig config) {
        this.config = config;
        this.client = new TestScrapperClient("", new ObjectMapper());
        for (Command command : config.commands()) {
            if (command instanceof AbstractClientCommand sc) {
                sc.setClient(client);
            }
        }
    }

    @BeforeEach
    public void setUpMocks() {
        when(updateMock.message()).thenReturn(messageMock);
        when(messageMock.from()).thenReturn(userMock);
    }

    private void setMessage(String text) {
        when(messageMock.text()).thenReturn(text);
    }

    @Test
    void notCommandExceptionTest() {
        setMessage("start");
        Assertions.assertThrows(
            NotCommandException.class,
            () -> AbstractCommand.parse(updateMock, config.commands())
        );
    }

    @Test
    void commandNotExistExceptionTest() {
        setMessage("/end");
        Assertions.assertThrows(
            CommandNotExistException.class,
            () -> AbstractCommand.parse(updateMock, config.commands())
        );
    }

    @Test
    void startTest()
        throws CommandException, ParameterException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        client.setChatRegistered(false);
        setMessage("/start");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = "Вы успешно зарегистрировались";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void startExceptionTest() throws CommandException {
        client.setChatRegistered(true);
        setMessage("/start");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(ChatAlreadyRegisteredException.class, () -> command.execute(updateMock));
    }

    @Test
    void paramNumberExceptionTest() throws CommandException {
        setMessage("/start hello");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(WrongNumberParametersException.class, () -> command.execute(updateMock));
    }

    @Test
    void helpTest() throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        setMessage("/help");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = """
            /start — зарегистрировать пользователя,
            /help — вывести окно с командами,
            /track — начать отслеживание ссылки,
            /untrack — прекратить отслеживание ссылки,
            /list — показать список отслеживаемых ссылок""";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void trackTest()
        throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        client.setChatRegistered(true);
        client.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = "Ссылка добавлена в отслеживаемые";
         String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void notLinkExceptionTest() throws CommandException {
        client.setChatRegistered(true);
        client.setLinks(List.of());
        setMessage("/track github.com/cyberpanncake");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(NotUrlException.class, () -> command.execute(updateMock));
    }

    @Test
    void sourceNotSupportedExceptionTest() throws CommandException {
        client.setChatRegistered(true);
        client.setLinks(List.of());
        setMessage("/track https://google.com");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(SourceNotSupportedException.class, () -> command.execute(updateMock));
    }

    @Test
    void badSourceUrlExceptionTest() throws CommandException {
        client.setChatRegistered(true);
        client.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(BadSourceUrlException.class, () -> command.execute(updateMock));
    }

    @Test
    void unregisteredUserExceptionTest() throws CommandException {
        client.setChatRegistered(false);
        client.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(ChatNotFoundException.class, () -> command.execute(updateMock));
    }

    @Test
    void linkRegisteredExceptionTest() throws CommandException {
        String link = "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring";
        client.setChatRegistered(true);
        client.setLinks(List.of(link));
        setMessage("/track " + link);
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(LinkAlreadyAddedException.class, () -> command.execute(updateMock));
    }

    @Test
    void untrackTest()
        throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        String link = "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring";
        client.setChatRegistered(true);
        client.setLinks(List.of(link));
        setMessage("/untrack " + link);
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = "Ссылка удалена из отслеживаемых";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void linkNotRegisteredExceptionTest() throws CommandException {
        client.setChatRegistered(true);
        client.setLinks(List.of());
        setMessage("/untrack https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(LinkNotFoundException.class, () -> command.execute(updateMock));
    }

    @ParameterizedTest
    @MethodSource("listParams")
    void listTest(List<String> links, String expected)
        throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        client.setChatRegistered(true);
        client.setLinks(links);
        setMessage("/list");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    private Arguments[] listParams() {
        return new Arguments[] {
            Arguments.of(
                List.of(),
                "У вас нет отслеживаемых ссылок"
            ),
            Arguments.of(
                List.of("https://github.com/cyberpanncake"),
                "1. https://github.com/cyberpanncake"
            ),
            Arguments.of(
                List.of(
                    "https://github.com",
                    "https://github.com/cyberpanncake",
                    "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring"
                ),
                """
                    1. https://github.com
                    2. https://github.com/cyberpanncake
                    3. https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring"""
            )
        };
    }
}
