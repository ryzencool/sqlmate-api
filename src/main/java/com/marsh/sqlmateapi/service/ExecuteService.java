package com.marsh.sqlmateapi.service;

import com.marsh.zutils.exception.BaseBizException;
import com.marsh.zutils.exception.BaseErrorCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExecuteService {

    private final JdbcTemplate jdbcTemplate;

    public ExecuteService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object execute(String sql) {
        sql = sql.strip();
        Object result = null;
        try {
            if (sql.startsWith("select") || sql.startsWith("explain")) {
                result = jdbcTemplate.queryForList(sql);
            } else if (sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete")) {
                result = jdbcTemplate.update(sql);
            } else {
                jdbcTemplate.execute(sql);
            }
        } catch (Exception e) {
            throw new BaseBizException(new BaseErrorCode("200000", e.getMessage()));
        }
        return result;
    }
}
