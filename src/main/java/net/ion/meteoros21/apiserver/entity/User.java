package net.ion.meteoros21.apiserver.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="tbl_user")
public class User
{
    @Id
    @Size(max = 45)
    private String userId;

    @NotBlank
    @Size(max = 100)
    private String userPwd;

    @NotBlank
    @Size(max = 40)
    private String userName;

    @Size(max = 1)
    private String isDeleted;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserPwd() {
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
    @Override
    public String toString()
    {
        String text = "userId: " + this.userId + ", userName: " + userName;
        return text;
    }
}