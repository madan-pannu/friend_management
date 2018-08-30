package spgroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spgroup.common.Util.isEmail;

@Component
public class SubscriptionService {

    Map<String, List<String>> subscriptionMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    BlockEmailService blockEmailService;

    private void validateSubscribeData(String requestor, String target) {
        if(requestor == null || target == null) throw new  IllegalArgumentException("User email cannot be null");
        if(!isEmail(requestor)) throw new IllegalArgumentException("'"+requestor+"' is not a valid email.");
        if(!isEmail(target)) throw new IllegalArgumentException("'"+target+"' is not a valid email.");
    }

    public boolean subscribe(String requestor, String target) {
        validateSubscribeData(requestor, target);

        if(requestor.equals(target)) {
            logger.info("Provide Requestor and target are same.");
            return false;
        }

        if(blockEmailService.blockEmail(requestor, target)) {
            logger.info(requestor+" has blocked "+ target);
            return false;
        }

        if(blockEmailService.blockEmail(target, requestor)) {
            logger.info(target+" has blocked "+ requestor);
            return false;
        }


        //TODO - Might want to validate if both emails exists

        subscriptionMap.putIfAbsent(target, new ArrayList<>());
        subscriptionMap.get(target).add(requestor);

        return true;
    }

}
