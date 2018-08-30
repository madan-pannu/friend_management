package spgroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spgroup.service.BlockEmailService;
import spgroup.service.FriendManagementService;
import spgroup.service.SubscriptionService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementServiceTest {

    @Autowired
    private FriendManagementService friendManagementService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private BlockEmailService blockEmailService;

    private String firstUserEmail = "user1@gmail.com";
    private String secondUserEmail = "user2@gmail.com";

    @Before
    public void setup() {
    }

    @After
    public void tearDown(){
        friendManagementService.clear();
        subscriptionService.clear();
        blockEmailService.clear();
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

    //Common Friends Test
    @Test
    public void testCommonFriendNull() {
        List<String> commonFriends = null;
        try {
            commonFriends = friendManagementService.getCommonFriends(null, null);
        }catch (IllegalArgumentException e) {
            commonFriends = null;
        }
        assertThat(commonFriends).isNull();
    }

    @Test
    public void testCommonFriendWrongEmail() {
        List<String> commonFriends = null;
        try {
            commonFriends = friendManagementService.getCommonFriends("wrongemail", "wrongemail2");
        }catch (IllegalArgumentException e) {
            commonFriends = null;
        }
        assertThat(commonFriends).isNull();
    }

    @Test
    public void testCommonFriendZeroFriend() {
        List<String> commonFriends = friendManagementService.getCommonFriends(firstUserEmail, secondUserEmail);
        assertThat(commonFriends).isNull();
    }

    @Test
    public void testCommonFriendData() {

        friendManagementService.connectFriends(firstUserEmail, "dummy@gmail.com");
        friendManagementService.connectFriends(secondUserEmail, "dummy@gmail.com");
        List<String> commonFriends = friendManagementService.getCommonFriends(firstUserEmail, secondUserEmail);
        assertThat(commonFriends.get(0)).isEqualTo("dummy@gmail.com");
    }


    //Test update email list
    @Test
    public void testGetUpdateEmailListNull() {
        List<String> updateEmailList = null;
        try {
            updateEmailList = friendManagementService.getUpdateEmailList(null, null);
        } catch (IllegalArgumentException e) {
            updateEmailList = null;
        }
        assertThat(updateEmailList).isEmpty();
    }

    @Test
    public void testGetUpdateEmailListWrongData() {
        List<String> updateEmailList = null;
        try {
            updateEmailList = friendManagementService.getUpdateEmailList("user", "user test");
        } catch (IllegalArgumentException e) {
            updateEmailList = null;
        }
        assertThat(updateEmailList).isEmpty();
    }

    @Test
    public void testGetUpdateEmailListFriendsEmail() {
        friendManagementService.connectFriends(firstUserEmail, secondUserEmail);
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(firstUserEmail, "random text");
        assertThat(updateEmailList).isNotEmpty();
        assertThat(updateEmailList.get(0)).isEqualTo(secondUserEmail);
    }

    @Test
    public void testGetUpdateEmailListFriendsEmailWithText() {
        String thirduserEmail = "user3@gmail.com";
        friendManagementService.connectFriends(firstUserEmail, secondUserEmail);
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(firstUserEmail, "random text "+thirduserEmail);
        assertThat(updateEmailList).isNotEmpty();
        assertThat(updateEmailList.size()).isEqualTo(2);
    }

    @Test
    public void testGetUpdateEmailListSubscribedEmail() {
        subscriptionService.subscribe(secondUserEmail, firstUserEmail);
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(firstUserEmail, "random text");
        assertThat(updateEmailList).isNotEmpty();
        assertThat(updateEmailList.get(0)).isEqualTo(secondUserEmail);
    }

    @Test
    public void testGetUpdateEmailListSubscribedEmailWithText() {
        String thirduserEmail = "user3@gmail.com";
        subscriptionService.subscribe(secondUserEmail, firstUserEmail);
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(firstUserEmail, "random text "+thirduserEmail);
        assertThat(updateEmailList).isNotEmpty();
        assertThat(updateEmailList.size()).isEqualTo(2);
        assertThat(updateEmailList.get(0)).isEqualTo(secondUserEmail);
        assertThat(updateEmailList.get(1)).isEqualTo(thirduserEmail);
    }

    @Test
    public void testGetUpdateEmailListSubscribedEmailWithTextWithoutBLock() {
        subscriptionService.subscribe(secondUserEmail, firstUserEmail);
        blockEmailService.blockEmail(secondUserEmail, firstUserEmail);
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(firstUserEmail, "random text ");
        assertThat(updateEmailList).isEmpty();
    }

}
