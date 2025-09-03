package top.otsuland.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.user.dto.UserFollowResp;
import top.otsuland.user.dto.UserFollowVO;
import top.otsuland.user.dto.UserFollowVO2;
import top.otsuland.user.dto.UserMetaResp;
import top.otsuland.user.entity.User;
import top.otsuland.user.entity.UserProfile;

public interface UserService {

    List<User> getUsersList();

    int register(User user);
    int login(User user);
    User withId(User user);
    int meta(Integer id, User user);
    UserMetaResp getMeta(Integer uid);
    int prof(Integer id, UserProfile userProfile);
    UserProfile getProf(Integer uid);
    int icon(Integer id, MultipartFile pic) throws IOException;
    byte[] getIcon(Integer uid);
    int follow(Integer uid, Integer fid);
    int disfollow(Integer uid, Integer fid);
    // 获取关注列表
    List<UserFollowResp> getFollowee(Integer uid);
    Page<UserFollowVO> getFolloweePage(Page<UserFollowVO> pageParam, Integer uid);
    // 获取粉丝列表
    List<UserFollowResp> getFollower(Integer uid);
    Page<UserFollowVO2> getFollowerPage(Page<UserFollowVO2> pageParam, Integer uid);
    Integer isFollowing(Integer uid, Integer fid);
    // 对外提供的功能
    boolean existsById(Integer userId);
    String getNameById(Integer userId);
}
