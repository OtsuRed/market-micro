package top.otsuland.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private String name;

    @TableField
    private BigDecimal price;

    @TableField
    private Integer amount;

    @TableField
    private String prof;

    @TableField
    private Integer sellerId;

    @TableField
    private String sellerName;

    // MyBatis-Plus 自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 有权限的属性
    @TableField
    private Integer state; // 默认为 0 销售中 1 已售罄 2 已下架 3 已删除

    @TableField
    private Integer want; // 默认为 0，显示几人想要。
}