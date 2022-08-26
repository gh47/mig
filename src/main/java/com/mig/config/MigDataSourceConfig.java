package com.mig.config;

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

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(value = "com.mig.repository.mig", sqlSessionFactoryRef = "migSqlSessionFactory")
public class MigDataSourceConfig {

    @Bean(name = "migSqlSessionFactory")
    public SqlSessionFactory migSqlSessionFactory(@Qualifier("migDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        SqlSessionFactory sqlSessionFactory = null;
        
        sqlSessionFactoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.getTypeHandlerRegistry().register(Byte[].class, JdbcType.BLOB, LongRawTypeHandler.class);
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactoryBean.setConfiguration(configuration);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
       try {
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("mapper/mig/*.xml"));
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

    @Bean(name = "migSqlSessionTemplate")
    public SqlSessionTemplate tobeSqlSessionTemplate(SqlSessionFactory tobeSqlSessionFactory) {
        return new SqlSessionTemplate(tobeSqlSessionFactory, ExecutorType.BATCH);
    }

}
