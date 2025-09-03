package top.otsuland.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.product.entity.ProductFav;

@Mapper
public interface ProductFavMapper extends BaseMapper<ProductFav> {

    // 别写反了！否则会导致 uid=1 pid=1 的时候能够通过，但是其他事不能的，会迷惑性成功
    ProductFav selectByUidAndPid(@Param("uid") Integer uid, @Param("pid") Integer pid);
    List<ProductFav> selectByUid(Integer uid);
    List<Integer> selectIdsByUid(Integer uid);
}
