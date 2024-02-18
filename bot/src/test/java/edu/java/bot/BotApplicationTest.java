package edu.java.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.command.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
    void startSuccessTest() throws Exception {
        TestScrapperService service = new TestScrapperService(false, List.of());
        Command.setService(service);
        setMessage("/start");

        String expected = "Вы успешно зарегистрировались";
        Command command = Command.parse(updateMock);
        String actual = command.execute(updateMock);

        Assertions.assertEquals(expected, actual);
    }
}
