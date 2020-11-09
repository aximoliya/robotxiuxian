package com.robot.ai.jdbc;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 增删改查工具类
 * @Author: fangju
 * @Date: 2019/6/15
 */
public class CRUDTemplate {
    /**
     * 增删改操作
     * @param sql 传入的SQL语句
     * @param params 可变参数
     * @return 操作结果
     */
    public static int executeUpdate(String sql,Object... params){
        Connection conn = null;
        PreparedStatement psmt = null;
        int result = 0;
        try {
            //获取数据库连接对象
            conn = JDBCUtil.getConn();
            //获取预编译语句对象
            psmt = conn.prepareStatement(sql);
            //给预编译语句赋值
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i+1,params[i]);
            }
            //执行SQL语句获取执行结果
            result = psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接
            JDBCUtil.close(conn,psmt,null);
        }
        return result;
    }

    /**
     * 查询操作
     * @param sql SQL语句
     * @param handler 判断查询一个还是多个
     * @param params 可变参数
     * @param <T> 具体操作的实体类
     * @return 返回IResultSetHandler接口中的泛型
     */
    public static <T> T executeQuery(String sql, IResultSetHandler<T> handler,Object... params){
        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
            //获取数据库连接对象
            conn = JDBCUtil.getConn();
            //获取预编译语句对象
            psmt = conn.prepareStatement(sql);
            //给预编译语句赋值
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i+1,params[i]);
            }
            //执行SQL语句获取结果集
            rs = psmt.executeQuery();
            //处理结果集
            return handler.handle(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接
            JDBCUtil.close(conn,psmt,rs);
        }
        return null;
    }

    //很经典的判断存在不存在
    public static int executeQueryID(String sql,Object... params){
        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        int row = 0;
        try {
            //获取数据库连接对象
            conn = JDBCUtil.getConn();
            //获取预编译语句对象
            psmt = conn.prepareStatement(sql);
            //给预编译语句赋值
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i+1,params[i]);
            }
            //执行SQL语句获取结果集
            rs = psmt.executeQuery();
            //结果集指针移到最后一行
            rs.last();
            //返回并记录当前指针在第几行
            row = rs.getRow();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接
            JDBCUtil.close(conn,psmt,rs);
        }
        return row;
    }

}


