package com.tujuhsembilan.wrcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
  private String subject;
  private String recipient;
  private String formattedDate;
  private String fullName;
  private String projectName;
  private String taskDescription;
  private String actualEffort;
}
