package com.team1.lotteon.controller.admin.config;

import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.entity.Config;
import com.team1.lotteon.service.admin.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @ModelAttribute("config")
    public ConfigDTO getConfig() {
        return configService.getCompanyInfo();
    }
}
