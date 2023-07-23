package com.tujuhsembilan.wrcore.service;

import javax.transaction.Transactional;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.HolidayDTO;
import com.tujuhsembilan.wrcore.model.Holiday;
import com.tujuhsembilan.wrcore.repository.HolidayRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayService {
    private final HolidayRepository holidayRepository;

    @Transactional
    public Holiday createHoliday(HolidayDTO holidayDTO){
       try {
        Holiday holiday = new Holiday();
        Optional<Holiday> cekDate = holidayRepository.findOptionalByDate(holidayDTO.getDate());
        if(!cekDate.isPresent()){
           holiday = createHolidayMapper(holidayDTO);
        } else {
            throw new EntityExistsException("Holiday Date Already Exist!");
        }
        return holidayRepository.save(holiday);
        } catch (Exception ex){
            log.info(ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    @Transactional
    public Holiday updateHoliday(Long id, HolidayDTO holidayDTO){
        try {
            Holiday holidayId = holidayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id Holiday Not Found"));
            Holiday updateHoliday = updateHolidayMapper(holidayId, holidayDTO);
            

            return holidayRepository.save(updateHoliday);
        } catch (Exception ex){
            log.info(ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }
    
    @Transactional
    public void deleteHoliday(Long id){
        try {
            Holiday holidayId = holidayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Holiday Id Not Found"));

            holidayRepository.delete(holidayId);
        } catch (Exception ex){
            log.info(ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }


    private Holiday updateHolidayMapper(Holiday id, HolidayDTO holidayDTO){
        return Holiday.builder()
                      .holidayId(id.getHolidayId())
                      .date(holidayDTO.getDate())
                      .notes(holidayDTO.getNotes())
                      .build();
    }

    private Holiday createHolidayMapper(HolidayDTO holidayDTO){
        return Holiday.builder()
                      .date(holidayDTO.getDate())
                      .notes(holidayDTO.getNotes())
                      .build();
    }
}
