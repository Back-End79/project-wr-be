package com.tujuhsembilan.wrcore;

import com.tujuhsembilan.wrcore.scheduler.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = { "lib.i18n", "com.tujuhsembilan", "lib.minio" })
@EnableScheduling
@EnableAsync
public class WorkingReportCoreApplication {

	@Autowired
	private PeriodService periodService;

	public static void main(String[] args) {
		SpringApplication.run(WorkingReportCoreApplication.class, args);
	}

	@Scheduled(cron = "0 1 0 1 1/12 ?")
	public void generatePeriod() throws ParseException {
		periodService.generatePeriod(LocalDate.now().getYear());
	}

}
