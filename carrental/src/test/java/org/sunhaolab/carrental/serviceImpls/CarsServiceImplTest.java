package org.sunhaolab.carrental.serviceImpls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.model.StockInfo;
import org.sunhaolab.carrental.services.CarsService;
import org.sunhaolab.carrental.services.OrderService;

import java.util.ArrayList;
import java.util.List;


class CarsServiceImplTest {

    @InjectMocks
    private CarsService carsService = new CarsServiceImpl();

    @Mock
    private CarsMapper carsMapper;

    @Mock
    private OrderMapper orderMapper;

    CarsServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableCarsStartTimeIsNull() {
        List<StockInfo> res = new ArrayList();
        String startTime = null;
        String endTime = "1671120000";
        res = carsService.getAvailableCars(startTime, endTime);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void testGetAvailableCarsStartTimeIsEmpty() {
        List<StockInfo> res = new ArrayList();
        String startTime = "";
        String endTime = "1671120000";
        res = carsService.getAvailableCars(startTime, endTime);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void testGetAvailableCarsEndTimeIsNull() {
        List<StockInfo> res = new ArrayList();
        String startTime = "1671120000";
        String endTime = null;
        res = carsService.getAvailableCars(startTime, endTime);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void testGetAvailableCarsEndTimeIsEmpty() {
        List<StockInfo> res = new ArrayList();
        String startTime = "1671120000";
        String endTime = "";
        res = carsService.getAvailableCars(startTime, endTime);
        Assertions.assertTrue(res.isEmpty());
    }

    
}