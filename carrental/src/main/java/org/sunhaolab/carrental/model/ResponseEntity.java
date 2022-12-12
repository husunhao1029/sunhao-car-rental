package org.sunhaolab.carrental.model;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ResponseEntity {
    private Integer status;
    private String msg;
    private Object data;
}
