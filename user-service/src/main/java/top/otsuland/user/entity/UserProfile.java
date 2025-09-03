package top.otsuland.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_profile")
public class UserProfile {
    
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer userId;

    @TableField
    private String email;

    @TableField
    private String gender; // 男，女，未公开 TODO：根据数字转换成汉字

    @TableField
    private String prof;

}
