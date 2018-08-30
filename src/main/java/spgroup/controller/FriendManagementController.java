package spgroup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spgroup.common.Result;
import spgroup.service.BlockEmailService;
import spgroup.service.FriendManagementService;
import spgroup.service.SubscriptionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spgroup")
public class FriendManagementController {

    @Autowired
    FriendManagementService friendManagementService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    BlockEmailService blockEmailService;

    @RequestMapping(
            value = "/connect_friends",
            method = RequestMethod.POST)
    public Result connectFriends(@RequestBody Map<String, List<String>> newFriendData)
            throws Exception {

        //Validate data
        if(null == newFriendData) throw new IllegalArgumentException("Missing Connect friend info.");
        List<String> emailList = newFriendData.get("friends");

        //validate content starts
        if(null == emailList) throw new IllegalArgumentException("Missing friends info.");
        if(emailList.size() != 2) throw new IllegalArgumentException("Only 2 emails are supported");
        //validate content ends

        //Add data to DS
        boolean isConncted = friendManagementService.connectFriends(emailList.get(0), emailList.get(1));

        Result result = new Result();
        result.setSuccess(isConncted);
        return result;
    }


    @RequestMapping(
            value = "/fetch_friends",
            method = RequestMethod.POST)
    public Result getFriends(@RequestBody Map<String, String> emailPayload) {

        if(null == emailPayload) throw new IllegalArgumentException("Missing EemailPayload info.");
        if(!emailPayload.containsKey("email")) throw new IllegalArgumentException("Missing email info");

        List<String> friends = friendManagementService.getFriendEmails(emailPayload.get("email"));
        Result result = new Result();
        result.setSuccess(true);

        Map<String, Object> map = new HashMap<>();
        if(friends == null) {
            map.put("count", 0);
        } else {
            map.put("count", friends.size());
            map.put("friends", friends);
        }

        result.setData(map);
        return result;
    }

    @RequestMapping(
            value = "/fetch_common_friends",
            method = RequestMethod.POST)
    public Result getCommonFriends(@RequestBody Map<String, List<String>> emails) {


        //Validate data
        if(null == emails) throw new IllegalArgumentException("Missing Emails info.");
        List<String> emailList = emails.get("friends");

        //validate content starts
        if(null == emailList) throw new IllegalArgumentException("Missing Email info.");
        if(emailList.size() != 2) throw new IllegalArgumentException("Only 2 emails are supported");



        List<String> commonFriends = friendManagementService.getCommonFriends(emailList.get(0), emailList.get(1));
        Result result = new Result();
        result.setSuccess(true);

        Map<String, Object> map = new HashMap<>();
        if(commonFriends == null) {
            map.put("count", 0);
        } else {
            map.put("count", commonFriends.size());
            map.put("friends", commonFriends);
        }

        result.setData(map);
        return result;
    }

    @RequestMapping(
            value = "/subscribe_email",
            method = RequestMethod.POST)
    public Result subscribeEmail(@RequestBody Map<String, String> subscriptionPayload) {
        //Validate data
        if(null == subscriptionPayload) throw new IllegalArgumentException("Missing SubscriptionPayload info.");
        if(!subscriptionPayload.containsKey("requestor")) throw new IllegalArgumentException("Missing Requestor info");
        if(!subscriptionPayload.containsKey("target")) throw new IllegalArgumentException("Missing Requestor info");

        //validate content starts
        boolean isSubscribed = subscriptionService.subscribe(subscriptionPayload.get("requestor"), subscriptionPayload.get("target"));
        Result result = new Result();
        result.setSuccess(isSubscribed);
        return result;

    }

    @RequestMapping(
            value = "/block_email",
            method = RequestMethod.POST)
    public Result blockEmail(@RequestBody Map<String, String> blockEmailload) {
        //Validate data
        if(null == blockEmailload) throw new IllegalArgumentException("Missing SubscriptionPayload info.");
        if(!blockEmailload.containsKey("requestor")) throw new IllegalArgumentException("Missing Requestor info");
        if(!blockEmailload.containsKey("target")) throw new IllegalArgumentException("Missing Requestor info");

        //validate content starts
        boolean isBlocked = blockEmailService.blockEmail(blockEmailload.get("requestor"), blockEmailload.get("target"));
        Result result = new Result();
        result.setSuccess(isBlocked);
        return result;

    }

    @RequestMapping(
            value = "/fetch_update_emails",
            method = RequestMethod.POST)
    public Result fetchSubscribedEmails(@RequestBody Map<String, String> updateEmailsPayload) {
        //Validate data
        if(null == updateEmailsPayload) throw new IllegalArgumentException("Missing UpdateEmailsPayload info.");
        if(!updateEmailsPayload.containsKey("sender")) throw new IllegalArgumentException("Missing sender info");


        //validate content starts
        List<String> updateEmailList = friendManagementService.getUpdateEmailList(updateEmailsPayload.get("sender"), updateEmailsPayload.get("text"));
        Result result = new Result();
        result.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("recipients", updateEmailList);
        result.setData(map);
        return result;

    }

}
