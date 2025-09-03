package top.otsuland.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("product_pic")
public class ProductPic {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer productId;

    @TableField
    private Integer kind; // 1 主图 2 副图

    @TableField
    private byte[] picture;
}
