package top.otsuland.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import top.otsuland.product.entity.ProductCategory;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    List<Integer> selectProductIdsByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);
}
