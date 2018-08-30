package spgroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spgroup.service.BlockEmailService;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockEmailServiceTest {

    private BlockEmailService blockEmailService;

    private String firstUserEmail = "user1@gmail.com";
    private String secondUserEmail = "user2@gmail.com";

    @Before
    public void setup() {
        //TODO - Handle Intialization from spring
        blockEmailService = new BlockEmailService();
    }

    @After
    public void tearDown(){
        blockEmailService = null;
    }

    //Block email test

    @Test
    public void testBlockEmailNull() {
        boolean isBlocked;
        try {
            isBlocked = blockEmailService.blockEmail(null, null);
        } catch (IllegalArgumentException e) {
            isBlocked = false;
        }
        assertThat(isBlocked).isEqualTo(false);
    }

    @Test
    public void testBlockEmailWrongData() {
        boolean isBlocked;
        try {
            isBlocked = blockEmailService.blockEmail("user1", "user2");
        } catch (IllegalArgumentException e) {
            isBlocked = false;
        }
        assertThat(isBlocked).isEqualTo(false);
    }


    @Test
    public void testBlockEmailSameData() {
        boolean isBlocked = blockEmailService.blockEmail(firstUserEmail, firstUserEmail);
        assertThat(isBlocked).isEqualTo(false);
    }

    @Test
    public void testBlockEmailCorrectData() {
        boolean isBlocked = blockEmailService.blockEmail(firstUserEmail, secondUserEmail);
        assertThat(isBlocked).isEqualTo(true);
    }

}
