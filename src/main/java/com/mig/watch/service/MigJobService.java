package com.mig.watch.service;

import com.mig.repository.mig.MigBaseMapper;
import com.mig.watch.dao.MigInsertDao;
import com.mig.watch.dao.MigSelectDao;
import com.mig.watch.vo.MigSqlTblVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigJobService {

    @Autowired
    private MigBaseService migBaseService;

    @Autowired
    private MigSelectDao migSelectDao;

    @Autowired
    private MigInsertDao migInsertDao;


    public int migJobMain() {
        int rtn = 0;
        List<MigSqlTblVO> migTblColList = migBaseService.migBaseMain();;
        log.debug("MigJobService MigJobMain migTblColMap size = {}", migTblColList.size() );

        log.info("getSaltTstReqBdMain ----------------------------------------------------------------------------------------------------------- START");
        for(MigSqlTblVO migSqlTblVO : migTblColList) {
            int divOutCnt = migSqlTblVO.getDivOutCnt();
            int totalCnt = migSelectDao.selectCount(migSqlTblVO);
            log.debug("MigJobService migJobMain divOutCnt/totalCnt  = {}", divOutCnt + "/" + totalCnt);
            for (int i = 0; i < totalCnt; i+= divOutCnt ) {
                List<Map<String, Object>> getSelectList = migSelectDao.migSelectMain(migSqlTblVO, i, divOutCnt);
                migInsertDao.migInsertMain(migSqlTblVO, getSelectList);
                getSelectList.clear();
            }
        }
        log.info("getSaltTstReqBdMain ----------------------------------------------------------------------------------------------------------- END");
        return rtn;
    }

}
