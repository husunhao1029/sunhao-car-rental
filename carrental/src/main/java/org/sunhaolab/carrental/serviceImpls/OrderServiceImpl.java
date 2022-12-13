package org.sunhaolab.carrental.serviceImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.exception.ServiceException;
import org.sunhaolab.carrental.model.Car;
import org.sunhaolab.carrental.model.Order;
import org.sunhaolab.carrental.services.OrderService;
import org.sunhaolab.carrental.utils.CustomValidationUtil;
import org.sunhaolab.carrental.utils.DateUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    OrderMapper orderMapper;

    @Resource
    CarsMapper carsMapper;


    @Override
    public List<Map<String, String>> queryOrderList() {
        return orderMapper.selectOrderList();
    }

    @Override
    public Integer placeOrder(Order order) {

        //param check: time
        if (null == order.getStartTime() || order.getStartTime().equals("")
                || null == order.getEndTime() || order.getEndTime().equals("")) {
            logger.error("Invalid input time, startTime is: {}, and endTime is :{}", order.getStartTime(), order.getEndTime());
            return 0;
        }
        CustomValidationUtil.timeValidation(order.getStartTime(), order.getEndTime());

        //param check: car model
        if(null == order.getCarModel() || order.getCarModel().equals("")) {
            logger.error("Invalid car model, value is: {}", order.getCarModel());
            throw new ServiceException("Car model cannot be empty!");
        }

        String model = order.getCarModel();
        String startTime = DateUtil.stampToDate(order.getStartTime());
        String endTime = DateUtil.stampToDate(order.getEndTime());
        List<Car> cars = carsMapper.selectCarsByModel(model);

        //param check: Is car model exist?
        if(cars.size() == 0) {
            throw new ServiceException("There is no car of the model: " + model);
        }

        List<String> occupiedCarIds = orderMapper.selectOccupiedCars(startTime, endTime, model);

        if(occupiedCarIds.size() == 0) {
            Car car = cars.get(0);
            return orderMapper.generateOrder(car.getId(), model, startTime, endTime);
        }

        for (Car car : cars) {
            if (!occupiedCarIds.contains(String.valueOf(car.getId()))) {
                return orderMapper.generateOrder(car.getId(), model, startTime, endTime);
            }
        }
        return 0;
    }
}
