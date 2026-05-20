package com.movie.dao;

import com.movie.entity.OrdinaryUser;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public OrdinaryUser findUserByAccount(String account) {
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

            if (rs.next()) {
                user = mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    public OrdinaryUser login(String account, String password) {
        OrdinaryUser user = findUserByAccount(account);
        if (user == null || password == null || !password.equals(user.getUserPassword())) {
            return null;
        }
        return user;
    }

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
            if (user.getBirthday() != null) {
                pstmt.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            pstmt.setTimestamp(7, new java.sql.Timestamp(user.getRegisterTime().getTime()));

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

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

    private OrdinaryUser mapUser(ResultSet rs) throws Exception {
        OrdinaryUser user = new OrdinaryUser();
        user.setUserId(rs.getInt("user_id"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setUserMailbox(rs.getString("user_mailbox"));
        user.setUserPassword(rs.getString("user_password"));
        user.setUserName(rs.getString("user_name"));
        user.setGender(rs.getString("gender"));
        user.setBirthday(rs.getDate("birthday"));
        user.setRegisterTime(rs.getTimestamp("register_time"));
        return user;
    }
}
