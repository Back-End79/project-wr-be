package com.tujuhsembilan.wrcore.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.tujuhsembilan.wrcore.dto.WorkingReportDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.Period;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.PeriodRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import com.tujuhsembilan.wrcore.repository.WorkingReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkingReportService {

  private final WorkingReportRepository workingReportRepository;
  private final PeriodRepository periodRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final UserRepository userRepository;

  // public WorkingReport createWorkingReport(WorkingReportDTO workingReportDTO)
  // throws NotFoundException {
  // WorkingReport workingReport = createWorkingReportMapper(workingReportDTO);
  // return workingReportRepository.save(workingReport);
  // }

  // private WorkingReport createWorkingReportMapper(WorkingReportDTO
  // workingReportDTO) throws NotFoundException{
  // return WorkingReport.builder()
  // .periodId(periodRepository.findById(workingReportDTO.getPeriodId()).orElseGet(()
  // -> new Period()))
  // .presenceId(categoryCodeRepository.findById(workingReportDTO.getPresenceId()).orElseGet(()
  // -> new CategoryCode()))
  // .userId(userRepository.findById(workingReportDTO.getUserId()).orElseGet(() ->
  // new Users()))
  // .date(workingReportDTO.getDate())
  // .checkIn(workingReportDTO.getCheckIn())
  // .checkOut(workingReportDTO.getCheckOut())
  // .workLocation(workingReportDTO.getWorkLocation())
  // .totalHours(workingReportDTO.getTotalHours())
  // .createTime(workingReportDTO.getCreateTime())
  // .isHoliday(workingReportDTO.isHoliday())
  // .locationCheckin(workingReportDTO.getLocationCheckin())
  // .locationCheckout(workingReportDTO.getLocationCheckout())
  // .build();
  // }

}
