package top.otsuland.product.dto;

import lombok.Data;

@Data
public class ProductMeta {
    private Integer id;
    private String username;
    private String name;
    private String price;
    private Integer amount;
}
