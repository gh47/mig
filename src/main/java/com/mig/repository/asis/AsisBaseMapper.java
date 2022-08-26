package com.mig.repository.asis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AsisBaseMapper {

    List<Map<String, Object>> selectAsisSysdate(Map<String, Object> params);
}
