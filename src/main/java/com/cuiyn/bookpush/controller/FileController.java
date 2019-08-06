package com.cuiyn.bookpush.controller;

import com.cuiyn.bookpush.config.EmailServerConfig;
import com.cuiyn.bookpush.config.SiteConfig;
import com.cuiyn.bookpush.model.User;
import com.cuiyn.bookpush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    EmailServerConfig emailServerConfig;
    @Autowired
    SiteConfig siteConfig;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/file")
    public String file(@RequestParam(required = true, value = "uri") String fileURI,
                       Model model,
                       HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/login?code=3";
        }
        String userName = session.getAttribute("userName").toString();
        model.addAttribute("siteName", siteConfig.getName());
        model.addAttribute("userName", userName);

        String URISplit[] = fileURI.split("/");
        String bookName = URISplit[URISplit.length - 1];
        model.addAttribute("bookName", bookName);
        model.addAttribute("file", fileURI);

        Integer userId = (Integer) session.getAttribute("userId");
        Optional<User> user = userRepository.findById(userId);
        String pushEmail = user.get().getPushEmail();
        model.addAttribute("pushEmail", pushEmail);

        return "file.html";
    }
}
