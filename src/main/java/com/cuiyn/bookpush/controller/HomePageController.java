package com.cuiyn.bookpush.controller;

import com.cuiyn.bookpush.config.SiteConfig;
import com.cuiyn.bookpush.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    SiteConfig siteConfig;

    @RequestMapping(value = "/")
    public String index(@RequestParam(required = false, value = "uri", defaultValue = "/") String URI,
                        Model model,
                        HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/login?code=3";
        }
        String userName = session.getAttribute("userName").toString();
        model.addAttribute("siteName", siteConfig.getName());
        model.addAttribute("userName", userName);

        String baseDir = siteConfig.getBookDir();

        File file = new File(baseDir + "/" + URI);
        // 此处假设根目录无文件！
        List<Book> bookList = new LinkedList<>();
        if (file.isDirectory()) {
            String s[] = file.list();
            for (int i = 0; i < s.length; i++) {
                File f = new File(baseDir + "/" + URI + "/" + s[i]);
                Boolean isDirectory = f.isDirectory();
                String name = s[i];
                String fURI = URI + "/" + name;
                if (URI.equals("/")) {
                    fURI = "/" + name;
                }
                Book book = new Book(isDirectory, name, fURI);
                bookList.add(book);
            }
            model.addAttribute("currentURI", URI);
            model.addAttribute("bookList", bookList);
        } else {
            model.addAttribute("currentURI", "错误" + URI);
        }

        return "index.html";
    }
}