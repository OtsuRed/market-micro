package top.otsuland.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("a_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private String username;

    @TableField
    private String pwd;
    
    @TableField
    private String tel;

    @TableField
    private Integer follow;

    @TableField
    private Integer fans;

}
