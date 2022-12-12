package org.sunhaolab.carrental.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.sunhaolab.carrental.model.Car;
import org.sunhaolab.carrental.model.Model;

import java.util.List;

@Repository
@Mapper
public interface CarsMapper {
    List<Model> selectModelCount();

    List<Car> selectCarsByModel(String model);
}
