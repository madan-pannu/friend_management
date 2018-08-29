package spgroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spgroup.service.FriendManagementService;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementServiceTest {

    @Autowired
    FriendManagementService friendManagementService;

    private String firstUserEmail = "user1@gmail.com";
    private String secondUserEmail = "user2@gmail.com";

    @Test
    public void testConnectNullEmail() {
        boolean isConnected;
        try {
            isConnected = friendManagementService.connectFriends(null, null);
        } catch (IllegalArgumentException e) {
            isConnected = false;
        }

        assertThat(isConnected).isEqualTo(false);
    }


    @Test
    public void testConnectWrongEmail(){
        boolean isConnected;
        try {
            isConnected = friendManagementService.connectFriends("user1", "user2");
        } catch (IllegalArgumentException e) {
            isConnected = false;
        }
        assertThat(isConnected).isEqualTo(false);
    }

    @Test
    public void testConnectCorrectEmail(){
        boolean isConnected = friendManagementService.connectFriends(firstUserEmail, secondUserEmail);
        assertThat(isConnected).isEqualTo(true);
    }x
}
