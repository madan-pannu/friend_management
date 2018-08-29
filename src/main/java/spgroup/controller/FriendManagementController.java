package spgroup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spgroup.common.Result;
import spgroup.service.FriendManagementService;

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



}
