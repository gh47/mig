package com.mig.watch.dao;

import com.mig.watch.service.MigBaseService;
import com.mig.watch.vo.MigColInfoVO;
import com.mig.watch.vo.MigSqlTblVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class MigInsertDao {

    @Qualifier("tobeJdbcDataSource")
    @Autowired
    JdbcTemplate jdbcTemplate;

    public MigInsertDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int migInsertMain(MigSqlTblVO migSqlTblVO, List<Map<String, Object>> insertList) {
        int rtn = 0;
        int divInCnt = migSqlTblVO.getDivInCnt();
        int s = insertList.size();
        for (int i = 0; i < s; i+= divInCnt) {
            int j = i + divInCnt;
            if (j >= s) j = s;
            this.insertTobeTable(migSqlTblVO, insertList.subList(i, j));
        }
        insertList.clear();
        return rtn;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "tobeTxManager")
    public void insertTobeTable(MigSqlTblVO migSqlTblVO, List<Map<String, Object>> insertList) {
        StopWatch timer = new StopWatch();
        timer.start();

        int seq = migSqlTblVO.getSeq();
        LinkedHashMap<String, MigColInfoVO> colInfoMap = MigBaseService.seqColInfoMap.get(seq);

        String sql = migSqlTblVO.getInsertStr();

        jdbcTemplate.setFetchSize(insertList.size());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> map = insertList.get(i);

                int index = 0;
                for(String key : colInfoMap.keySet()) {
                    index++;
                    String dataType = colInfoMap.get(key).getColDataType();
                    if("NUMBER".equals(dataType)) {
                        ps.setBigDecimal(index, (BigDecimal) map.get(key));
                    } else if("CHAR".equals(dataType) || "VARCHAR2".equals(dataType)) {
                        ps.setString(index, (String) map.get(key));
                    } else if("DATE".equals(dataType)) {
                        ps.setDate(index, (Date) map.get(key));
                    } else if("BLOB".equals(dataType)) {
                        ps.setBlob(index, (Blob) map.get(key));
                    } else if("CLOB".equals(dataType)) {
                        ps.setClob(index, (Clob) map.get(key));
                    }
                }
            }
            @Override
            public int getBatchSize() {
                return insertList.size();
            }
        });

        timer.stop();
        log.debug("Insert -> time in seconds = {}", insertList.size() +" / " + timer.getTotalTimeSeconds());

    }

}
