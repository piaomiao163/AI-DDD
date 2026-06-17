package com.piaomiao.dal.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.piaomiao.dal.interceptor.DataPermissionSqlParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis-Plus 配置类
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 添加数据权限 SQL 解析器
        List<ISqlParser> sqlParsers = new ArrayList<>();
        sqlParsers.add(new DataPermissionSqlParser());
        paginationInterceptor.setSqlParserList(sqlParsers);

        return paginationInterceptor;
    }
}