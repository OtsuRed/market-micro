package top.otsuland.user.dto;

import lombok.Data;
import top.otsuland.user.entity.UserProfile;

@Data
public class UserProfResp {
    private Integer userId;
    private String username;
    private String tel;
    private String email;
    private String gender;
    private String prof;
    private Integer follow;
    private Integer fans;

    public UserProfResp(UserProfile uprof) {
        this.userId = uprof.getUserId();
        this.email = uprof.getEmail();
        this.gender = uprof.getGender();
        this.prof = uprof.getProf();    
    }
}
