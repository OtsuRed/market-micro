package top.otsuland.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.user.entity.UserPic;

@Mapper
public interface UserPicMapper extends BaseMapper<UserPic> {

    UserPic selectByUserId(int userId);

    int selectIdByUserId(Integer userId);

    int updatePicById(@Param("id") Integer id, @Param("pic") byte[] pic);

    int insertPicAndPicNarrow(@Param("userId") Integer userId, @Param("pic") byte[] pic, @Param("picN") byte[] picN);
}
