package com.zdw.crm.utils;

import java.sql.*;


    /**
     * JDBC的工具类
     */
    public class JdbcUtil {

        static {
            // 注册驱动（注册驱动只需要注册一次，放在静态代码块当中。DBUtil类加载的时候执行。）
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取数据库连接对象
         * @return conn 连接对象
         * @throws SQLException
         */
        public static Connection getConnection() throws SQLException {
            // 获取连接

            Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/crm","root","555");
            return conn;
        }

        /**
         * 释放资源
         * @param conn 连接对象
         * @param ps 数据库操作对象
         * @param rs 结果集对象
         */
        public static void close(Connection conn, Statement ps, ResultSet rs){
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

