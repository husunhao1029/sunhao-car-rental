package org.sunhaolab.carrental.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Order {

    private Integer id;

    @JsonAlias("car_id")
    private Integer carId;

    @JsonAlias("car_model")
    private String carModel;

    @JsonAlias("start_time")
    private String startTime;

    @JsonAlias("end_time")
    private String endTime;

}
