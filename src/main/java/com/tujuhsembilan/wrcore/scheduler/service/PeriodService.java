package com.tujuhsembilan.wrcore.scheduler.service;

import com.tujuhsembilan.wrcore.model.Period;
import com.tujuhsembilan.wrcore.repository.PeriodRepository;
import com.tujuhsembilan.wrcore.scheduler.dto.PeriodDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PeriodService {
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private ModelMapper mapper;

    private String[] monthNames = { "", "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };

    public void generatePeriod(int year) throws ParseException {
        Long dataSize = 0L;

        List<Period> listPeriod = new ArrayList<Period>();
        List<Object> listPeriodDTO = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, i-1, 1);

            String yearAndMonth = year + "-" + i + "-";
            String startDateString = yearAndMonth + "01";

            if (periodRepository.getPeriodByStartDate(startDateString) == null) {
                String endDateString = yearAndMonth + cal.getActualMaximum(Calendar.DATE);
                System.out.println(monthNames[i] + ": " + cal.getActualMaximum(Calendar.DATE));
                log.info(monthNames[i] + ": " + cal.getActualMaximum(Calendar.DATE));

                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);

                Period period = new Period();
                period.setPeriod(monthNames[i]);
                period.setStartDate((java.sql.Date) startDate);
                period.setEndDate((java.sql.Date) endDate);

                listPeriod.add(period);
                listPeriodDTO.add(mapper.map(period, PeriodDTO.class));

                dataSize ++;
            }
        }

        periodRepository.saveAll(listPeriod);
        checkInjectStatus(listPeriodDTO, dataSize);
    }

    private void checkInjectStatus(List<Object> listObject, Long dataSize) {
        if (dataSize > 0) {
            System.out.println("Success.");
            log.info("Success.");
        } else {
            System.out.println("No Data Injected");
            log.info("No Data Injected.");

        }
    }
}
