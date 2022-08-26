package com.mig;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("h2")
@Slf4j
public class InitData {

    private final DataSource asisDataSource;
    private final DataSource tobeDataSource;
    private final DataSource migDataSource;

    public InitData(@Qualifier("migDataSource") DataSource migDataSource, @Qualifier("tobeDataSource") DataSource asisDataSource, @Qualifier("asisDataSource") DataSource tobeDataSource) {
        this.migDataSource = migDataSource;
        this.asisDataSource = asisDataSource;
        this.tobeDataSource = tobeDataSource;
    }

    @PostConstruct
    public void initialize(){
        initAsis();
        initTobe();
        initMig();
    }
    
    private void initAsis() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/schema-h2-asis.sql"));
        DatabasePopulatorUtils.execute(populator, asisDataSource);
        log.info("Asis database initialized success!");
    }

    private void initTobe() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/schema-h2-tobe.sql"));
        DatabasePopulatorUtils.execute(populator, tobeDataSource);
        log.info("Tobe database initialized success!");
    }

    private void initMig() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/schema-h2-sync.sql"));
        DatabasePopulatorUtils.execute(populator, migDataSource);
        log.info("Mig database initialized success!");
    }

}
