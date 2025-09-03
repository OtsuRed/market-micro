package top.otsuland.product.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer amount;
    private Integer sellerId;
    private Integer state;
    private Integer want;
    private String sellerName;
}
