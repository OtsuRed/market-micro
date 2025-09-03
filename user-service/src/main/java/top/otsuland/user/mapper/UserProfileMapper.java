package top.otsuland.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.user.entity.UserProfile;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
    
    int selectIdByUserId(Integer userId);
    int updateEmailById(@Param("id") Integer id, @Param("email") String email);
    int updateGenderById(@Param("id") Integer id, @Param("gender") String gender);
    int updateProfById(@Param("id") Integer id, @Param("prof") String prof);
    UserProfile selectByUid(Integer uid);
}
