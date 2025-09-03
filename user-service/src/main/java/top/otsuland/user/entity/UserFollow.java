package top.otsuland.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_follow")
public class UserFollow {
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 被关注者
    @TableField
    private Integer followeeId;

    // 关注别人的人
    @TableField
    private Integer followerId;
}
