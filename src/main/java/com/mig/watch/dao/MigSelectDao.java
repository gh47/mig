package com.mig.watch.dao;

import com.mig.watch.service.MigBaseService;
import com.mig.watch.vo.MigColInfoVO;
import com.mig.watch.vo.MigSqlTblVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import java.util.*;

@Slf4j
@Repository
public class MigSelectDao {

    @Qualifier("asisJdbcDataSource")
    @Autowired
    JdbcTemplate jdbcTemplate;

    public MigSelectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> migSelectMain(MigSqlTblVO migSqlTblVO, int s, int e) {
        int rtn = 0;
        StopWatch timer = new StopWatch();
        timer.start();
        List<Map<String, Object>> selectList = this.selectSqlPaging(migSqlTblVO, s, e);
        timer.stop();
        log.debug("Select -> time in seconds = {}", s +"/"+ selectList.size() +" / " + timer.getTotalTimeSeconds());
        return selectList;
    }

    public int selectCount(MigSqlTblVO migSqlTblVO) {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT ");
        sqlSb.append("COUNT(*) AS CNT ");
        sqlSb.append("FROM ");
        sqlSb.append(migSqlTblVO.getTblAsis());
        sqlSb.append(" ");
        if(migSqlTblVO.getSqlWhere() != null) {
            sqlSb.append("WHERE 1=1");
            sqlSb.append(" ");
            sqlSb.append(migSqlTblVO.getSqlWhere());
            sqlSb.append(" ");
        }
        jdbcTemplate.setFetchSize(1);
        int cnt = jdbcTemplate.queryForObject(sqlSb.toString(), Integer.class);
        return cnt;
    }


    public List<Map<String, Object>> selectSqlPaging(MigSqlTblVO migSqlTblVO, int s, int e) {



        int seq = migSqlTblVO.getSeq();
        LinkedHashMap<String, MigColInfoVO> colInfoMap = MigBaseService.seqColInfoMap.get(seq);

        String sql = migSqlTblVO.getSelectStr();

        if(migSqlTblVO.getDivOutCnt() > 0) {
            jdbcTemplate.setFetchSize(migSqlTblVO.getDivOutCnt());
        } else {
            jdbcTemplate.setFetchSize(1000);
        }

        return jdbcTemplate.query(sql,
                preparedStatement -> {
                    preparedStatement.setInt(1, s);
                    preparedStatement.setInt(2, e);
                },

                (rs, i) -> {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for(String key : colInfoMap.keySet()) {
                        String dataType = colInfoMap.get(key).getColDataType();
                        if("NUMBER".equals(dataType)) {
                            map.put(key, rs.getBigDecimal(key));
                        } else if("CHAR".equals(dataType) || "VARCHAR2".equals(dataType)) {
                            map.put(key, rs.getString(key));
                        } else if("DATE".equals(dataType)) {
                            map.put(key, rs.getDate(key));
                        } else if("BLOB".equals(dataType)) {
                            map.put(key, rs.getBlob(key));
                        } else if("CLOB".equals(dataType)) {
                            map.put(key, rs.getClob(key));
                        }
                    }

                    return map;
                });

    }

}
