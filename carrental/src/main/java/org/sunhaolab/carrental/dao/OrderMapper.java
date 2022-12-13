package org.sunhaolab.carrental.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface OrderMapper {
    List<Map<String, Object>> countOccupiedNum(String startTime, String endTime);

    List<Map<String, String>> selectOrderList();

    List<String> selectOccupiedCars(String startTime, String endTime, String model);

    Integer generateOrder(Integer carId, String model, String startTime, String endTime);
}
