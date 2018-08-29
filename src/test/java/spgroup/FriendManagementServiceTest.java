package spgroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spgroup.service.FriendManagementService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementServiceTest {

    private FriendManagementService friendManagementService;

    private String firstUserEmail = "user1@gmail.com";
    private String secondUserEmail = "user2@gmail.com";

    @Before
    public void setup() {
        //TODO - Handle Intialization from spring
        friendManagementService = new FriendManagementService();
    }

    @After
    public void tearDown(){
        friendManagementService = null;
    }


    //ConnectFriends Tests
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
    }

    //Fetch Friends Tests

    @Test
    public void testFetchFriendNull() {
        List<String> friends = null;
        try {
            friends = friendManagementService.getFriendEmails(null);
        }catch (IllegalArgumentException e) {
            friends = null;
        }
        assertThat(friends).isNull();
    }

    @Test
    public void testFetchFriendWrongEmail() {
        List<String> friends = null;
        try {
            friends = friendManagementService.getFriendEmails("wrongemail");
        }catch (IllegalArgumentException e) {
            friends = null;
        }
        assertThat(friends).isNull();
    }

    @Test
    public void testFetchFriendZeroFriends() {
        List<String> friends = friendManagementService.getFriendEmails(firstUserEmail);
        assertThat(friends).isNull();
    }

    @Test
    public void testFetchFriend() {
        friendManagementService.connectFriends(firstUserEmail, secondUserEmail);
        List<String> friends = friendManagementService.getFriendEmails(firstUserEmail);
        assertThat(friends).isNotNull();
    }

    @Test
    public void testFetchFriendData() {
        friendManagementService.connectFriends(firstUserEmail, secondUserEmail);
        List<String> friends = friendManagementService.getFriendEmails(firstUserEmail);
        assertThat(friends.get(0)).isEqualTo(secondUserEmail);
    }
}
