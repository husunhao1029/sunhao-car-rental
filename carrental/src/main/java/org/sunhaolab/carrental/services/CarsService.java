package org.sunhaolab.carrental.services;

import org.springframework.stereotype.Service;
import org.sunhaolab.carrental.model.StockInfo;

import java.util.List;

@Service
public interface CarsService {

    List<StockInfo> getAvailableCars(String startTime, String endTime);

}
