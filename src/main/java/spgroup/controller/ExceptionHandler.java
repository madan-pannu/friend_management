package spgroup.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandler extends ResponseEntityExceptionHandler{

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public void handleException(HttpServletResponse httpServletResponse, Exception ex) throws IOException {

        Map<String, String> exceptionMap = new HashMap<>();
        exceptionMap.put("success", "false");
        exceptionMap.put("message", ex.getMessage());
        httpServletResponse.getWriter().print(exceptionMap);
    }


}


