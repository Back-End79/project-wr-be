package com.tujuhsembilan.wrcore.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "project_id")
  private Long projectId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`company_id`")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
  private Company companyId;

  @Column(name = "pic_project_name", length = 100)
  private String picProjectName;

  @Column(name = "pic_project_phone", length = 15)
  private String picProjectPhone;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "project_name", length = 100)
  private String projectName;

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "end_date")
  private Date endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "`project_type`", referencedColumnName = "category_code_id")
  private CategoryCode projectType;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "created_on")
  private Timestamp createdOn;

  @Column(name = "last_modified_by")
  private Long lastModifiedBy;

  @Column(name = "last_modified_on")
  private Timestamp lastModifiedOn;

  @Column(name = "initial_project")
  private String initialProject;
}