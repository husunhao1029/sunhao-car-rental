package org.sunhaolab.carrental.serviceImpls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.model.Model;
import org.sunhaolab.carrental.model.StockInfo;
import org.sunhaolab.carrental.services.CarsService;
import org.sunhaolab.carrental.utils.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;


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

    @Test
    void testTimeInvalid() {
        String startTime = "1671120000";
        String endTime = "aaa";
        try {
            List<StockInfo> res = carsService.getAvailableCars(startTime, endTime);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals(
                    "Invalid input, please check startTime and endTime are both timestamp format.",
                    e.getMessage());
        }
    }

    @Test
    void testNoCarsInStock() {
        String startTime = "1671465600";
        String endTime = "1671552000";
        List<Model> mockModel = new ArrayList();
        Mockito.when(carsMapper.selectModelCount()).thenReturn(mockModel);
        try {
            List<StockInfo> res = carsService.getAvailableCars(startTime, endTime);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals("No available cars.", e.getMessage());
        }
    }

    @Test
    void testCarsInStockAndNotBeOccupied() {
        String startTimestamp = "1671465600";
        String endTimestamp = "1671552000";
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Model> mockModel = new ArrayList();
        Model model = new Model();
        model.setModel("testModel");
        model.setCounter(2);
        mockModel.add(model);
        Mockito.when(carsMapper.selectModelCount()).thenReturn(mockModel);
        Mockito.when(orderMapper.countOccupiedNum(startTime, endTime)).thenReturn(new ArrayList());

        List<StockInfo> res = carsService.getAvailableCars(startTimestamp, endTimestamp);
        Assertions.assertTrue(!res.isEmpty());
    }

    @Test
    void testCarsInStockAndInvalidOccupiedModelListValue() {
        String startTimestamp = "1671465600";
        String endTimestamp = "1671552000";
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Model> mockModel = new ArrayList();
        Model model = new Model();
        model.setModel("testModel");
        model.setCounter(2);
        mockModel.add(model);
        Mockito.when(carsMapper.selectModelCount()).thenReturn(mockModel);
        List<Map<String, Object>> mockOccupiedList = new ArrayList();
        Map<String, Object> mockOccupiedMap = new HashMap();
        mockOccupiedMap.put("model", "testModel");
        mockOccupiedMap.put("counter", "aaa");

        mockOccupiedList.add(mockOccupiedMap);

        Mockito.when(orderMapper.countOccupiedNum(startTime, endTime)).thenReturn(mockOccupiedList);

        try {
            List<StockInfo> res = carsService.getAvailableCars(startTimestamp, endTimestamp);
        } catch (Exception e) {
            Assertions.assertEquals("Convert occupiedModelList to map failed.", e.getMessage());
        }

    }

    @Test
    void testCarsInStockAndAllBeOccupied() {
        String startTimestamp = "1671465600";
        String endTimestamp = "1671552000";
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Model> mockModel = new ArrayList();
        Model model = new Model();
        model.setModel("testModel");
        model.setCounter(2);
        mockModel.add(model);
        Mockito.when(carsMapper.selectModelCount()).thenReturn(mockModel);
        List<Map<String, Object>> mockOccupiedList = new ArrayList();
        Map<String, Object> mockOccupiedMap = new HashMap();
        mockOccupiedMap.put("model", "testModel");
        mockOccupiedMap.put("counter", 2);

        mockOccupiedList.add(mockOccupiedMap);

        Mockito.when(orderMapper.countOccupiedNum(startTime, endTime)).thenReturn(mockOccupiedList);

        List<StockInfo> res = carsService.getAvailableCars(startTimestamp, endTimestamp);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void testCarsInStockAndPartOfThemBeOccupied() {
        String startTimestamp = "1671465600";
        String endTimestamp = "1671552000";
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Model> mockModel = new ArrayList();
        Model model = new Model();
        model.setModel("testModel");
        model.setCounter(2);
        mockModel.add(model);
        Mockito.when(carsMapper.selectModelCount()).thenReturn(mockModel);
        List<Map<String, Object>> mockOccupiedList = new ArrayList();
        Map<String, Object> mockOccupiedMap = new HashMap();
        mockOccupiedMap.put("model", "testModel");
        mockOccupiedMap.put("counter", 1);

        mockOccupiedList.add(mockOccupiedMap);

        Mockito.when(orderMapper.countOccupiedNum(startTime, endTime)).thenReturn(mockOccupiedList);

        List<StockInfo> res = carsService.getAvailableCars(startTimestamp, endTimestamp);
        Assertions.assertTrue(!res.isEmpty());
    }
}