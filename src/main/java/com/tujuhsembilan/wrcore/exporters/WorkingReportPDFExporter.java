package com.tujuhsembilan.wrcore.exporters;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tujuhsembilan.wrcore.dto.ExportConfigurationDTO;
import com.tujuhsembilan.wrcore.dto.ExportConfigurationDTO.ColumnConfiguration;
import com.tujuhsembilan.wrcore.model.Task;
import com.tujuhsembilan.wrcore.model.WorkingReport;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class WorkingReportPDFExporter {

    private final List<WorkingReport> listWorkingReport;
    private final ExportConfigurationDTO config;

    public static final String ABSENCE = "absence";

    public WorkingReportPDFExporter(List<WorkingReport> listWorkingReport, ExportConfigurationDTO config) {
        this.listWorkingReport = Collections.unmodifiableList(listWorkingReport);
        this.config = config;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setPadding(5);
        cell.setHorizontalAlignment(1);
        cell.setVerticalAlignment(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        for (ColumnConfiguration colConf : config.getColumnConfigurations()) {
            cell.setPhrase(new Phrase(colConf.getDisplayName(), font));
            table.addCell(cell);
        }
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

    private void writeTableData(PdfPTable table, List<Task> listTasks) {
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
            table.getDefaultCell().setVerticalAlignment(5);

            for (ColumnConfiguration colConf : config.getColumnConfigurations()) {
                table.getDefaultCell().setHorizontalAlignment(1);
                switch (colConf.getColumn()) {
                    case DATE:
                        table.addCell(Optional.ofNullable(String.valueOf(workingReport.getDate())).orElse("").replace("null", ""));
                        break;
                    case END_TIME:
                        String[] x = Optional.ofNullable(String.valueOf(workingReport.getEndTime())).orElse("").replace("null", "")
                                .split(":");
                        table.addCell(x.length < 2 ? "" : (x[0] + ":" + x[1]));
                        break;
                    case LOCATION:
                        table.getDefaultCell().setHorizontalAlignment(0);
                        table.addCell(
                                Optional.ofNullable(String.valueOf(workingReport.getWorkLocation())).orElse("").replace("null", ""));
                        break;
                    case OVERTIME_TASKS:
                        table.getDefaultCell().setHorizontalAlignment(0);
                        table.addCell(Optional.of(overtimeTasks.toString()).orElse("").replace("null", ""));
                        break;
                    case PERIOD:
                        table.addCell(
                                Optional.ofNullable(String.valueOf(workingReport.getPeriodId().getPeriod() + " " + tahun)).orElse("")
                                        .replace("null", ""));
                        break;
                    case PRESENCE:
                        table.addCell(Optional.ofNullable(String.valueOf(workingReport.getPresenceId().getCodeName())).orElse("")
                                .replace("null", ""));
                        break;
                    case REGULAR_TASKS:
                        table.getDefaultCell().setHorizontalAlignment(0);
                        table.addCell(Optional.of(regularTasks.toString()).orElse("").replace("null", ""));
                        break;
                    case ROW_NUMBER:
                        table.addCell(String.valueOf(i));
                        break;
                    case START_TIME:
                        String[] y = Optional.ofNullable(String.valueOf(workingReport.getStartTime())).orElse("")
                                .replace("null", "")
                                .split(":");
                        table.addCell(y.length < 2 ? "" : (y[0] + ":" + y[1]));
                        break;
                    case TASKS:
                        table.getDefaultCell().setHorizontalAlignment(0);
                        table.addCell(Optional.of(regularTasks + "\n" + overtimeTasks).orElse("").replace("null", ""));
                        break;
                    case TOTAL_HOURS:
                        table.addCell(
                                Optional.ofNullable(String.valueOf(workingReport.getTotalHours())).orElse("").replace("null", ""));
                        break;
                }
            }

            i++;
        }
    }

    public void export(HttpServletResponse response, String employeeName, List<Task> listTasks, String date, String approver)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Image image = Image.getInstance(new ClassPathResource("/img/logo.png").getInputStream().readAllBytes());
        image.setAlignment(Element.ALIGN_CENTER);
        image.scaleToFit(300, 600);
        document.add(image);


        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        Paragraph p = new Paragraph(employeeName + "'s Working Report " + "(" + date + ")" + " Approver : " + approver, font);
        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        int colCnt = this.config.getColumnConfigurations().size();
        float[] widths = new float[] { 1.0F, 2.3F, 2.2F, 2.2F, 3.0F, 2.2F, 2.0F, 2.6F, 3.0F, 3.0F, 3.0F };

        PdfPTable table = new PdfPTable(colCnt);
        table.setWidthPercentage(100F);
        table.setWidths(Arrays.copyOfRange(widths, 0, colCnt));

        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, listTasks);

        document.add(table);

        document.close();
    }
}
