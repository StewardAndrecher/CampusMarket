
package com.campusmarket.campusmarketserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)   // 主键自增
    private Long id;

    private String username;
    private String password;       // 存的是 BCrypt 加密后的密文
    private String nickname;
    private String avatar;
    private String phone;
    private Long schoolId;
    private Integer campusVerified;
    private Integer status;
    private Integer role;  // 0普通用户 1管理员
    private LocalDateTime createTime;  // 数据库默认值自动填，不用 set
    private LocalDateTime updateTime;
}