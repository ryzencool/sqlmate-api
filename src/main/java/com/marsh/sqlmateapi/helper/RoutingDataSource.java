package com.marsh.sqlmateapi.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marsh.sqlmateapi.domain.ProjectDataSource;
import com.marsh.sqlmateapi.mapper.ProjectDataSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    private JdbcTemplate jdbcTemplate;


    public  static Map<Object, Object> DATASOURCE_MAP = new ConcurrentHashMap<>();


    public RoutingDataSource() {
        log.info("初始化动态数据源");
        createAndSaveDataSource(RoutingDataSourceContext.getMainKey());

        log.info("创建jdbcTemplate");
        DruidDataSource dataSource = getDataSource("master");
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = RoutingDataSourceContext.getRouteKey();
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = RoutingDataSourceContext.getMainKey();
        }
        log.info("当前操作账套:{}", tenantId);
        if (!DATASOURCE_MAP.containsKey(tenantId)) {
            log.info("{}数据源不存在, 创建对应的数据源", tenantId);
            createAndSaveDataSource(tenantId);
        } else {
            log.info("{}数据源已存在不需要创建", tenantId);
        }
        log.info("切换到{}数据源", tenantId);
        return tenantId;
    }

    public void createAndSaveDataSource(String tenantId) {
        DruidDataSource dataSource = createDataSource(tenantId);
        DATASOURCE_MAP.put(tenantId, dataSource);
        super.setTargetDataSources(DATASOURCE_MAP);
        afterPropertiesSet();
    }

    public void closeDataSource(String tenantId) {
        var ds = DATASOURCE_MAP.get(tenantId);
        var dsObj = (DruidDataSource) ds;
        dsObj.close();
        DATASOURCE_MAP.remove(tenantId);
    }

    private DruidDataSource createDataSource(String tenantId) {

        ProjectDataSource projectDataSource;
        if (tenantId.equalsIgnoreCase("master")) {
            projectDataSource = new ProjectDataSource();
            projectDataSource.setName("master");
            projectDataSource.setDbType(2);
            projectDataSource.setUrl("jdbc:postgresql://localhost:55435/sqlmate?currentSchema=public&useSSL=false");
            projectDataSource.setUsername("postgres");
            projectDataSource.setPassword("abc123");
        } else {
            projectDataSource = getDataSourceProperties(tenantId);
        }

        if (projectDataSource == null) {
            throw new InvalidParameterException("租户不存在");
        }
        return createDruidDataSource(projectDataSource);
    }


    private ProjectDataSource getDataSourceProperties(String name) {
        String sql = "select name, url, username, password, db_type, project_id, id from project_data_source where name = ?";
        RowMapper<ProjectDataSource> rowMapper = new BeanPropertyRowMapper<>(ProjectDataSource.class);
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }


    public static DruidDataSource createDruidDataSource(ProjectDataSource ds) {
        DruidDataSource dataSource = new DruidDataSource();
        if (ds.getDbType() == 2) {
            dataSource.setDriverClassName("org.postgresql.Driver");
        }
        dataSource.setName(ds.getName());
        dataSource.setUrl(ds.getUrl());
        dataSource.setUsername(ds.getUsername());
        dataSource.setPassword(ds.getPassword());

        dataSource.setInitialSize(2);
        // 从池中取得链接时做健康检查，该做法十分保守
        dataSource.setTestOnBorrow(true);
        // 如果连接空闲超过1小时就断开
        dataSource.setMinEvictableIdleTimeMillis(1 * 60000 * 60);
        // 每十分钟验证一下连接
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        // 运行ilde链接测试线程，剔除不可用的链接
        dataSource.setTestWhileIdle(true);
        dataSource.setMaxWait(-1);
        return dataSource;
    }


    public static DruidDataSource getDataSource(String tenantId) {
        return (DruidDataSource) DATASOURCE_MAP.get(tenantId);
    }

}
