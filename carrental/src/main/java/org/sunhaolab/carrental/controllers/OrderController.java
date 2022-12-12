package org.sunhaolab.carrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.sunhaolab.carrental.model.Order;
import org.sunhaolab.carrental.model.ResponseEntity;
import org.sunhaolab.carrental.services.OrderService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    OrderService orderService;

    @GetMapping(path = "/orders")
    @ResponseBody
    public ResponseEntity queryOrders() {
        logger.info("Start querying orders...");
        ResponseEntity resp = new ResponseEntity();
        resp.setStatus(0);
        resp.setMsg("ok");
        Map<String,Object> data = new HashMap();
        List<Map<String, String>> orderList = orderService.queryOrderList();
        data.put("rows",orderList);
        data.put("count",orderList.size());
        resp.setData(data);
        logger.info("Query orders finished.");
        return resp;
    }

    @PostMapping(path = "/orders")
    @ResponseBody
    public ResponseEntity placeOrder(
            @RequestBody Order order
    ) {
        logger.info("Start place order...");
        Integer res = orderService.placeOrder(order);
        ResponseEntity resp = new ResponseEntity();
        if(res > 0) {
            resp.setStatus(0);
            resp.setMsg("generate order success, total:" + res);
            resp.setData("ok");
        } else {
            resp.setStatus(-1);
            resp.setMsg("generate order failed, total:" + 0);
            resp.setData("failed");
        }
        logger.info("Place order finished.");
        return resp;
    }

}
