package com.mdxt.secureapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfig {
	@Scheduled(fixedRate = 60 * 1000)
	public void scheduleFixedRateTask() {
	    System.out.println(
	      "Fixed rate task - " + System.currentTimeMillis() / 1000);
	}
}
