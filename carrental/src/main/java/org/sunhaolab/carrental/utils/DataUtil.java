package org.sunhaolab.carrental.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {

    public static Map<String, Integer> mapListToMap(List<Map<String, Integer>> list, String keyField, String valueField) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<String, Integer> res = new HashMap<>(list.size());
        list.forEach(
                record -> res.put(String.valueOf(record.get(keyField)),
                        Integer.parseInt(String.valueOf(record.get(valueField))))
        );

        return res;

    }

}
