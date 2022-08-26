package com.mig.repository.mig;

import com.mig.watch.vo.MigSqlTblVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MigBaseMapper {

    List<Map<String, Object>> selectMigSysdate(Map<String, Object> params);

    List<MigSqlTblVO> selectMigSqlTblList(Map<String, Object> params);
}
