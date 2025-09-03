package top.otsuland.product.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.product.common.Result;
import top.otsuland.product.dto.PageResult;
import top.otsuland.product.dto.ProductCreateReq;
import top.otsuland.product.dto.ProductPicMetaDTO;
import top.otsuland.product.dto.ProductVO;
import top.otsuland.product.entity.Product;
import top.otsuland.product.service.ProductService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 用户-添加商品
     */
    @PostMapping
    public Result<?> add(
        @RequestAttribute("id") Integer uid,
        @RequestBody ProductCreateReq pcr) {
        int code = productService.add(uid, pcr);
        if(code > 0) {
            return Result.set(1, "上传成功！", code);
        } else if(code == -1) {
            return Result.set(code, "用户不存在！");
        } else if(code == -2) {
            return Result.set(code, "信息不完整！");
        } else if(code == -3) {
            return Result.set(code, "商品名重复！");
        }
        return Result.set(code, "创建失败！");
    }

    /**
     * 用户-上传商品图
     * ok
     */
    @PostMapping("/pic/{kind}/{pid}")
    public Result<?> pic(
        @RequestAttribute(required = true, value="id") Integer uid, 
        @PathVariable Integer kind,
        @PathVariable Integer pid, MultipartFile pic) {
        try {
            int code = productService.pic(kind, uid, pid, pic);
            switch(code) {
                case 1: return Result.set(code, "上传成功！");
                case -1: return Result.set(code, "用户不存在！");
                case -2: return Result.set(code, "无权限或商品不存在！");
                case -3: return Result.set(code, "主图已经上传过！");
                case -4: return Result.set(code, "副图已经超过 5 张！");
                default: return Result.fail();
            }
        } catch (IOException e) {
            return Result.set(-5, "输入输出异常！");
        }
    }

    /**
     * 用户-修改商品图
     */
    @PutMapping("/pic/{picId}")
    public Result<?> picEdit(@RequestAttribute("id") Integer uid, @PathVariable Integer picId, MultipartFile pic) {
        try {
            int code = productService.picEdit(uid, picId, pic);
            switch(code) {
                case 1: return Result.set(code, "修改成功！");
                case -1: return Result.set(code, "用户不存在！");
                case -2: return Result.set(code, "无权限或商品图不存在！");
                default: return Result.fail();
            }
        } catch (IOException e) {
            return Result.set(-3, "输入输出异常！");
        }
        
    }

    /**
     * 用户-删除商品图
     */
    @DeleteMapping("/pic/{picId}")
    public Result<?> picDel(@RequestAttribute("id") Integer uid, @PathVariable Integer picId) {
        int code = productService.picDel(uid, picId);
        switch(code) {
            case 1: return Result.set(code, "成功删除！");
            case -1: return Result.set(code, "用户不存在！");
            case -2: return Result.set(code, "无权限或商品不存在！");
            case -3: return Result.set(code, "禁止删主图！");
            default: return Result.fail();
        }
    }

    /**
     * 用户-获取自己已发布的商品列表
     */
    @GetMapping
    public Result<?> get(@RequestAttribute("id") Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Product> pageParam = new Page<>(page, size);
        Page<Product> resultPage = productService.getListPage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));

        // List<Product> products = productService.get(uid);
        // if(products == null) {
        //     return Result.set(-1, "暂无商品！");
        // }
        // return Result.set(1, "获取商品成功！", products);
    }

    /**
     * 获取别人发布的商品列表
     */
    @GetMapping("/published/{uid}")
    public Result<?> getMethodName(@PathVariable Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Product> pageParam = new Page<>(page, size);
        Page<Product> resultPage = productService.getListPage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));
    }
    
    /**
     * 用户-修改商品
     */
    @PutMapping
    public Result<?> edit(@RequestAttribute("id") Integer uid, @RequestBody Product product) {
        int code = productService.edit(uid, product);
        switch(code) {
            case 1: return Result.set(code, "修改成功！");
            case -1: return Result.set(code, "用户不存在！");
            case -2: return Result.set(code, "无权限或商品不存在！");
            default: return Result.fail();
        }
    }

    /**
     * 用户-删除商品（不再出售）
     */
    @DeleteMapping("/{pid}")
    public Result<?> del(@RequestAttribute("id") Integer uid, @PathVariable Integer pid) {
        int code = productService.del(uid, pid);
        switch(code) {
            case 1: return Result.set(code, "成功删除！");
            case -1: return Result.set(code, "用户不存在！");
            case -2: return Result.set(code, "无权限或商品不存在！");
            default: return Result.fail();
        }
    }

    /**
     * 收藏商品
     */
    @PostMapping("/fav/{pid}")
    public Result<?> fav(@RequestAttribute("id") Integer uid, @PathVariable Integer pid) {
        int code = productService.fav(uid, pid);
        switch(code) {
            case 1: return Result.set(code, "收藏成功！");
            case -1: return Result.set(code, "用户不存在！");
            case -2: return Result.set(code, "商品不存在！");
            case -3: return Result.set(code, "已经收藏过该商品了！");
            default: return Result.fail();
        }
    }

    /**
     * 删除收藏
     */
    @DeleteMapping("/fav/{pid}")
    public Result<?> favDel(@RequestAttribute("id") Integer uid, @PathVariable Integer pid) {
        int code = productService.favDel(uid, pid);
        switch(code) {
            case 1: return Result.set(code, "成功删除！");
            case -1: return Result.set(code, "收藏不存在！");
            default: return Result.fail();
        }
    }

    /**
     * 获取收藏列表
     */
    @GetMapping("/fav")
    public Result<?> favList(@RequestAttribute("id") Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Product> pageParam = new Page<>(page, size);
        Page<Product> resultPage = productService.getFavPage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));

        // List<Product> products = productService.favList(uid);
        // if(products.isEmpty() || products == null) {
        //     return Result.set(-1, "收藏列表为空！");
        // }
        // return Result.set(1, "获取收藏列表成功！", products);
    }

    @GetMapping("/fav/{uid}")
    public Result<?> favListOther(@PathVariable Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Product> pageParam = new Page<>(page, size);
        Page<Product> resultPage = productService.getFavPage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));
    }
    
    /**
     * 判断是否收藏商品
     */
    @GetMapping("/fav/if/{pid}")
    public Result<?> isFav(@RequestAttribute("id") Integer uid, @PathVariable Integer pid) {
        if(productService.isFav(uid, pid) == 1) {
            return Result.set(1, "已收藏");
        }
        return Result.set(0, "未收藏");
    }
    
    // 获取商品图片 id 列表
    @GetMapping("/pics/{pid}")
    public Result<?> getPicMeta(@PathVariable Integer pid) {
        List<ProductPicMetaDTO> data =  productService.getPicsMeta(pid);
        if(data != null && !data.isEmpty()) {
            return Result.set(1, "获取成功！", data);
        }
        return Result.set(-1, "商品不存在或列表为空！");
    }

    /**
     * 获取商品的主图
     */
    @GetMapping("/{picId}")
    public ResponseEntity<byte[]> getPic(@PathVariable Integer picId) {
        byte[] pic = productService.getPic(picId);
        if(pic == null || pic.length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(pic.length);
        return new ResponseEntity<>(pic, headers, HttpStatus.OK);
    }
    
    /**
     * 分页获取商品列表
     */
    @GetMapping("/lists")
    public Result<?> list(
        @RequestAttribute("id") Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(required = false) List<Integer> categoryIds, // 分类，集合类型必需显式声明为非必需
        @RequestParam(required = false) Integer priceSort, // 排序
        @RequestParam(required = false) Integer timeSort,
        @RequestParam(required = false) String keyword
    ) {
        Page<Product> pageParam = new Page<>(page, size);
        Page<Product> productPage = productService.list(pageParam, uid, categoryIds, priceSort, timeSort, keyword);
        PageResult<ProductVO> result = PageResult.of(productPage, this::convertToVO);
        return Result.set(1, "获取成功！", result);
    }

    private ProductVO convertToVO(Product product) {
        return new ProductVO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getAmount(),
            product.getSellerId(),
            product.getState(),
            product.getWant(),
            product.getSellerName()
        );
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{pid}")
    public Result<?> getProduct(@PathVariable Integer pid) {
        Product result = productService.getProduct(pid);
        return Result.set(1, "获取成功！", result);
    }
}
