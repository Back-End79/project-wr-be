package com.tujuhsembilan.wrcore.controller;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.CompanyDTO;
import com.tujuhsembilan.wrcore.dto.DetailCompanyDTO;
import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.service.CompanyService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyController {

  private final CompanyService companyService;
  private final ModelMapper modelMapper;
  private final MessageUtil msg;

  @GetMapping
  public ResponseEntity<Object> getAllCompany( @RequestParam(required = false) String search, Pageable pageable) {
    Page<Company> company = companyService.getAllCompany(pageable, search);
    List<CompanyDTO> companyDTO = companyService.convertCompanyToDto(company);
    return ResponseEntity.ok(PagedModel.of(companyDTO,
        new PageMetadata(company.getSize(), company.getNumber(),
            company.getTotalElements())));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteCompany(@PathVariable Long id) {
    try {
      companyService.deleteCompany(id);
      return ResponseEntity.status(HttpStatus.OK)
              .body(JsonApiModelBuilder
                      .jsonApiModel()
                      .meta(ConstantMessage.MESSAGE, msg
                              .get("application.success.deleted"))
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

  @PostMapping("/addCompany")
  public ResponseEntity<Object> addCompany(@RequestBody CompanyDTO companyDto) {
    var o = companyService.addCompany(companyDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(o))
            .meta(ConstantMessage.MESSAGE, msg
                .get("application.success.created", "Company Data"))
            .build());
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<Object> getCompanyById(@PathVariable Long companyId) {
    DetailCompanyDTO detailCompany = companyService.getDetailCompany(companyId);

    return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(detailCompany));
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<Object> updateCompany(
      @PathVariable Long companyId,
      @RequestBody CompanyDTO companyDTO) {
    Company updatedCompany = companyService.updateCompany(companyId, companyDTO);
    CompanyDTO updatedCompanyDTO = convertToDto(updatedCompany);

    return ResponseEntity.status(HttpStatus.OK)
        .body(JsonApiModelBuilder.jsonApiModel().model(EntityModel.of(updatedCompanyDTO))
            .meta(ConstantMessage.MESSAGE, msg.get("application.success.updated", "Company")).build());
  }

  private CompanyDTO convertToDto(Company company) {
    return modelMapper.map(company, CompanyDTO.class);
  }
}
