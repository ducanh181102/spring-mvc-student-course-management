package org.example.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("breadcrumb", "Cài đặt");
        model.addAttribute("activeNav", "settings");
        return "settings/index";
    }
}
