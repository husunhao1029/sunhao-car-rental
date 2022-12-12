package org.sunhaolab.carrental.serviceImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.exception.ServiceException;
import org.sunhaolab.carrental.model.Model;
import org.sunhaolab.carrental.model.StockInfo;
import org.sunhaolab.carrental.services.CarsService;
import org.sunhaolab.carrental.utils.CustomValidationUtil;
import org.sunhaolab.carrental.utils.DataUtil;
import org.sunhaolab.carrental.utils.DateUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarsServiceImpl implements CarsService {

    Logger logger = LoggerFactory.getLogger(CarsServiceImpl.class);
    @Resource
    private CarsMapper carsMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<StockInfo> getAvailableCars(String startTime, String endTime) {
        List<StockInfo> res = new ArrayList();
        if (null == startTime || startTime.equals("") || null == endTime || endTime.equals("")) {
            logger.error("Invalid input time, startTime is: {}, and endTime is :{}", startTime, endTime);
            return res;
        }

        CustomValidationUtil.timeValidation(startTime, endTime);

        startTime = DateUtil.stampToDate(startTime);
        endTime = DateUtil.stampToDate(endTime);

        List<Model> modelList = carsMapper.selectModelCount();
        List<Map<String, Integer>> occupiedModelList = orderMapper.countOccupiedNum(startTime, endTime);
        Map<String, Integer> occupiedModelMap;
        try {
            occupiedModelMap = DataUtil.mapListToMap(occupiedModelList, "model", "counter");
        } catch (Exception e) {
            logger.error("Convert occupiedModelList to map failed, list value is: {}", occupiedModelList.toString());
            throw new ServiceException("Convert occupiedModelList to map failed.");
        }
        modelList.forEach(
                model -> {
                    StockInfo stockInfo = new StockInfo();
                    stockInfo.setLabel(model.getModel());
                    stockInfo.setValue(model.getModel());
                    Integer occupiedCount =
                            occupiedModelMap.get(model.getModel()) == null ? 0 : occupiedModelMap.get(model.getModel());
                    stockInfo.setAvailable(model.getCounter() - occupiedCount);
                    if (stockInfo.getAvailable() > 0) {
                        res.add(stockInfo);
                    }
                }
        );

        return res;
    }

}
