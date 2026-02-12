package com.movie.dao;

import com.movie.entity.OrdinaryUser;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 用户数据访问层
 */
public class UserDao {

    /**
     * 根据账号（邮箱或手机号）查找用户
     * @param account 账号（邮箱或手机号）
     * @return 用户对象，不存在返回 null
     */
    public OrdinaryUser findUserByAccount(String account) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        OrdinaryUser user = null;

        try {
            conn = DBUtil.getConnection();
            System.out.println("【DAO检查】当前连接的数据库: " + conn.getMetaData().getURL());

            String sql = "SELECT user_id, phone_number, user_mailbox, user_password, " +
                    "user_name, gender, birthday, register_time " +
                    "FROM ordinary_user " +
                    "WHERE user_mailbox = ? OR phone_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account);
            pstmt.setString(2, account);

            System.out.println("【DAO检查】正在执行查询，参数: [" + account + "]");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String dbPassword = rs.getString("user_password");
                System.out.println("【DAO检查】查询结果 - user_id: " + userId + ", user_password: " + dbPassword);

                user = new OrdinaryUser();
                user.setUserId(userId);
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setUserMailbox(rs.getString("user_mailbox"));
                user.setUserPassword(dbPassword);
                user.setUserName(rs.getString("user_name"));
                user.setGender(rs.getString("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setRegisterTime(rs.getTimestamp("register_time"));
                System.out.println("findUserByAccount 找到用户: " + user.getUserName() + ", 邮箱: " + user.getUserMailbox());
            } else {
                System.out.println("【DAO检查】查询结果集为空");
                System.out.println("findUserByAccount 未找到账号: " + account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    /**
     * 用户登录
     * @param account 账号（邮箱或手机号）
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回 null
     */
    public OrdinaryUser login(String account, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        OrdinaryUser user = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT user_id, phone_number, user_mailbox, user_password, " +
                    "user_name, gender, birthday, register_time " +
                    "FROM ordinary_user " +
                    "WHERE user_mailbox = ? OR phone_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account);
            pstmt.setString(2, account);
            rs = pstmt.executeQuery();

            System.out.println("login 查询账号: " + account);

            if (rs.next()) {
                user = new OrdinaryUser();
                user.setUserId(rs.getInt("user_id"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setUserMailbox(rs.getString("user_mailbox"));
                user.setUserPassword(rs.getString("user_password"));
                user.setUserName(rs.getString("user_name"));
                user.setGender(rs.getString("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setRegisterTime(rs.getTimestamp("register_time"));
                System.out.println("login 找到用户: " + user.getUserName() + ", 数据库密码: " + user.getUserPassword() + ", 输入密码: " + password);

                // 验证密码
                if (!password.equals(user.getUserPassword())) {
                    System.out.println("login 密码不匹配");
                    return null;
                }
            } else {
                System.out.println("login 未找到账号: " + account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册成功返回 true，失败返回 false
     */
    public boolean register(OrdinaryUser user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO ordinary_user " +
                    "(phone_number, user_mailbox, user_password, user_name, gender, birthday, register_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPhoneNumber());
            pstmt.setString(2, user.getUserMailbox());
            pstmt.setString(3, user.getUserPassword());
            pstmt.setString(4, user.getUserName());
            pstmt.setString(5, user.getGender());
            pstmt.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
            pstmt.setTimestamp(7, new java.sql.Timestamp(user.getRegisterTime().getTime()));

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 检查账号是否已存在
     * @param account 账号（邮箱或手机号）
     * @return 存在返回 true，不存在返回 false
     */
    public boolean checkUserExist(String account) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM ordinary_user " +
                    "WHERE user_mailbox = ? OR phone_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account);
            pstmt.setString(2, account);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return false;
    }
}
