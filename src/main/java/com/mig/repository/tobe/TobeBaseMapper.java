package com.mig.repository.tobe;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TobeBaseMapper {


    List<Map<String, Object>> selectTobeSysdate(Map<String, Object> params);

}
