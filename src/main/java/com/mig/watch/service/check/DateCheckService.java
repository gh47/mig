package com.mig.watch.service.check;

import com.mig.repository.asis.AsisBaseMapper;
import com.mig.repository.mig.MigBaseMapper;
import com.mig.repository.tobe.TobeBaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class DateCheckService {

    @Autowired
    private AsisBaseMapper asisBaseMapper;

    @Autowired
    private TobeBaseMapper tobeBaseMapper;

    @Autowired
    private MigBaseMapper migBaseMapper;

    @Async("threadPoolTaskCheck")
    public void getMigSysdate(Map<String, Object> params) {
        List<Map<String, Object>> rtnList = migBaseMapper.selectMigSysdate(params);
        String dbDate = (String) (rtnList.get(0)).get("DT");
        log.info("Mig database Date = {}", dbDate );
    }

    @Async("threadPoolTaskCheck")
    public void getTobeSysdate(Map<String, Object> params) {
        List<Map<String, Object>> rtnList = tobeBaseMapper.selectTobeSysdate(params);
        String dbDate = (String) (rtnList.get(0)).get("DT");
        log.info("Tobe database Date = {}", dbDate );
    }

    @Async("threadPoolTaskCheck")
    public void getAsisSysdate(Map<String, Object> params) {
        List<Map<String, Object>> rtnList = asisBaseMapper.selectAsisSysdate(params);
        String dbDate = (String) (rtnList.get(0)).get("DT");
        log.info("Asis database Date = {}", dbDate );
    }

    @Async("threadPoolTaskCheck")
    public void getJavaDate(Map<String, Object> params) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("Java Server Date = {}", new Date() );
    }
}
