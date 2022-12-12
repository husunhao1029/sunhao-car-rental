package org.sunhaolab.carrental.services;

import org.springframework.stereotype.Service;
import org.sunhaolab.carrental.model.Order;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {

    List<Map<String, String>> queryOrderList();

    Integer placeOrder(Order order);
}
