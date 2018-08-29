package spgroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FriendManagementService {

    Logger logger = LoggerFactory.getLogger(FriendManagementService.class);

    Map<String, List<String>> friendsMap = new HashMap<>();

    private void validateConnectFriendsData(String userEmail1, String userEmail2) {
        if(userEmail1 == null || userEmail2 == null) throw new  IllegalArgumentException("User email cannot be null");
        if(!isEmail(userEmail1)) throw new IllegalArgumentException("'"+userEmail1+"' is not a valid email.");
        if(!isEmail(userEmail2)) throw new IllegalArgumentException("'"+userEmail2+"' is not a valid email.");

    }

    public boolean connectFriends(String userEmail1, String userEmail2) {

        validateConnectFriendsData(userEmail1, userEmail2);

        if(userEmail1.equals(userEmail2)) {
            logger.info("[CONNECT_FRIENDS] [%s] %s", userEmail1, "both emails should be different.");
            return false;
        }

        //Add user data if absent
        friendsMap.putIfAbsent(userEmail1, new ArrayList<>());
        friendsMap.putIfAbsent(userEmail2, new ArrayList<>());

        //Make friends with each other
        friendsMap.get(userEmail1).add(userEmail2);
        friendsMap.get(userEmail2).add(userEmail1);

        return true;
    }

    private boolean isEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void validateGetFriendEmailsData(String userEmail) {
        if(null == userEmail) throw new IllegalArgumentException("UserEmail cannot be null");
        if(!isEmail(userEmail)) throw new IllegalArgumentException("'"+userEmail+"' is not a valid email.");
    }

    public List<String> getFriendEmails(String userEmail) {
        validateGetFriendEmailsData(userEmail);
        return friendsMap.get(userEmail);
    }

}
