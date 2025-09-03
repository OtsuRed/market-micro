package top.otsuland.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_pic")
public class UserPic {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer userId;

    @TableField
    private byte[] picture;

    @TableField
    private byte[] pictureNarrow;

}
