package com.example.photos.util;

import com.example.photos.model.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Auther: Arrow
 * @Date: 2023/5/5
 * @Description: com.example.photos.util
 */

@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String email;

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendHtmlMail(EmailDTO emailDTO) {
        if(!(emailDTO.getEmail()==null||emailDTO.getEmail().isEmpty()))
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Context context = new Context();
            context.setVariables(emailDTO.getCommentMap());
            String process = templateEngine.process(emailDTO.getTemplate(), context);
            mimeMessageHelper.setFrom(email);
            mimeMessageHelper.setTo(emailDTO.getEmail());
            mimeMessageHelper.setSubject(emailDTO.getSubject());
            mimeMessageHelper.setText(process, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
