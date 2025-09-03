package top.otsuland.product.dto;

import lombok.Data;

@Data
public class ProductPicMetaDTO {
    private Integer id;
    private Integer kind;

    public ProductPicMetaDTO(Integer id, Integer kind) {
        this.id = id;
        this.kind = kind;
    }
}
