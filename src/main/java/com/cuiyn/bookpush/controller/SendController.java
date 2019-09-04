package com.cuiyn.bookpush.controller;

import com.cuiyn.bookpush.config.EmailServerConfig;
import com.cuiyn.bookpush.config.SiteConfig;
import com.cuiyn.bookpush.model.User;
import com.cuiyn.bookpush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Properties;

@Controller
public class SendController {
    @Autowired
    SiteConfig siteConfig;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailServerConfig emailServerConfig;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String send(@RequestParam(required = true, value = "file")String file,
                       @RequestParam(required = true, value = "name")String bookName,
                       HttpSession session,
                       Model model) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/login?code=3";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        String baseURI = siteConfig.getBookDir();
        String fileURI = baseURI + "/" + file;
        String result = "";

        model.addAttribute("siteName", siteConfig.getName());
        model.addAttribute("userName", user.getUserName());

        if (user.getPushLimit() == 0)
            result = "剩余推送次数为0，推送失败";
        else {
            user.setPushLimit(user.getPushLimit() - 1);

            String to = user.getPushEmail();
            String from = emailServerConfig.getAddress();
            Properties props = System.getProperties();

            props.setProperty("mail.smtp.host", emailServerConfig.getServer());
            props.put("mail.smtp.auth", "true");
            Session mailSession = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailServerConfig.getAddress(), emailServerConfig.getPassword());
                }
            });

            try {
                MimeMessage mimeMessage = new MimeMessage(mailSession);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                mimeMessage.setSubject("Convert");

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Convert");

                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileURI);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(bookName);
                multipart.addBodyPart(messageBodyPart);

                mimeMessage.setContent(multipart);
                Transport.send(mimeMessage);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            userRepository.save(user);
            result = "推送成功！";
        }

        model.addAttribute("result", result);
        return "send.html";
    }
}
