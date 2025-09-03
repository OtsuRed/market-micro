package top.otsuland.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.product.entity.ProductPic;

@Mapper
public interface ProductPicMapper extends BaseMapper<ProductPic> {
    List<ProductPic> selectByPid(Integer pid);
    ProductPic selectMainByPid(Integer pid);
    List<ProductPic> selectSubByPid(Integer pid);
    Integer countSubByPid(Integer pid);
    ProductPic selectByUidAndId(@Param("uid") Integer uid, @Param("id") Integer id);
    int updatePicById(@Param("id") Integer id, @Param("pic") byte[] pic);

    byte[] selectPicById(Integer picId);
}
