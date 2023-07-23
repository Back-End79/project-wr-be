package com.tujuhsembilan.wrcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.HolidayDTO;
import com.tujuhsembilan.wrcore.service.HolidayService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/holiday")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayController {
    private final HolidayService holidayService;
      private final MessageUtil msg;


    @PostMapping("/addHoliday")
    public ResponseEntity<Object> addHoliday(@RequestBody HolidayDTO holidayDTO){
        try {
            var o = holidayService.createHoliday(holidayDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .model(EntityModel.of(o))
                                 .meta(ConstantMessage.MESSAGE, msg
                                 .get("application.success.created", "Holiday Data"))
                                 .build());
        } catch (Exception ex){
            log.info(ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .meta(ConstantMessage.MESSAGE, ex.getMessage())
                                 .build());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> editHoliday(@PathVariable Long id, @RequestBody HolidayDTO holidayDTO){
        try {
            var o = holidayService.updateHoliday(id, holidayDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .model(EntityModel.of(o))
                                 .meta(ConstantMessage.MESSAGE, msg
                                 .get("application.success.updated", "Holiday"))
                                 .build());
        } catch (Exception ex){
            log.info(ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .meta(ConstantMessage.MESSAGE, ex.getMessage())
                                 .build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHoliday(@PathVariable("id") Long id){
        try {
            holidayService.deleteHoliday(id);
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .meta(ConstantMessage.MESSAGE, msg
                                 .get("application.success.deleted"))
                                 .build());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(JsonApiModelBuilder
                                 .jsonApiModel()
                                 .meta(ConstantMessage.MESSAGE, ex.getMessage())
                                 .build());
        }
    }
}
