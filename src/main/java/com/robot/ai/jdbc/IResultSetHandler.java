package com.robot.ai.jdbc;

import java.sql.ResultSet;

/**
 * 把JDBC返回的结果集封装成特定类型
 * @Author: fangju
 * @Date: 2019/6/15
 */
public interface IResultSetHandler<T> {
    T handle(ResultSet rs) throws Exception;
}

