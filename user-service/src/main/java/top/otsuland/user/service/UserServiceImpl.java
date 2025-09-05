package top.otsuland.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import top.otsuland.user.common.PictureUtils;
import top.otsuland.user.dto.UserFollowResp;
import top.otsuland.user.dto.UserFollowVO;
import top.otsuland.user.dto.UserFollowVO2;
import top.otsuland.user.dto.UserMetaResp;
import top.otsuland.user.entity.User;
import top.otsuland.user.entity.UserFollow;
import top.otsuland.user.entity.UserPic;
import top.otsuland.user.entity.UserProfile;
import top.otsuland.user.mapper.UserFollowMapper;
import top.otsuland.user.mapper.UserMapper;
import top.otsuland.user.mapper.UserPicMapper;
import top.otsuland.user.mapper.UserProfileMapper;

import static org.springframework.core.io.buffer.DataBufferUtils.readInputStream;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserPicMapper userPicMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public List<User> getUsersList() {
        return userMapper.list();
    }

    /**
     * 注册
     * ok
     */
    @Override
    @Transactional
    public int register(User user) {
        // 检测是否为空、空串、空白串
        if(StringUtils.isAnyBlank(user.getPwd(), user.getTel(), user.getUsername())) {
            return -3;
        }
        // 如果用户名已经被使用
        if(userMapper.selectByUsername(user.getUsername()) != null) {
            // 用户名已经被使用！
            return -1;
        }
        if(userMapper.selectByTel(user.getTel()) != null) {
            // 该电话号码已经被注册！
            return -2;
        }
        user.setFollow(0);
        user.setFans(0);
        int row1 = userMapper.insert(user);

        // 初始化 UserProfile
        UserProfile up = new UserProfile();
        up.setUserId(userMapper.selectByUsername(user.getUsername()).getId());
        int row2 = userProfileMapper.insert(up);
        try
        {
            URL httpUrl = new URL("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = inStream.readAllBytes();//得到图片的二进制数据
            int row3 = userPicMapper.insertPicAndPicNarrow(userMapper.selectByUsername(user.getUsername()).getId(),btImg,null);
            if(row1 == 1 && row2 == 1 && row3 == 1) {
                return 1;
            }
            return 0;
        } catch (RuntimeException | IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * 登录
     * ok
     */
    @Override
    public int login(User user) {
        if(StringUtils.isAnyBlank(user.getPwd())) {
            // 密码为空！
            return -1;
        }
        if(StringUtils.isAllBlank(user.getUsername(), user.getTel())) {
            // 缺失信息！
            return -2;
        }
        User u = null;
        // 输入了用户名
        if(StringUtils.isBlank(user.getTel())) {
            u = userMapper.selectByUsername(user.getUsername());
            if(u == null) {
                // 用户不存在！
                return -3;
            }
        } else { // 输入了电话号码
            u = userMapper.selectByTel(user.getTel());
            if(u == null) {
                return -3;
            }
        }
        // 密码不正确！
        // 字符串比较，不能直接 !=,!= 比较的是其引用！
        if(!u.getPwd().equals(user.getPwd())) {
            return -4;
        }
        // 登录成功！
        Integer uid = u.getId();
        return uid;
    }

    /**
     * 获取 user 对象的 id
     * ok
     */
    @Override
    public User withId(User user) {
        if(StringUtils.isBlank(user.getTel())) {
            return userMapper.selectByUsername(user.getUsername());
        }
        return userMapper.selectByTel(user.getTel());
    }

    /**
     * 修改基本信息
     * ok
     */
    @Override
    public int meta(Integer id, User user) {
        // 用户不存在
        if(userMapper.selectById(id) == null) {
            return -1;
        }
        // 修改密码
        if(!StringUtils.isBlank(user.getPwd())) {
            userMapper.updatePwdById(id, user.getPwd());
        }
        // 修改电话号码
        if(!StringUtils.isBlank(user.getTel())) {
            userMapper.updateTelById(id, user.getTel());
        }
        // 修改用户名
        if(!StringUtils.isBlank(user.getUsername())) {
            userMapper.updateUsernameById(id, user.getUsername());
        }
        // 修改成功
        return 1;
    }

    /**
     * 获取基本信息
     */
    @Override
    public UserMetaResp getMeta(Integer uid) {
        User user = userMapper.selectById(uid);
        if(userMapper.selectById(uid) != null) {
            UserMetaResp umr = new UserMetaResp();
            umr.setUsername(user.getUsername());
            umr.setTel(user.getTel());
            umr.setFollow(user.getFollow());
            umr.setFans(user.getFans());
            return umr;
        }
        return null;
    }

    /**
     * 修改个人简介
     * ok
     */
    @Override
    public int prof(Integer uid, UserProfile userProfile) {
        // 用户不存在！
        if(userMapper.selectById(uid) == null) {
            return -1;
        }
        // 获取 user_profile id
        int id = userProfileMapper.selectIdByUserId(uid);
        // 修改邮箱
        if(!StringUtils.isBlank(userProfile.getEmail())) {
            userProfileMapper.updateEmailById(id, userProfile.getEmail());
        }
        // 修改性别
        if((!StringUtils.isBlank(userProfile.getGender()))
            && ((userProfile.getGender().equals("男"))
            || (userProfile.getGender().equals("女"))
            || (userProfile.getGender().equals("未公开")))) {
            userProfileMapper.updateGenderById(id, userProfile.getGender());
        }
        // 修改简介文字
        if(!StringUtils.isBlank(userProfile.getProf())) {
            userProfileMapper.updateProfById(id, userProfile.getProf());
        }
        // 修改成功
        return 1;
    }

    @Override
    public UserProfile getProf(Integer uid) {
        // 用户不存在！
        if(userMapper.selectById(uid) == null) {
            return null;
        }
        return userProfileMapper.selectByUid(uid);
    }

    /**
     * 上传头像图片
     * ok
     */
    @Override
    public int icon(Integer uid, MultipartFile pic) throws IOException {
        // 用户不存在！
        if(userMapper.selectById(uid) == null) {
            return -1;
        }
        // 生成缩略图
        byte[] picNarrow = PictureUtils.getNarrowPhoto(pic);
        // 如果用户没有头像
        if(userPicMapper.selectByUserId(uid) == null) {
            userPicMapper.insertPicAndPicNarrow(uid, pic.getBytes(), picNarrow);
        }
        // 如果用户已经有头像
        int id = userPicMapper.selectIdByUserId(uid);
        userPicMapper.updatePicById(id, pic.getBytes());
        // 上传成功！
        return 1;
    }

    @Override
    public byte[] getIcon(Integer uid) {
        UserPic upic = userPicMapper.selectByUserId(uid);
        if(upic == null) {
            return userPicMapper.selectById(0).getPicture();
        }
        return upic.getPicture();
    }

    @Override
    @Transactional
    public int follow(Integer uid, Integer fid) {
        if(userMapper.selectById(uid) == null ||
            userMapper.selectById(fid) == null) {
            return 0;
        }
        if(userFollowMapper.selectByFolloweeAndFollower(fid, uid) != null) {
            return -1;
        }
        UserFollow ufo = new UserFollow();
        ufo.setFolloweeId(fid);
        ufo.setFollowerId(uid);
        userFollowMapper.insert(ufo);

        // MySQL 需要添加 allowMultiQueries=true
        int updateFollow = userMapper.update(null,
            new UpdateWrapper<User>()
                .setSql("follow = follow + 1") // 关注者关注数 + 1
                .eq("id", uid)
        );
        int updateFans = userMapper.update(null,
            new UpdateWrapper<User>()
                .setSql("fans = fans + 1")
                .eq("id", fid)
        );
        if(updateFollow == 0 || updateFans == 0) {
            throw new RuntimeException("更新计数器失败！");
        }
        return 1;
    }

    @Override
    @Transactional
    public int disfollow(Integer uid, Integer fid) {
        if(userMapper.selectById(uid) == null ||
            userMapper.selectById(fid) == null ||
            userMapper.selectById(uid).getFollow() <= 0 ||
            userMapper.selectById(fid).getFans() <= 0
        ) {
            return 0;
        }
        if(userFollowMapper.selectByFolloweeAndFollower(fid, uid) == null) {
            return -1;
        }
        int ufid = userFollowMapper.selectByFolloweeAndFollower(fid, uid).getId();
        userFollowMapper.deleteById(ufid);
        int updateFollow = userMapper.update(null,
            new UpdateWrapper<User>()
                .setSql("follow = follow - 1")
                .eq("id", uid)
        );
        int updateFans = userMapper.update(null,
            new UpdateWrapper<User>()
                .setSql("fans = fans - 1")
                .eq("id", fid)
        );
        if(updateFollow == 0 || updateFans == 0) {
            throw new RuntimeException("更新计数器失败！");
        }
        return 1;
    }

    // 获取关注列表
    @Override
    public List<UserFollowResp> getFollowee(Integer uid) {
        return userMapper.selectFolloweesByUid(uid);
    }

    @Override
    public Page<UserFollowVO> getFolloweePage(Page<UserFollowVO> pageParam, Integer uid) {
        Page<UserFollowVO> result = userFollowMapper.selectFolloweeWithUsername(pageParam, uid);
        return result;
    }

    // 获取粉丝列表
    @Override
    public List<UserFollowResp> getFollower(Integer uid) {
        return userMapper.selectFollowersByUid(uid);
    }

    @Override
    public Page<UserFollowVO2> getFollowerPage(Page<UserFollowVO2> pageParam, Integer uid) {
        Page<UserFollowVO2> result = userFollowMapper.selectFollowerWithUsername(pageParam, uid);
        return result;
    }

    // 是否关注
    @Override
    public Integer isFollowing(Integer uid, Integer fid) {
        LambdaQueryWrapper<UserFollow> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserFollow::getFollowerId, uid)
            .eq(UserFollow::getFolloweeId, fid);
        if(userFollowMapper.selectCount(lqw) == 1) {
            return 1;
        }
        return 0;
    }

    // 对外提供的接口
    @Override
    public boolean existsById(Integer userId) {
        if(userMapper.selectById(userId) != null) {
            return true;
        }
        return false;
    }

    @Override
    public String getNameById(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user == null) {
            return null;
        }
        return user.getUsername();
    }
}
