package spgroup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spgroup.common.Result;
import spgroup.service.FriendManagementService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spgroup")
public class FriendManagementController {

    @Autowired
    FriendManagementService friendManagementService;



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
            method = RequestMethod.GET)
    public Result getFriends(@RequestParam(name="email", required=true)  String userEmail) {
        List<String> friends = friendManagementService.getFriendEmails(userEmail);
        Result result = new Result();
        result.setSuccess(true);

        Map<String, Object> map = new HashMap<>();
        if(friends != null) {
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
            method = RequestMethod.GET)
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
        if(commonFriends != null) {
            map.put("count", 0);
        } else {
            map.put("count", commonFriends.size());
            map.put("friends", commonFriends);
        }

        result.setData(map);
        return result;
    }

}
