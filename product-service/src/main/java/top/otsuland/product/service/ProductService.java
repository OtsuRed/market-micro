package top.otsuland.product.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.product.dto.ProductCreateReq;
import top.otsuland.product.dto.ProductPicMetaDTO;
import top.otsuland.product.entity.Product;

public interface ProductService {

    int add(Integer uid, ProductCreateReq pcr);
    int pic(Integer kind, Integer uid, Integer pid, MultipartFile pic) throws IOException;
    int picEdit(Integer uid, Integer picId, MultipartFile pic) throws IOException;
    int picDel(Integer uid, Integer picId);
    List<Product> get(Integer uid);
    int edit(Integer uid, Product product);
    int del(Integer uid, Integer pid);
    int fav(Integer uid, Integer pid);
    int favDel(Integer uid, Integer pid);
    List<Product> favList(Integer uid);
    List<ProductPicMetaDTO> getPicsMeta(Integer pid);
    byte[] getPic(Integer picId);

    Page<Product> list(Page<Product> pageParam, Integer uid, List<Integer> categoryIds, Integer priceSort, Integer timeSort, String keyword);
    Product getProduct(Integer pid);
    Page<Product> getListPage(Page<Product> pageParam, Integer uid);
    Page<Product> getFavPage(Page<Product> pageParam, Integer uid);
    Integer isFav(Integer uid, Integer pid);
}
