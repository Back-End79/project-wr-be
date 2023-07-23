package com.tujuhsembilan.wrcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tujuhsembilan.wrcore.model.NotificationEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Async
  void sendMail(NotificationEmail notificationEmail) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom("workingreport@cloudias79.com", "Working Report");
      messageHelper.setTo(notificationEmail.getRecipient());
      messageHelper.setSubject(notificationEmail.getSubject());

      // Use Thymeleaf template to generate email content
      String emailContent = generateEmailContent(notificationEmail);
      messageHelper.setText(emailContent, true);
    };
    try {
      mailSender.send(messagePreparator);
      log.info("Notification email was sent!");
    } catch (MailException ex) {
      throw new MailSendException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), ex);
    }
  }

  private String generateEmailContent(NotificationEmail notificationEmail) {
    Context context = new Context();
    context.setVariable("recipient", notificationEmail.getRecipient());
    context.setVariable("formattedDate", notificationEmail.getFormattedDate());
    context.setVariable("fullName", notificationEmail.getFullName());
    context.setVariable("projectName", notificationEmail.getProjectName());
    context.setVariable("taskDescription", notificationEmail.getTaskDescription());
    context.setVariable("actualEffort", notificationEmail.getActualEffort() + " hours");

    return templateEngine.process("notification-email", context);
  }
}
