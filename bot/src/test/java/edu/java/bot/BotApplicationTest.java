package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.telegram.command.AbstractCommand;
import edu.java.bot.telegram.command.AbstractServiceCommand;
import edu.java.bot.telegram.command.Command;
import edu.java.bot.configuration.TelegramBotConfig;
import edu.java.bot.telegram.exception.UnregisteredUserException;
import edu.java.bot.telegram.exception.command.CommandException;
import edu.java.bot.telegram.exception.command.CommandNotExistException;
import edu.java.bot.telegram.exception.command.NotCommandException;
import edu.java.dto.utils.exception.BadSourceUrlException;
import edu.java.dto.utils.exception.LinkException;
import edu.java.dto.utils.exception.LinkRegistrationException;
import edu.java.dto.utils.exception.NotLinkException;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.bot.telegram.exception.parameter.WrongNumberParametersException;
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
    private final TestScrapperService service;
    @Mock
    private Update updateMock;
    @Mock
    private Message messageMock;
    @Mock
    private User userMock;

    @Autowired BotApplicationTest(TelegramBotConfig config) {
        this.config = config;
        this.service = new TestScrapperService();
        for (Command command : config.commands()) {
            if (command instanceof AbstractServiceCommand sc) {
                sc.setService(service);
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

    @ParameterizedTest
    @MethodSource("startParams")
    void startTest(boolean isRegistered, String expected)
        throws CommandException, UnregisteredUserException, ParameterException, LinkException {
        service.setUserRegistered(isRegistered);
        setMessage("/start");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    private Arguments[] startParams() {
        return new Arguments[] {
            Arguments.of(false, "Вы успешно зарегистрировались"),
            Arguments.of(true, "Вы уже зарегистрировались")
        };
    }

    @Test
    void paramNumberExceptionTest() throws CommandException {
        setMessage("/start hello");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(WrongNumberParametersException.class, () -> command.execute(updateMock));
    }

    @Test
    void helpTest() throws UnregisteredUserException, ParameterException, CommandException, LinkException {
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
    void trackTest() throws UnregisteredUserException, ParameterException, CommandException, LinkException {
        service.setUserRegistered(true);
        service.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = "Ссылка добавлена в отслеживаемые";
         String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void notLinkExceptionTest() throws CommandException {
        service.setUserRegistered(true);
        service.setLinks(List.of());
        setMessage("/track github.com/cyberpanncake");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(NotLinkException.class, () -> command.execute(updateMock));
    }

    @Test
    void sourceNotSupportedExceptionTest() throws CommandException {
        service.setUserRegistered(true);
        service.setLinks(List.of());
        setMessage("/track https://google.com");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(SourceNotSupportedException.class, () -> command.execute(updateMock));
    }

    @Test
    void badSourceUrlExceptionTest() throws CommandException {
        service.setUserRegistered(true);
        service.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(BadSourceUrlException.class, () -> command.execute(updateMock));
    }

    @Test
    void unregisteredUserExceptionTest() throws CommandException {
        service.setUserRegistered(false);
        service.setLinks(List.of());
        setMessage("/track https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(UnregisteredUserException.class, () -> command.execute(updateMock));
    }

    @Test
    void linkRegisteredExceptionTest() throws CommandException {
        String link = "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring";
        service.setUserRegistered(true);
        service.setLinks(List.of(link));
        setMessage("/track " + link);
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(LinkRegistrationException.class, () -> command.execute(updateMock));
    }

    @Test
    void untrackTest() throws UnregisteredUserException, ParameterException, CommandException, LinkException {
        String link = "https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring";
        service.setUserRegistered(true);
        service.setLinks(List.of(link));
        setMessage("/untrack " + link);
        Command command = AbstractCommand.parse(updateMock, config.commands());
        String expected = "Ссылка удалена из отслеживаемых";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void linkNotRegisteredExceptionTest() throws CommandException {
        service.setUserRegistered(true);
        service.setLinks(List.of());
        setMessage("/untrack https://github.com/cyberpanncake/Torzhkova-Tinkoff-JavaBackendCourse-Spring");
        Command command = AbstractCommand.parse(updateMock, config.commands());
        Assertions.assertThrows(LinkRegistrationException.class, () -> command.execute(updateMock));
    }

    @ParameterizedTest
    @MethodSource("listParams")
    void startTest(List<String> links, String expected)
        throws UnregisteredUserException, ParameterException, CommandException, LinkException {
        service.setUserRegistered(true);
        service.setLinks(links);
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
                    "https://github.com/1",
                    "https://github.com/2",
                    "https://github.com/3"
                ),
                """
                    1. https://github.com/1
                    2. https://github.com/2
                    3. https://github.com/3"""
            )
        };
    }
}
