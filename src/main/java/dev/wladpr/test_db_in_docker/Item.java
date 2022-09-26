package dev.wladpr.test_db_in_docker;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

    private Integer id;
    private String model;
    private String color;
    private Integer ram;
    private Integer hd;
    private Integer price;


}
