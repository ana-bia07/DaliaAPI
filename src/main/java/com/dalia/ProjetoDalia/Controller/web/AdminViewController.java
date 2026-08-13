package com.dalia.ProjetoDalia.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "Dashboard"; // Retorna Dashboard.html da pasta templates/
    }

    @GetMapping("/conteudo")
    public String contentPage() {
        return "deleteContent"; // Retorna deleteContent.html da pasta templates/
    }
}