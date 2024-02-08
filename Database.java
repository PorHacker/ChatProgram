package me;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    Connection con = null;
    Statement stmt = null;
    String url = "jdbc:mysql://127.0.0.1:3306/kim's talk?serverTimezone=Asia/Seoul";
//    String url = "jdbc:mysql://127.0.0.1:3306/kim's talk?serverTimezone=Asia/Seoul";
    String user = "local_user";
    String passwd = "1234";      //root 계정의 비밀번호를 입력하면 된다.

    Database() {   //데이터베이스에 연결한다.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, passwd);
            stmt = con.createStatement();
            System.out.println("MySQL 서버 연동 성공");
        } catch(Exception e) {
            System.out.println("MySQL 서버 연동 실패 > " + e.toString());
        }
    }

    /* 로그인 정보를 확인 */
    String logincheck(String _i, String _p) {
        String str = null;


        String id = _i;
        String pw = _p;

        try {
            String checkingStr = "SELECT * FROM joinpersonal WHERE ID='" + id + "'";
            ResultSet result = stmt.executeQuery(checkingStr);

            int count = 0;
            while(result.next()) {
                if(pw.equals(result.getString("Password"))) {
                    str = result.getString("ID");
                    System.out.println("로그인 성공");
                }
                else {
                    System.out.println("로그인 실패");
                }
                count++;
            }
        } catch(Exception e) {
            System.out.println("로그인 실패 > " + e.toString());
        }

        return str;
    }

    // 회원가입 정보 등록
    boolean joinCheck(String _i, String _p, String _n, String _g, int _b, String _e, int _a) {
        boolean flag = false;

        String id = _i;
        String pw = _p;
        String name = _n;
        String gender = _g;
        int birth = _b;
        String Email = _e;
        int account = _a;

        try {
            String insertStr = "INSERT INTO joinpersonal VALUES('" + id + "', '" + pw + "', '" + name + "', '" + gender + "', '" + birth + "', '" + Email + "', '" + account + "')";
            stmt.executeUpdate(insertStr);

            flag = true;
            System.out.println("회원가입 성공");
        } catch(Exception e) {
            flag = false;
            System.out.println("회원가입 실패 > " + e.toString());
        }

        return flag;
    }

    // 계정 삭제 메서드
    boolean deleteAccount(String id) {
        boolean isDeleted = false;

        try {
            String deleteQuery = "DELETE FROM joinpersonal WHERE ID=?";
            try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
                pstmt.setString(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    isDeleted = true;
                    System.out.println("ID " + id + "에 해당하는 사용자 정보 삭제 성공");
                } else {
                    System.out.println("ID " + id + "에 해당하는 사용자 정보가 없습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }

        return isDeleted;
    }

    // 회원정보 수정 메서드
    void updateUserInfo(String id, String password, String name, String gender, String birth, String email, int account) {
        try {
            String updateQuery = "UPDATE joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, password);
                pstmt.setString(2, name);
                pstmt.setString(3, gender);
                pstmt.setString(4, birth);
                pstmt.setString(5, email);
                pstmt.setInt(6, account);
                pstmt.setString(7, id);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("ID " + id + "에 해당하는 사용자 정보 업데이트 성공");
                } else {
                    System.out.println("ID " + id + "에 해당하는 사용자 정보가 없습니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
    }

    // 이름 불러오기
    String getName(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);

                String s_name=null;
                ResultSet result = pstmt.executeQuery();

                while (result.next()) {
                    s_name = result.getString("Name");

                }
                return s_name;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }

        return null;
    }

    //pw 불러오기
    String getPassword(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);
                String s_password = null;

                ResultSet result = pstmt.executeQuery();

                while (result.next()) {
                    s_password = result.getString("Password");

                }

                return s_password;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
        return null;
    }

    String getGender(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);

                String s_gender = null;

                ResultSet result = pstmt.executeQuery();

                while (result.next()) {
                    s_gender = result.getString("Gender");

                }
                return s_gender;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
        return null;
    }

    String getBirth(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE ID = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);

                ResultSet result = pstmt.executeQuery();
                String s_birth = null;

                while (result.next()) {
                    s_birth = result.getString("Birth");

                }
                return s_birth;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
        return null;
    }

    String getEmail(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE ID = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);

                String s_email = null;

                ResultSet result = pstmt.executeQuery();

                while (result.next()) {
                    s_email = result.getString("Email");

                }
                return s_email;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
        return null;
    }

    String getAccount(String id) {
        try {
//            String updateQuery = "SELECT joinpersonal SET Password=?, Name=?, Gender=?, Birth=?, Email=?, Account=? WHERE ID=?";
            String updateQuery = "SELECT * FROM joinpersonal WHERE ID = ?";
            try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
                pstmt.setString(1, id);

                String s_account = null;

                ResultSet result = pstmt.executeQuery();

                while (result.next()) {
                    s_account = result.getString("Account");

                }
                return s_account;
            }
        } catch (SQLException e) {
            System.out.println("MySQL 서버 연동 실패 또는 SQL 오류: " + e.toString());
        }
        return null;
    }
}