package com.mig.watch.service.check;

import com.mig.repository.asis.AsisBaseMapper;
import com.mig.repository.mig.MigBaseMapper;
import com.mig.repository.tobe.TobeBaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DbCheckMainService {


    @Autowired
    private DateCheckService dateCheckService;


    public int dbCheckMain(String profiles) {
        int rtn = 0;
            this.dbDateCheck();
        return rtn;
    }
    private int dbDateCheck() {
        Map<String, Object> params =  new HashMap<String, Object>();
        dateCheckService.getMigSysdate(params);
        dateCheckService.getTobeSysdate(params);
        dateCheckService.getAsisSysdate(params);
        dateCheckService.getJavaDate(params);
        return 0;

    }

}
