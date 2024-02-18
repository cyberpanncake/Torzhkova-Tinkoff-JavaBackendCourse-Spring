package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.command.Command;
import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.command.CommandException;
import edu.java.bot.exception.command.CommandNotExistException;
import edu.java.bot.exception.command.NotCommandException;
import edu.java.bot.exception.link.LinkRegistrationException;
import edu.java.bot.exception.link.NotLinkException;
import edu.java.bot.exception.parameter.WrongNumberParametersException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {BotApplication.class})
class BotApplicationTest {
    @Mock
    private Update updateMock;
    @Mock
    private Message messageMock;
    @Mock
    private User userMock;

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
        Assertions.assertThrows(NotCommandException.class, () -> Command.parse(updateMock));
    }

    @Test
    void commandNotExistExceptionTest() {
        setMessage("/end");
        Assertions.assertThrows(CommandNotExistException.class, () -> Command.parse(updateMock));
    }

    @ParameterizedTest
    @MethodSource("startParams")
    void startTest(boolean isRegistered, String expected) throws Exception {
        TestScrapperService service = new TestScrapperService(isRegistered, List.of());
        Command.setService(service);
        setMessage("/start");
        Command command = Command.parse(updateMock);
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
        Command command = Command.parse(updateMock);
        Assertions.assertThrows(WrongNumberParametersException.class, () -> command.execute(updateMock));
    }

    @Test
    void helpTest() throws Exception {
        setMessage("/help");
        Command command = Command.parse(updateMock);
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
    void trackTest() throws Exception {
        TestScrapperService service = new TestScrapperService(true, List.of());
        Command.setService(service);
        setMessage("/track https://github.com/cyberpanncake");
        Command command = Command.parse(updateMock);
        String expected = "Ссылка добавлена в отслеживаемые";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void notLinkExceptionTest() throws Exception {
        TestScrapperService service = new TestScrapperService(true, List.of());
        Command.setService(service);
        setMessage("/track github.com/cyberpanncake");
        Command command = Command.parse(updateMock);
        Assertions.assertThrows(NotLinkException.class, () -> command.execute(updateMock));
    }

    @Test
    void unregisteredUserExceptionTest() throws CommandException {
        TestScrapperService service = new TestScrapperService(false, List.of());
        Command.setService(service);
        setMessage("/track https://github.com/cyberpanncake");
        Command command = Command.parse(updateMock);
        Assertions.assertThrows(UnregisteredUserException.class, () -> command.execute(updateMock));
    }

    @Test
    void linkRegisteredExceptionTest() throws CommandException {
        String link = "https://github.com/cyberpanncake";
        TestScrapperService service = new TestScrapperService(true, List.of(link));
        Command.setService(service);
        setMessage("/track " + link);
        Command command = Command.parse(updateMock);
        Assertions.assertThrows(LinkRegistrationException.class, () -> command.execute(updateMock));
    }

    @Test
    void untrackTest() throws Exception {
        String link = "https://github.com/cyberpanncake";
        TestScrapperService service = new TestScrapperService(true, List.of(link));
        Command.setService(service);
        setMessage("/untrack " + link);
        Command command = Command.parse(updateMock);
        String expected = "Ссылка удалена из отслеживаемых";
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void linkNotRegisteredExceptionTest() throws CommandException {
        TestScrapperService service = new TestScrapperService(true, List.of());
        Command.setService(service);
        setMessage("/untrack https://github.com/cyberpanncake");
        Command command = Command.parse(updateMock);
        Assertions.assertThrows(LinkRegistrationException.class, () -> command.execute(updateMock));
    }

    @ParameterizedTest
    @MethodSource("listParams")
    void startTest(List<String> links, String expected) throws Exception {
        TestScrapperService service = new TestScrapperService(true, links);
        Command.setService(service);
        setMessage("/list");
        Command command = Command.parse(updateMock);
        String actual = command.execute(updateMock);
        Assertions.assertEquals(expected, actual);
    }

    private Arguments[] listParams() {
        return new Arguments[] {
            Arguments.of(List.of(),
                "У вас нет отслеживаемых ссылок"),
            Arguments.of(List.of("https://github.com/cyberpanncake"),
                "1. https://github.com/cyberpanncake"),
            Arguments.of(List.of("https://github.com/1",
                    "https://github.com/2",
                    "https://github.com/3"),
                    """
                            1. https://github.com/1
                            2. https://github.com/2
                            3. https://github.com/3""")
        };
    }
}
