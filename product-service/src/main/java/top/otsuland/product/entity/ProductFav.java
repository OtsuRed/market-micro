package top.otsuland.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("product_fav")
public class ProductFav {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer uid;

    @TableField
    private Integer pid;

}
