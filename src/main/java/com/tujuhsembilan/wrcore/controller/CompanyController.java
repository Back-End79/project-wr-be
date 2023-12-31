package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.model.Company;
import com.tujuhsembilan.wrcore.dto.CompanyDTO;
import com.tujuhsembilan.wrcore.service.CompanyService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyController {

  private final CompanyService companyService;
  private final ModelMapper modelMapper;
  private final MessageUtil msg;

  @PostMapping("/addCompany")
  public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDto) {
    var o = companyService.addCompany(companyDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(JsonApiModelBuilder
            .jsonApiModel()
            .model(EntityModel.of(o))
            .meta("message",
                msg.get("application.success.created", "Company Data"))
            .build());
  }

 @GetMapping
public ResponseEntity<?> getAllCompany(Pageable pageable, @RequestParam(required = false) String search) {

    Page<Company> companyPage;

    if (search != null && !search.isEmpty()) {
        companyPage = companyService.getCompanyByName(pageable, search);
    } else {
        companyPage = companyService.getAllCompany(pageable);
    }

    List<CompanyDTO> companyDTO = companyPage.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

    return ResponseEntity.ok(PagedModel.of(companyDTO,
            new PageMetadata(companyPage.getSize(), companyPage.getNumber(),
                    companyPage.getTotalElements())));
}


  @GetMapping("/{companyId}")
  public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) throws NotFoundException {
    Company company = companyService.getCompanyById(companyId);
    CompanyDTO companyDTO = convertToDto(company);

    if (companyDTO != null) {
      return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(companyDTO));
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{companyId}")
  public ResponseEntity<?> updateCompany(
      @PathVariable Long companyId,
      @RequestBody CompanyDTO companyDTO) throws NotFoundException {
    Company updatedCompany = companyService.updateCompany(companyId, companyDTO);
    CompanyDTO updatedCompanyDTO = convertToDto(updatedCompany);

    if (updatedCompanyDTO != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel()
              .model(EntityModel.of(updatedCompanyDTO))
              .meta("message",
                  msg.get("application.success.updated", "Company"))
              .build());
    }
    return ResponseEntity.notFound().build();
  }

  private CompanyDTO convertToDto(Company company) {
    return modelMapper.map(company, CompanyDTO.class);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
    companyService.deleteCompany(id);
    return ResponseEntity.noContent().build();
  }
}
