package top.otsuland.product.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.product.entity.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    Product selectByUidAndPid(Integer uid, Integer pid);
    List<Product> selectByUserId(Integer uid);
    int updateNameById(Integer id, String name);
    int updatePriceById(Integer id, BigDecimal price);
    int updateAmountById(Integer id, Integer amount);
    int updateProfById(Integer id, String prof);
    List<Product> selectByIds(List<Integer> ids);
    String selectNameById(Integer id);
    Product selectByNameAndSellerId(@Param("name") String name, @Param("sellerId") Integer uid);
    Page<Product> selectProductByUid(
        @Param("page") Page<Product> page,
        @Param("uid") Integer uid
    );
    Page<Product> selectFavByUid(
        @Param("page") Page<Product> page,
        @Param("uid") Integer uid
    );
}
