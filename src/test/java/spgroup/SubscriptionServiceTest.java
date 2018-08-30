package spgroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spgroup.service.FriendManagementService;
import spgroup.service.SubscriptionService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    private String firstUserEmail = "user1@gmail.com";
    private String secondUserEmail = "user2@gmail.com";

    @Before
    public void setup() {
    }

    @After
    public void tearDown(){
        subscriptionService.clear();
    }

    //Subscribe email test
    @Test
    public void testSubscribeNullEmail() {
        boolean isSubscribed;
        try {
            isSubscribed = subscriptionService.subscribe(null, null);
        } catch (IllegalArgumentException e) {
            isSubscribed = false;
        }
        assertThat(isSubscribed).isEqualTo(false);
    }

    @Test
    public void testSubscribeWrongEmail() {
        boolean isSubscribed;
        try {
            isSubscribed = subscriptionService.subscribe("user1", "user2");
        } catch (IllegalArgumentException e) {
            isSubscribed = false;
        }
        assertThat(isSubscribed).isEqualTo(false);
    }

    @Test
    public void testSubscribSameEmail() {
        boolean isSubscribed = subscriptionService.subscribe(firstUserEmail, firstUserEmail);
        assertThat(isSubscribed).isEqualTo(false);
    }

    @Test
    public void testSubscribeEmail() {
        boolean isSubscribed = subscriptionService.subscribe(firstUserEmail, secondUserEmail);
        assertThat(isSubscribed).isEqualTo(true);
    }
}
