package com.movie.service;

import com.movie.dao.UserDao;
import com.movie.entity.OrdinaryUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户业务逻辑层
 */
public class UserService {

    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    /**
     * 用户登录
     * @param account 账号（邮箱或手机号）
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回 null
     */
    public OrdinaryUser login(String account, String password) {
        if (account == null || account.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return userDao.login(account, password);
    }

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册成功返回用户对象，失败返回 null
     */
    public OrdinaryUser register(OrdinaryUser user) {
        if (user == null) {
            return null;
        }

        // 检查手机号或邮箱是否已存在
        String account = user.getUserMailbox() != null ? user.getUserMailbox() : user.getPhoneNumber();
        if (checkUserExist(account)) {
            return null;
        }

        // 设置注册时间
        user.setRegisterTime(new Date());

        // 执行注册
        if (userDao.register(user)) {
            return userDao.findUserByAccount(account);
        }
        return null;
    }

    /**
     * 检查用户是否存在
     * @param account 账号（邮箱或手机号）
     * @return 存在返回 true，不存在返回 false
     */
    public boolean checkUserExist(String account) {
        if (account == null || account.isEmpty()) {
            return false;
        }
        return userDao.checkUserExist(account);
    }

    public Map<String, Object> toSafeUser(OrdinaryUser user) {
        Map<String, Object> userData = new HashMap<>();
        if (user == null) {
            return userData;
        }
        userData.put("userId", user.getUserId());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("userMailbox", user.getUserMailbox());
        userData.put("userName", user.getUserName());
        userData.put("gender", user.getGender());
        userData.put("birthday", user.getBirthday());
        userData.put("registerTime", user.getRegisterTime());
        return userData;
    }
}
