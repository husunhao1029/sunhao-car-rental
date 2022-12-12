package org.sunhaolab.carrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sunhaolab.carrental.exception.ServiceException;
import org.sunhaolab.carrental.model.ResponseEntity;
import org.sunhaolab.carrental.model.StockInfo;
import org.sunhaolab.carrental.services.CarsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CarsController {

    @Resource
    CarsService carsService;

    @GetMapping(path = "/cars")
    @ResponseBody
    public ResponseEntity getAvailableCars (
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime
    ) {
        System.out.println("startTime is: '" + startTime + "'");
        ResponseEntity resp = new ResponseEntity();
        resp.setStatus(0);
        resp.setMsg("ok");
        Map<String, Object>  map = new HashMap<>();
        List<StockInfo> res = carsService.getAvailableCars(startTime, endTime);
        map.put("options", res);
        resp.setData(map);
        return resp;
    }

}
