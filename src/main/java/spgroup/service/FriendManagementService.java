package spgroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spgroup.common.Util;

import java.util.*;
import java.util.stream.Collectors;

import static spgroup.common.Util.isEmail;

@Component
public class FriendManagementService {

    Logger logger = LoggerFactory.getLogger(FriendManagementService.class);

    Map<String, List<String>> friendsMap = new HashMap<>();

    @Autowired
    BlockEmailService blockEmailService;

    @Autowired
    SubscriptionService subscriptionService;


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

        if(blockEmailService.isEmailBlocked(userEmail1, userEmail2)) {
            logger.info(userEmail1+" has blocked "+ userEmail2);
            return false;
        }

        if(blockEmailService.isEmailBlocked(userEmail2, userEmail1)) {
            logger.info(userEmail2+" has blocked "+ userEmail1);
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



    private void validateGetFriendEmailsData(String userEmail) {
        if(null == userEmail) throw new IllegalArgumentException("UserEmail cannot be null");
        if(!isEmail(userEmail)) throw new IllegalArgumentException("'"+userEmail+"' is not a valid email.");
    }

    public List<String> getFriendEmails(String userEmail) {
        validateGetFriendEmailsData(userEmail);
        return friendsMap.get(userEmail);
    }

    public List<String> getCommonFriends(String userEmail1, String userEmail2) {
        validateConnectFriendsData(userEmail1, userEmail2);
        if(userEmail1.equals(userEmail2)) {
            logger.info("[CONNECT_FRIENDS] [%s] %s", userEmail1, "both emails should be different.");
            return friendsMap.get(userEmail1);
        }

        List<String> user1Friends = friendsMap.get(userEmail1);
        List<String> user2Friends = friendsMap.get(userEmail2);

        return getCommonData(user1Friends, user2Friends);
    }

    private List<String> getCommonData(List<String> list1, List<String> list2) {
        if(list1 == null || list2 == null) return null;
        return list1.stream().filter(list2::contains).collect(Collectors.toList());
    }


    public List<String> getUpdateEmailList(String requestor, String text) {
        List<String> updateEmailList = new ArrayList<>();

        List<String> friendsList = friendsMap.get(requestor);
        if(friendsList != null) updateEmailList.addAll(friendsList);

        List<String> subscribedList = subscriptionService.getSubscribedList(requestor);
        if(subscribedList != null) updateEmailList.addAll(subscribedList);

        List<String> mentionedEmailsList = getEmailsFromText(text);
        if(mentionedEmailsList != null) updateEmailList.addAll(mentionedEmailsList);

        return updateEmailList.stream().filter(userEmail -> !blockEmailService.isEmailBlocked(userEmail, requestor)).collect(Collectors.toList());

    }

    private List<String> getEmailsFromText(String text) {
        if(text == null) return null;
        return Arrays.stream(text.split(" ")).filter(Util::isEmail).distinct().collect(Collectors.toList());
    }


    public void clear() {
        friendsMap.clear();
    }



}
