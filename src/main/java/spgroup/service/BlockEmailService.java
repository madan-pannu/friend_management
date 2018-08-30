package spgroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static spgroup.common.Util.isEmail;

@Component
public class BlockEmailService {

    Map<String, List<String>> blockEmailMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(BlockEmailService.class);


    public boolean blockEmail(String requestor, String target) {
        validateBlockEmailData(requestor, target);

        if(requestor.equals(target)) {
            logger.info("Provide Requestor and target are same.");
            return false;
        }

        blockEmailMap.putIfAbsent(requestor, new ArrayList<>());
        blockEmailMap.get(requestor).add(target);
        return true;

    }

    private void validateBlockEmailData(String requestor, String target) {
        if(requestor == null || target == null) throw new  IllegalArgumentException("User email cannot be null");
        if(!isEmail(requestor)) throw new IllegalArgumentException("'"+requestor+"' is not a valid email.");
        if(!isEmail(target)) throw new IllegalArgumentException("'"+target+"' is not a valid email.");
    }

    public boolean isEmailBlocked(String requestor, String blockedEmail) {
        List<String> blockedEmailList = blockEmailMap.get(requestor);
        if(blockedEmailList == null) return false;
        Optional<String> optional = blockedEmailList.stream().filter(blockedEmail::equals).findFirst();
        return optional.isPresent();
    }
}
