package com.mig.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(value = "com.mig.repository.asis", sqlSessionFactoryRef = "asisSqlSessionFactory")
public class AsisDataSourceConfig {

    @Bean(name = "asisSqlSessionFactory")
    public SqlSessionFactory asisSqlSessionFactory(@Qualifier("asisDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        SqlSessionFactory sqlSessionFactory = null;
        
        sqlSessionFactoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        
        sqlSessionFactoryBean.setConfiguration(configuration);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("mapper/asis/*.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        sqlSessionFactoryBean.setTypeAliasesPackage("com.mig.watch.vo,");
        
		try {
			sqlSessionFactory = sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return sqlSessionFactory;
    }

    @Bean(name = "asisSqlSessionTemplate")
    public SqlSessionTemplate asisSqlSessionTemplate(SqlSessionFactory asisSqlSessionFactory) {
        return new SqlSessionTemplate(asisSqlSessionFactory, ExecutorType.BATCH);
    }

}
