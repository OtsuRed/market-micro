package top.otsuland.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.user.dto.UserFollowResp;
import top.otsuland.user.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户列表
     * @return
     */
    List<User> list();

    User selectByUsername(String username);

    User selectByTel(String tel);
    int updatePwdById(@Param("id") Integer id, @Param("pwd") String pwd);

    int updateTelById(@Param("id") Integer id, @Param("tel") String tel);

    int updateUsernameById(@Param("id") Integer id, @Param("username") String username);
    String selectUsernameById(Integer id);

    List<UserFollowResp> selectFolloweesByUid(Integer uid);

    List<UserFollowResp> selectFollowersByUid(Integer uid);
}
