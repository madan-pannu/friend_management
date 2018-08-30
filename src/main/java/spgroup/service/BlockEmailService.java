package spgroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static spgroup.common.Util.isEmail;

@Component
public class BlockEmailService {

    private Map<String, List<String>> blockEmailFrom = new HashMap<>();
    private Map<String, List<String>> blockEmailTo = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(BlockEmailService.class);


    public boolean blockEmail(String requestor, String target) {
        validateBlockEmailData(requestor, target);

        if(requestor.equals(target)) {
            logger.info("Provide Requestor and target are same.");
            return false;
        }

        blockEmailFrom.putIfAbsent(requestor, new ArrayList<>());
        blockEmailFrom.get(requestor).add(target);

        blockEmailTo.putIfAbsent(target, new ArrayList<>());
        blockEmailTo.get(target).add(requestor);

        return true;

    }

    private void validateBlockEmailData(String requestor, String target) {
        if(requestor == null || target == null) throw new  IllegalArgumentException("User email cannot be null");
        if(!isEmail(requestor)) throw new IllegalArgumentException("'"+requestor+"' is not a valid email.");
        if(!isEmail(target)) throw new IllegalArgumentException("'"+target+"' is not a valid email.");
    }

    public boolean isEmailBlocked(String requestor, String blockedEmail) {
        List<String> blockedEmailList = blockEmailFrom.get(requestor);
        if(blockedEmailList == null) return false;
        Optional<String> optional = blockedEmailList.stream().filter(blockedEmail::equals).findFirst();
        return optional.isPresent();
    }

    public List<String> getBlockedEmailTo(String email) {
        return blockEmailTo.get(email);
    }

    public void clear() {
        blockEmailTo.clear();
        blockEmailFrom.clear();
    }
}
