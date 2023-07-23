package com.tujuhsembilan.wrcore.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfigurationDTO {

    @AllArgsConstructor
    public enum Column {
        ROW_NUMBER("No."),
        DATE("Date"),
        START_TIME("Start Time"),
        END_TIME("End Time"),
        LOCATION("Location"),
        TOTAL_HOURS("Total Hours"),
        PRESENCE("Presence"),
        PERIOD("Period"),
        REGULAR_TASKS("Regular Tasks"),
        OVERTIME_TASKS("Overtime Tasks"),
        TASKS("Tasks"),
        ;

        private final String displayName;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ColumnConfiguration {
        private Column column;
        private String displayName;
        private Integer order;
    }

    private boolean includeAbsenceType = true;
    private boolean includeProjectName = true;
    private boolean includeDuration = true;

    @Getter(AccessLevel.NONE)
    private List<ColumnConfiguration> columnConfigurations = Collections.emptyList();

    public List<ColumnConfiguration> getColumnConfigurations() {
        if (this.columnConfigurations.isEmpty()) {
            List<ColumnConfiguration> def = new ArrayList<>();

            int i = 0;
            for (Column v : Column.values()) {
                def.add(new ColumnConfiguration(v, v.displayName, i));
                i++;
            }

            return def;
        } else {
            // @formatter:off
            return columnConfigurations
                    .stream()
                    .map(colConf -> {
                        if(colConf.getDisplayName() ==null || colConf.getDisplayName().trim().isEmpty()){
                            colConf.setDisplayName(colConf.getColumn().displayName);
                        }

                        return colConf;
                    })
                    .sorted(Comparator.comparing(ColumnConfiguration::getOrder))
                    .collect(Collectors.toList());
            // @formatter:on
        }
    }

}