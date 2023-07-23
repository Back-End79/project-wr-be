package com.tujuhsembilan.wrcore.exporters;

import com.tujuhsembilan.wrcore.dto.ExportConfigurationDTO;
import com.tujuhsembilan.wrcore.dto.ExportConfigurationDTO.ColumnConfiguration;
import com.tujuhsembilan.wrcore.model.Task;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class WorkingReportExcelExporter {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<WorkingReport> listWorkingReport;
    private final ExportConfigurationDTO config;

    public static final String ABSENCE = "absence";

    public WorkingReportExcelExporter(List<WorkingReport> listWorkingReport) {
        this(listWorkingReport, new ExportConfigurationDTO());
    }

    public WorkingReportExcelExporter(List<WorkingReport> listWorkingReport, ExportConfigurationDTO config) {
        this.listWorkingReport = Collections.unmodifiableList(listWorkingReport);
        workbook = new XSSFWorkbook();
        this.config = config;
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Working Report");

        Row row = sheet.createRow(3);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int i = 0;
        for (ColumnConfiguration colConf : config.getColumnConfigurations()) {
            createCell(row, i++, colConf.getDisplayName(), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(String.valueOf(value));
        cell.setCellStyle(style);
    }

    private String constructTask(Task task) {
        boolean dontIncludeAbsenceType = !this.config.isIncludeAbsenceType()
                && ABSENCE.equalsIgnoreCase(task.getWorkingReportId().getPresenceId().getCodeName());

        if (dontIncludeAbsenceType) {
            return "";
        }

        boolean includeAbsenceType = true;
        boolean includeProjectName = true;
        try {
            includeAbsenceType = this.config.isIncludeAbsenceType()
                    && ABSENCE.equalsIgnoreCase(task.getWorkingReportId().getPresenceId().getCodeName());
            includeProjectName = this.config.isIncludeProjectName()
                    && !ABSENCE.equalsIgnoreCase(task.getWorkingReportId().getPresenceId().getCodeName());
        } catch (Exception e) {
        }

        StringBuilder s = new StringBuilder();

        if (includeAbsenceType || includeProjectName) {
            s.append("[").append(Optional.of(task.getWorkingReportId().getPresenceId())
                    .orElse(WorkingReport.builder().presenceId(null).build().getPresenceId())).append("] ");
        }
        s.append(task.getTaskItem());
        if (this.config.isIncludeDuration()) {
            s.append(" - ").append(task.getDuration()).append(" Jam");
        }
        s.append("\n");

        return s.toString();
    }

    private void writeDataLines(List<Task> listTasks) {
        int rowCount = 4;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setWrapText(true);

        int i = 1;
        int tahun = 0;
        Calendar cal = Calendar.getInstance();

        for (WorkingReport workingReport : listWorkingReport) {

            StringBuilder regularTasks = new StringBuilder();
            StringBuilder overtimeTasks = new StringBuilder();


            for (Task task : listTasks) {
                if (Objects.equals(task.getWorkingReportId().getWorkingReportId(), workingReport.getWorkingReportId())) {
                    if (task.getWorkingReportId().isOvertime()) {
                        overtimeTasks.append(constructTask(task));
                    }else{
                        regularTasks.append(constructTask(task));
                    }
                }
            }

            cal.setTime(workingReport.getDate());
            tahun = cal.get(Calendar.YEAR);
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            for (ColumnConfiguration colConf : config.getColumnConfigurations()) {
                style.setAlignment(HorizontalAlignment.CENTER);
                switch (colConf.getColumn()) {
                    case DATE:
                        createCell(row, columnCount++,
                                Optional.ofNullable(String.valueOf(workingReport.getDate())).orElse("").replace("null",
                                        ""),
                                style);
                        break;
                    case END_TIME:
                        String[] x = Optional.ofNullable(String.valueOf(workingReport.getEndTime())).orElse("")
                                .replace("null", "")
                                .split(":");
                        createCell(row, columnCount++,
                                x.length < 2 ? "" : (x[0] + ":" + x[1]),
                                style);
                        break;
                    case LOCATION:
                        style.setAlignment(HorizontalAlignment.LEFT);
                        createCell(row, columnCount++,
                                Optional.ofNullable(workingReport.getWorkLocation()).orElse("").replace("null", ""), style);
                        break;
                    case OVERTIME_TASKS:
                        style.setAlignment(HorizontalAlignment.LEFT);
                        createCell(row, columnCount++,
                                Optional.of(overtimeTasks.toString()).orElse("").replace("null", ""),
                                style);
                        break;
                    case PERIOD:
                        createCell(row, columnCount++,
                                Optional.of(workingReport.getPeriodId().getPeriod() + " " + tahun).orElse("")
                                        .replace("null", ""),
                                style);
                        break;
                    case PRESENCE:
                        createCell(row, columnCount++,
                                Optional.ofNullable(workingReport.getPresenceId().getCodeName()).orElse("").replace(
                                        "null",
                                        ""),
                                style);
                        break;
                    case REGULAR_TASKS:
                        style.setAlignment(HorizontalAlignment.LEFT);
                        createCell(row, columnCount++, Optional.of(regularTasks.toString()).orElse("").replace("null", ""),
                                style);
                        break;
                    case ROW_NUMBER:
                        createCell(row, columnCount++, i, style);
                        break;
                    case START_TIME:
                        String[] y = Optional.ofNullable(String.valueOf(workingReport.getStartTime())).orElse("")
                                .replace("null", "")
                                .split(":");
                        createCell(row, columnCount++,
                                y.length < 2 ? "" : (y[0] + ":" + y[1]),
                                style);
                        break;
                    case TASKS:
                        style.setAlignment(HorizontalAlignment.LEFT);
                        createCell(row, columnCount++,
                                Optional.of(regularTasks + "\n" + overtimeTasks).orElse("").replace("null", ""),
                                style);
                        break;
                    case TOTAL_HOURS:
                        createCell(row, columnCount++,
                                Optional.ofNullable(String.valueOf(workingReport.getTotalHours())).orElse("").replace(
                                        "null",
                                        ""),
                                style);
                        break;
                }
            }

            i++;
        }
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
    }

    private void writeTitle(String userName) {
        Row row1 = sheet.createRow(1);
        CellStyle styleTitle = workbook.createCellStyle();
        XSSFFont fontTitle = workbook.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeight(18);
        styleTitle.setFont(fontTitle);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        createCell(row1, 0, userName + "'s Working Report", styleTitle);

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

    }

    public void export(HttpServletResponse response, String userName, List<Task> listTasks) throws IOException {
        writeHeaderLine();
        writeTitle(userName);
        writeDataLines(listTasks);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }

}
