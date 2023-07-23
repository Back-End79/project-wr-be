package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.model.File;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.repository.FileRepository;
import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CheckoutEmployeeService {
    private final WorkingReportRepository workingReportRepository;
    private final FileRepository fileRepository;

    @Transactional
    public void checkoutEmployee(Long wrId, String latitude, String longitude) {
        WorkingReport workingReport = workingReportRepository.findById(wrId).orElseThrow(()->new EntityNotFoundException("Working Report ID Not Found!"));
        boolean isCheckouted = workingReport.getCheckOut()!=null;
        boolean isCheckin = workingReport.getCheckIn()!=null;
        if (!isCheckouted && isCheckin) {
            workingReport.setCheckOut(Time.valueOf(LocalTime.now()));
            workingReport.setLocationCheckout(latitude +":"+ longitude);
            workingReport.setTotalHours(BigDecimal.valueOf((long) workingReport.getCheckOut().toLocalTime().getHour()-
                    workingReport.getCheckIn().toLocalTime().getHour()));
            workingReportRepository.save(workingReport);
        } else if (!isCheckin) {
            throw new EntityExistsException("Go CheckIn first before you CheckOut the Task!");
        } else {
            throw new EntityExistsException("This Working Report Already Checkouted!");
        }
    }

    @Transactional
    public void uploadedPhoto(Long wrId, String fileName) {
        WorkingReport workingReport = workingReportRepository.findById(wrId).orElseThrow(()->new EntityNotFoundException("Working Report ID Not Found!"));

        File file = File.builder().fileName(fileName)
                .workingReportId(workingReport).build();
        fileRepository.save(file);
    }
}
