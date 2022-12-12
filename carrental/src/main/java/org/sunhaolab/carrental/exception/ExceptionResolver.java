package org.sunhaolab.carrental.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sunhaolab.carrental.model.ResponseEntity;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity handleException(ServiceException e) {
        ResponseEntity resp = new ResponseEntity();
        resp.setStatus(-1);
        resp.setMsg(e.getMessage());
        resp.setData(new HashMap());
        return resp;
    }
}
