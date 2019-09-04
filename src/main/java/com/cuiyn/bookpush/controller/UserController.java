package com.cuiyn.bookpush.controller;

import com.cuiyn.bookpush.config.SiteConfig;
import com.cuiyn.bookpush.model.User;
import com.cuiyn.bookpush.repository.UserRepository;
import com.cuiyn.bookpush.tools.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    SiteConfig siteConfig;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(required = false, value = "code", defaultValue = "0") String code,
                        HttpSession session,
                        Model model) {
        model.addAttribute("siteName", siteConfig.getName());

        if (session.getAttribute("userName") != null) {
            model.addAttribute("error", "您已登录！");
            return "login.html";
        }
        if (code.equals("1")) {
            model.addAttribute("error", "登录失败，请检查用户名与密码是否正确");
        } else if (code.equals("2")) {
            model.addAttribute("error", "注册成功！");
        } else if (code.equals("3")) {
            model.addAttribute("error", "请先登录");
        } else if (code.equals("4")) {
            model.addAttribute("error", "注册失败：注册码错误");
        } else if (code.equals("5")) {
            model.addAttribute("error", "注册失败：推送邮箱已注册，请勿重复注册");
        }
        model.addAttribute("siteName", siteConfig.getName());
        return "login.html";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public String loginResult(@RequestParam String userName,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        List<User> users = userRepository.findByUserName(userName);
        if (users.size() == 0) {
            return "redirect:/login?code=1";
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (BCrypt.checkpw(password, user.getPassword())) {
                session.setAttribute("userName", userName);
                session.setAttribute("userId", user.getId());
                model.addAttribute("siteName", siteConfig.getName());
                model.addAttribute("userName", userName);
                return "authenticate.html";
            }
        }
        return "redirect:/login?code=1";
    }

    @RequestMapping (value = "/register")
    public String register(Model model) {
        model.addAttribute("siteName", siteConfig.getName());
        return "register.html";
    }

    @RequestMapping(value = "/register_result", method = RequestMethod.POST)
    public String register_result(@RequestParam String userName,
                                  @RequestParam String password,
                                  @RequestParam String pushEmail,
                                  @RequestParam String registerCode) {
        List<User> users = userRepository.findByPushEmail(pushEmail);
        if (users.size() != 0)
            return "redirect:/login?code=5";
        if (registerCode.equals(siteConfig.getRegisterCode())) {
            Integer pushLimit = siteConfig.getPushLimit();
            User user = new User(userName, password, pushEmail, pushLimit);
            userRepository.save(user);
            return "redirect:/login?code=2";
        } else
            return "redirect:/login?code=4";
    }

    @RequestMapping(value = "/logout")
    public String logout(Model model, HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/login?code=3";
        }
        session.removeAttribute("userName");
        model.addAttribute("siteName", siteConfig.getName());
        return "logout.html";
    }

    @GetMapping(value = "/account")
    public String account(Model model, HttpSession session) {
        if (session.getAttribute("userName") == null) {
            return "redirect:/login?code=3";
        }

        model.addAttribute("siteName", siteConfig.getName());
        model.addAttribute("pushLimit", siteConfig.getPushLimit());

        Integer userId = (Integer) session.getAttribute("userId");
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        model.addAttribute("user", user);
        return "account.html";
    }

    @ResponseBody
    @GetMapping(value = "/checkin")
    public String sign(HttpSession session){
        if (session.getAttribute("userName") == null) {
            return "登录信息出错，请尝试重新登录";
        } else {
            Integer userId = (Integer) session.getAttribute("userId");
            Optional<User> optionalUser = userRepository.findById(userId);
            User user = optionalUser.get();
            Date checkinDate = user.getCheckinDate();
            Date now = new Date();
            if (checkinDate == null) {
                user.setPushLimit(siteConfig.getPushLimit());
                user.setCheckinDate(now);
                userRepository.save(user);
                return "签到成功！";
            }
            if (isSameDay(checkinDate, now)) {
                return "您今天已经签到了";
            } else {
                user.setPushLimit(siteConfig.getPushLimit());
                user.setCheckinDate(now);
                userRepository.save(user);
                return "签到成功！";
            }
        }
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }
}
