package top.otsuland.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.user.common.Result;
import top.otsuland.user.dto.PageResult;
import top.otsuland.user.dto.UserFollowVO;
import top.otsuland.user.dto.UserFollowVO2;
import top.otsuland.user.dto.UserLoginResp;
import top.otsuland.user.dto.UserProfResp;
import top.otsuland.user.entity.User;
import top.otsuland.user.entity.UserProfile;
import top.otsuland.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 测试 - 获取用户列表
     */
    @GetMapping
    public Result<?> list() {
        List<User> users = userService.getUsersList();
        return Result.success(users);
    }

    /**
     * 注册
     * ok
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        int code = userService.register(user);
        switch(code) {
            case 1: return Result.set(code, "注册成功！");
            case -3: return Result.set(code, "缺少信息！");
            case -1: return Result.set(code, "用户名已经被使用！");
            case -2: return Result.set(code, "该电话号码已经被注册！");
            default: return Result.fail();
        }
    }
    
    /**
     * 登录
     * ok
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        int code = userService.login(user);
        System.out.println(code);
        if(code > 0) {
//            String token = JwtUtils.geneJWT(userService.withId(user));
//            System.out.println(token);
            UserLoginResp ulr = new UserLoginResp();
//            ulr.setToken(token);
            ulr.setUid(code);
            return Result.set(1, "登录成功！!", ulr);
        } else if(code == -1) {
            return Result.set(code, "密码为空！");
        } else if(code == -2) {
            return Result.set(code, "缺失信息！");
        } else if(code == -3) {
            return Result.set(code, "用户不存在！");
        } else if(code == -4) {
            return Result.set(code, "密码不正确！");
        } else {
            return Result.fail();
        }
    }

    /**
     * 修改基本信息
     * ok
     */
    @PutMapping
    public Result<?> meta(@RequestHeader("X-User-Id") Integer uid, @RequestBody User user) {
        if(userService.meta(uid, user) == 1) {
            return Result.set(1, "修改成功！", userService.getMeta(uid));
        }
        return Result.set(0, "修改失败！");
    }

    /**
     * 修改个人简介
     * ok
     */
    @PutMapping("/prof")
    public Result<?> prof(@RequestHeader("X-User-Id") Integer id, @RequestBody UserProfile userProfile) {
        int code = userService.prof(id, userProfile);
        if(code == 1) {
            return Result.set(code, "修改成功！");
        }
        return Result.set(code, "用户不存在！");
    }

    /**
     * 获取个人简介
     */
    @GetMapping("/prof/{uid}")
    public Result<?> getProf(@PathVariable("uid") Integer uid) {
        UserProfile uprof =  userService.getProf(uid);
        if(uprof == null) {
            return Result.set(0, "获取失败！");
        }
        UserProfResp upr = new UserProfResp(uprof);
        upr.setUsername(userService.getMeta(uid).getUsername());
        upr.setFollow(userService.getMeta(uid).getFollow());
        upr.setFans(userService.getMeta(uid).getFans());
        upr.setTel(userService.getMeta(uid).getTel());
        return Result.set(1, "获取成功！", upr);
    }

    @GetMapping("/prof")
    public Result<?> getProfWithoutUid(@RequestHeader("X-User-Id") Integer uid) {
        UserProfile uprof =  userService.getProf(uid);
        if(uprof == null) {
            return Result.set(0, "获取失败！");
        }
        UserProfResp upr = new UserProfResp(uprof);
        upr.setUsername(userService.getMeta(uid).getUsername());
        upr.setFollow(userService.getMeta(uid).getFollow());
        upr.setFans(userService.getMeta(uid).getFans());
        upr.setTel(userService.getMeta(uid).getTel());
        return Result.set(1, "获取成功！", upr);
    }

    /**
     * 上传头像
     * ok
     */
    @PostMapping("/icon")
    public Result<?> icon(@RequestHeader("X-User-Id") Integer id, @RequestParam MultipartFile pic) {
        try {
            int code = userService.icon(id, pic);
            switch (code) {
                case 1: return Result.set(code, "上传成功！");
                case -1: return Result.set(code, "用户不存在！");
                default: return Result.fail();
            }
        } catch (IOException e) {
            return Result.set(-2, "输入输出异常！");
        }
    }



    /**
     * 下载头像
     */
    @GetMapping("/icon/{uid}")
    public ResponseEntity<?> loadIcon2(@PathVariable("uid") Integer uid) {
        byte[] image = userService.getIcon(uid);
        if(image == null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(null);
        }
        String filename = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("inline", filename);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    /**
     * 关注用户
     */
    @PutMapping("/follow/{fid}")
    public Result<?> follow(@RequestHeader("X-User-Id") Integer uid, @PathVariable Integer fid) {
        int code = userService.follow(uid, fid);
        if(code == 1) {
            return Result.set(1, "关注成功！");
        }
        if(code == -1) {
            return Result.set(-1, "已关注！");
        }
        return Result.set(0, "关注失败！");
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/follow/{fid}")
    public Result<?> disfollow(@RequestHeader("X-User-Id") Integer uid, @PathVariable Integer fid) {
        int code = userService.disfollow(uid, fid);
        if(code == 1) {
            return Result.set(1, "取消关注！");
        }
        if(code == -1) {
            return Result.set(-1, "未关注！");
        }
        return Result.set(0, "取消失败！");
    }

    /**
     * 分页获取关注列表
     */
    @GetMapping("/follow/{uid}")
    public Result<?> getfollower(@PathVariable("uid") Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<UserFollowVO> pageParam = new Page<>(page, size);
        Page<UserFollowVO> resultPage = userService.getFolloweePage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));
    }

    /**
     * 分页获取粉丝列表
     */
    @GetMapping("/fans/{uid}")
    public Result<?> getfollowing(@PathVariable("uid") Integer uid,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {

        Page<UserFollowVO2> pageParam = new Page<>(page, size);
        Page<UserFollowVO2> resultPage = userService.getFollowerPage(pageParam, uid);
        return Result.set(1, "获取成功！", PageResult.of(resultPage));
    }

    /**
     * 判断是否关注
     */
    @GetMapping("/follow/if/{fid}")
    public Result<?> isFollowing(@RequestHeader("X-User-Id") Integer uid, @PathVariable Integer fid) {
        if(userService.isFollowing(uid, fid) == 1) {
            return Result.set(1, "已关注");
        }
        return Result.set(0, "未关注");
    }

    /**
     * 对外提供的验证接口
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkUserExistence(@PathVariable Integer userId) {
        boolean exists = userService.existsById(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getname/{userId}")
    public ResponseEntity<String> getUsername(@PathVariable Integer userId) {
        String username = userService.getNameById(userId);
        return ResponseEntity.ok(username);
    }
    

}
