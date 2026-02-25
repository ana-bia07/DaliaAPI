package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Gravidez")
public class HomeGravidezController {

    private final PregnancyMonitoringService pregnancyService;

    @GetMapping("/homeGravidez")
    public String showPregnancyPage(Model model, HttpSession session) {
        String idUser = (String) session.getAttribute("idUser");
        if (idUser == null) {
            return "redirect:/login";
        }

        PregnancyMonitoringDTO dto = pregnancyService.getPregnancyByIdUser(idUser)
                .orElse(new PregnancyMonitoringDTO());

        model.addAttribute("pregnancy", dto);
        model.addAttribute("idUser", idUser);
        // Converter lista de consultas em string
        if (dto.consultations() != null && !dto.consultations().isEmpty()) {
            String consultationsString = String.join(", ", dto.consultations());
            model.addAttribute("consultationsString", consultationsString);
        } else {
            model.addAttribute("consultationsString", "");
        }

        return "gravidez";
    }

    @PostMapping("/save")
    public String savePregnancy(
            @RequestParam String idUser,
            @ModelAttribute("pregnancy") PregnancyMonitoringDTO dto,
            @RequestParam(value = "consultationsString", required = false) String consultationsString) {

        List<String> consultations = (consultationsString != null && !consultationsString.isBlank())
                ? Arrays.stream(consultationsString.split(","))
                .map(String::trim)
                .toList()
                : List.of();

        // Criar novo DTO com as consultas incluídas
        PregnancyMonitoringDTO updatedDto = new PregnancyMonitoringDTO(
                dto.isPregnant(),
                dto.dayPregnancy(),
                dto.gestationWeeks(),
                dto.expectedBirthDate(),
                consultations
        );

        pregnancyService.updatePregnancy(idUser, updatedDto);

        return "redirect:/Gravidez/homeGravidez";
    }
}
