package com.cnpc.framework.base.entity;

import lombok.Data;

@Data
public class NewPasswordUser extends User {
    private transient String oldPassword;
    private transient String newPassword;
    private transient String confirmPassword;
}
