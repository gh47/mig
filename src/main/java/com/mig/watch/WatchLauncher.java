package com.mig.watch;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mig.watch.service.MigBaseService;
import com.mig.watch.service.MigJobService;
import com.mig.watch.service.check.DbCheckMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

@Component
@Profile({ "watch" })
@RequiredArgsConstructor
@Slf4j
public class WatchLauncher implements ApplicationListener<ApplicationReadyEvent>  {

	@Value("${spring.profiles.active}")
	private List<String> active;

	@Autowired
	private DbCheckMainService dbCheckMainService;

	@Autowired
	private MigBaseService migBaseService;

	@Autowired
	private MigJobService migJobService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		log.info("spring.profiles.active = {} ", active);
		String profiles = active.get(0);

		int result = 0;
		long delayTime = 300;

		try {
			// db접속, db시간 체크
			result = dbCheckMainService.dbCheckMain(profiles);
			TimeUnit.MILLISECONDS.sleep(1000);


			StopWatch timer = new StopWatch();
			timer.start();

			// 마이그 테이블 리스트 셋팅
			migBaseService.migBaseMain();

			log.info("*migJobService Start");
			// 마이그 테이블 리스트 셋팅
			migJobService.migJobMain();

			timer.stop();

			log.info("* migJobService End -> Total time in seconds: " + timer.getTotalTimeSeconds());

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}
}
