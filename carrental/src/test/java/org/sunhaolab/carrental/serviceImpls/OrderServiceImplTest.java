package org.sunhaolab.carrental.serviceImpls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.exception.ServiceException;
import org.sunhaolab.carrental.model.Car;
import org.sunhaolab.carrental.model.Order;
import org.sunhaolab.carrental.services.OrderService;
import org.sunhaolab.carrental.utils.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CarsMapper carsMapper;

    OrderServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryOrderList() {
        List<Map<String, String>> list = new ArrayList();
        list.add(new HashMap());
        Mockito.when(orderMapper.selectOrderList()).thenReturn(list);
        List<Map<String, String>> resList = orderService.queryOrderList();
        Assertions.assertEquals(resList, list);
    }

    @Test
    void testPlaceOrderStartTimeIsNull() {
        Order order = new Order();
        order.setStartTime(null);
        order.setEndTime("1671120000");
        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 0);
    }

    @Test
    void testPlaceOrderStartTimeIsEmptyString() {
        Order order = new Order();
        order.setStartTime("");
        order.setEndTime("1671120000");
        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 0);
    }

    @Test
    void testPlaceOrderEndTimeIsNull() {
        Order order = new Order();
        order.setStartTime("1671120000");
        order.setEndTime(null);
        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 0);
    }

    @Test
    void testPlaceOrderEndTimeIsEmptyString() {
        Order order = new Order();
        order.setStartTime("1671120000");
        order.setEndTime("");
        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 0);
    }

    @Test
    void testTimeInvalid() {
        Order order = new Order();
        order.setStartTime("aaa");
        order.setEndTime("1671120000");
        try {
            Integer res = orderService.placeOrder(order);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals(
                    "Invalid input, please check startTime and endTime are both timestamp format.",
                    e.getMessage());
        }
    }

    @Test
    void testCarModelIsNull() {
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        try {
            Integer res = orderService.placeOrder(order);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals("Car model cannot be empty!", e.getMessage());
        }
    }

    @Test
    void testCarModelIsEmpty() {
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        order.setCarModel("");
        try {
            Integer res = orderService.placeOrder(order);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals("Car model cannot be empty!", e.getMessage());
        }
    }

    @Test
    void testCarModelIsNotExist() {
        List<Car> list = new ArrayList();
        Mockito.when(carsMapper.selectCarsByModel("not_exist")).thenReturn(list);
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        order.setCarModel("not_exist");
        try {
            Integer res = orderService.placeOrder(order);
            fail("Expected ServiceException here");
        } catch (Exception e) {
            Assertions.assertEquals("There is no car of the model: not_exist", e.getMessage());
        }
    }

    @Test
    void testCarsIsOccupied() {
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        order.setCarModel("testModel");

        List<Car> list = new ArrayList();
        Car car = new Car();
        car.setId(1);
        car.setModel("testModel");
        list.add(car);
        List<String> occupiedCarIds = new ArrayList();
        occupiedCarIds.add("1");
        Mockito.when(carsMapper.selectCarsByModel("testModel")).thenReturn(list);
        Mockito.when(orderMapper.selectOccupiedCars(order.getStartTime(), order.getEndTime(), order.getCarModel()))
                .thenReturn(occupiedCarIds);

        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 0);
    }

    @Test
    void testCarsIsNotOccupied() {
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        order.setCarModel("testModel");
        String startTimestamp = order.getStartTime();
        String endTimestamp = order.getEndTime();
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Car> list = new ArrayList();
        Car car = new Car();
        car.setId(1);
        car.setModel("testModel");
        list.add(car);
        List<String> occupiedCarIds = new ArrayList();
        occupiedCarIds.add("1");
        Mockito.when(carsMapper.selectCarsByModel("testModel")).thenReturn(list);
        Mockito.when(orderMapper.selectOccupiedCars(startTimestamp, endTimestamp, order.getCarModel()))
                .thenReturn(occupiedCarIds);
        Mockito.when(orderMapper.generateOrder(
                        car.getId(), order.getCarModel(), startTime, endTime))
                .thenReturn(1);

        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 1);
    }

    @Test
    void testCarsWithNoCarOccupied() {
        Order order = new Order();
        order.setStartTime("1671465600");
        order.setEndTime("1671552000");
        order.setCarModel("testModel");
        String startTimestamp = order.getStartTime();
        String endTimestamp = order.getEndTime();
        String startTime = DateUtil.stampToDate(startTimestamp);
        String endTime = DateUtil.stampToDate(endTimestamp);

        List<Car> list = new ArrayList();
        Car car = new Car();
        car.setId(1);
        car.setModel("testModel");
        list.add(car);
        List<String> occupiedCarIds = new ArrayList();
        Mockito.when(carsMapper.selectCarsByModel("testModel")).thenReturn(list);
        Mockito.when(orderMapper.selectOccupiedCars(startTimestamp, endTimestamp, order.getCarModel()))
                .thenReturn(occupiedCarIds);
        Mockito.when(orderMapper.generateOrder(
                car.getId(), order.getCarModel(), startTime, endTime))
                .thenReturn(1);

        Integer res = orderService.placeOrder(order);
        Assertions.assertTrue(res == 1);
    }


}