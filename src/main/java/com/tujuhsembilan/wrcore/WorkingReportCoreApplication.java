package com.tujuhsembilan.wrcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "lib.i18n", "com.tujuhsembilan", "lib.minio"})
public class WorkingReportCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkingReportCoreApplication.class, args);
	}

}
