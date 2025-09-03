package top.otsuland.product.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductCreateReq {
    private String name;
    private BigDecimal price;
    private Integer amount;
    private String prof;
    private List<Integer> category;
}
