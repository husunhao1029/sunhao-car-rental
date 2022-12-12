package org.sunhaolab.carrental.serviceImpls;

import org.springframework.stereotype.Service;
import org.sunhaolab.carrental.dao.CarsMapper;
import org.sunhaolab.carrental.dao.OrderMapper;
import org.sunhaolab.carrental.model.Model;
import org.sunhaolab.carrental.model.StockInfo;
import org.sunhaolab.carrental.services.CarsService;
import org.sunhaolab.carrental.utils.CustomValidationUtil;
import org.sunhaolab.carrental.utils.DataUtil;
import org.sunhaolab.carrental.utils.DateUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CarsServiceImpl implements CarsService {

    @Resource
    private CarsMapper carsMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<StockInfo> getAvailableCars(String startTime, String endTime) {
        List<StockInfo> res = new ArrayList();
        if (null == startTime || startTime.equals("") || null == endTime || endTime.equals("")) {
            return res;
        }

        CustomValidationUtil.timeValidation(startTime, endTime);

        startTime = DateUtil.stampToDate(startTime);
        endTime = DateUtil.stampToDate(endTime);

        List<Model> modelList = carsMapper.selectModelCount();
        List<Map<String, Integer>> occupiedModelList = orderMapper.countOccupiedNum(startTime, endTime);
        Map<String, Integer> occupiedModelMap = DataUtil.mapListToMap(occupiedModelList, "model", "counter");
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
