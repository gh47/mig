package com.mig.watch.service;

import com.mig.repository.mig.MigBaseMapper;
import com.mig.watch.dao.MigInsertDao;
import com.mig.watch.dao.MigSelectDao;
import com.mig.watch.vo.MigSqlTblVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        log.info("MigJobService MigJobMain migTblColMap size = {}", migTblColList.size() );

        log.info("getSaltTstReqBdMain ----------------------------------------------------------------------------------------------------------- START");
        for(MigSqlTblVO migSqlTblVO : migTblColList) {
            log.info("migSqlTblVO = {}", "[seq:" + migSqlTblVO.getSeq() + "] asis-" + migSqlTblVO.getTblAsis() + " ---> tobe-" + migSqlTblVO.getTblTobe());

            int divOutCnt = migSqlTblVO.getDivOutCnt();
            int totalCnt = migSelectDao.selectCount(migSqlTblVO);
            log.debug("MigJobService migJobMain divOutCnt/totalCnt  = {}", divOutCnt + "/" + totalCnt);

            for (int i = 0; i < totalCnt; i+= divOutCnt ) {
                List<Map<String, Object>> getSelectList = migSelectDao.migSelectMain(migSqlTblVO, i, divOutCnt);
                this.saveList(migSqlTblVO, getSelectList);
            }
        }
        log.info("getSaltTstReqBdMain ----------------------------------------------------------------------------------------------------------- END");
        return rtn;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveList(MigSqlTblVO migSqlTblVO, List<Map<String, Object>> getSelectList) {
        migInsertDao.migInsertMain(migSqlTblVO, getSelectList);
    }

}
