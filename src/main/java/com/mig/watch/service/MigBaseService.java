package com.mig.watch.service;


import com.mig.repository.mig.MigBaseMapper;
import com.mig.watch.vo.MigColInfoVO;
import com.mig.watch.vo.MigSqlTblVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigBaseService {

    public static LinkedHashMap<Integer, LinkedHashMap<String, MigColInfoVO>> seqColInfoMap;

    @Autowired
    private MigBaseMapper migBaseMapper;

    public List<MigSqlTblVO> migBaseMain() {
        int rtn =  0;
        List<MigSqlTblVO> migTblColList = this.getMigSqlTblList();
        log.debug("migBaseMain migTblColList size = {}", migTblColList.size());

        this.setSeqColInfoMap(migTblColList);
        log.debug("migBaseMain seqColInfoMap size = {}", seqColInfoMap.size());

        return migTblColList;
    }

    private List<MigSqlTblVO> getMigSqlTblList() {
        int rtn = 0;
        List<MigSqlTblVO> migTblColList = null;

        Map<String, Object> params =  new HashMap<String, Object>();
        params.put("USE_YN", "Y");
        migTblColList =  migBaseMapper.selectMigSqlTblList(params);

        for (MigSqlTblVO migSqlTblVO : migTblColList) {
            String selectStr = this.migSelectStringBuilder(migSqlTblVO);
            migSqlTblVO.setSelectStr(selectStr);

            String insertStr = this.migInsertStringBuilder(migSqlTblVO);
            migSqlTblVO.setInsertStr(insertStr);
        }

        return migTblColList;
    }

    public void setSeqColInfoMap(List<MigSqlTblVO> migTblColList) {

        seqColInfoMap = new LinkedHashMap<Integer, LinkedHashMap<String, MigColInfoVO>>();
        LinkedHashMap<String, MigColInfoVO> colInfoMap = null;

        for(MigSqlTblVO migSqlTblVO : migTblColList) {
            int seq = migSqlTblVO.getSeq();
            List<String> colNameList = Arrays.asList(migSqlTblVO.getColName().split(","));
            List<String> colDataTypeList = Arrays.asList(migSqlTblVO.getColDataType().split(","));

            colInfoMap = new LinkedHashMap<String, MigColInfoVO>();

            for (int i = 0; i < colNameList.size(); i++) {

                String colNameTmp = colNameList.get(i);
                String colDataTypeTmp = colDataTypeList.get(i);

                MigColInfoVO migColInfoVO = new MigColInfoVO();
                migColInfoVO.setColName(colNameTmp);
                migColInfoVO.setColDataType(colDataTypeTmp);

                colInfoMap.put(colNameTmp, migColInfoVO);

            }
            seqColInfoMap.put(seq, colInfoMap);
        }
    }

    public String migSelectStringBuilder(MigSqlTblVO migSqlTblVO) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("SELECT ");
        sqlSb.append(migSqlTblVO.getColName());
        sqlSb.append(" ");
        sqlSb.append("FROM");
        sqlSb.append(" ");
        sqlSb.append(migSqlTblVO.getTblAsis());
        sqlSb.append(" ");
        if(migSqlTblVO.getSqlWhere() != null) {
            sqlSb.append("WHERE 1=1");
            sqlSb.append(" ");
            sqlSb.append(migSqlTblVO.getSqlWhere());
            sqlSb.append(" ");
        }
        if(migSqlTblVO.getSqlOrder() != null) {
            sqlSb.append(migSqlTblVO.getSqlOrder());
            sqlSb.append(" ");
        }
        if("ORACLE".equals(migSqlTblVO.getDivPagination())) {
            sqlSb.append("OFFSET :OFFSET_NUM ROWS FETCH NEXT :ROWS_NUM ROWS ONLY");
        } else {
            sqlSb.append("OFFSET :OFFSET_NUM ROWS FETCH NEXT :ROWS_NUM ROWS ONLY");
        }
        sqlSb.append(" ");

        return sqlSb.toString();
    }

    public String migInsertStringBuilder(MigSqlTblVO migSqlTblVO) {
        List<String> colList = Arrays.asList(migSqlTblVO.getColName().split(","));
        StringBuilder sqlSb = new StringBuilder();
            sqlSb.append("INSERT INTO ");
            sqlSb.append(migSqlTblVO.getTblTobe());
            sqlSb.append(" ");
            sqlSb.append("(");
            sqlSb.append(migSqlTblVO.getColName());
            sqlSb.append(")");
            sqlSb.append(" VALUES ");
            sqlSb.append(" ");

            sqlSb.append("(");
            for(int i = 0; i < colList.size(); i++) {
                if(i != 0) {
                    sqlSb.append(",");
                }
                sqlSb.append("?");
            }
            sqlSb.append(")");
        sqlSb.append(" ");

        return sqlSb.toString();
    }

}
