package com.novelpark.application.mail;

import com.novelpark.exception.ErrorCode;
import com.novelpark.exception.InternalServerException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
public class MailService {

  private final TemplateEngine templateEngine;
  private final JavaMailSender mailSender;

  @Async("mailThreadExecutor")
  public void sendFindLoginIdMail(final String loginId, final String receiverEmail) {
    Context context = new Context();
    context.setVariable("head", "Novel Park 아이디 찾가");
    context.setVariable("body", String.format("귀하의 아이디는 %s 입니다.", loginId));

    String message = templateEngine.process("email.html", context);
    sendMail("[Novel Park] 귀하의 아이디 찾기에 대한 메일입니다.", message, receiverEmail);
  }

  private void sendMail(final String subject, final String htmlText, final String receiverEmail) {
    try {
      MimeMessage mail = mailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");
      mimeMessageHelper.setTo(receiverEmail);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(htmlText, true);

      mailSender.send(mail);
    } catch (MessagingException ex) {
      throw new InternalServerException(ErrorCode.MAIL_SEND_FAIL);
    }
  }
}
