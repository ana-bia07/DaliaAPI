package com.dalia.ProjetoDalia.Controller;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.EmailService;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class PerfilController {
    private final UsersRepository usersRepository;
    private EmailService emailService;
    private PregnancyMonitoringService pregnancyService;

    @PostMapping("/updatePerfil")
    public String updatePerfil(@ModelAttribute("userDTO") UsersDTO userDTO, HttpSession session) {
        String idUser = (String) session.getAttribute("idUser");
        if (idUser == null) {
            return "redirect:/login";
        }

        Optional<Users> userOriginalOpt = usersRepository.findById(idUser);
        if (userOriginalOpt.isPresent()) {
            Users user = userOriginalOpt.get();

            user.setName(userDTO.name());
            user.setSurname(userDTO.surname());
            user.setEmail(userDTO.email());
            if (userDTO.password() == null || userDTO.password().isBlank()) {
            } else {
                user.setPassword(userDTO.password());
            }
            if (user.getSearch() == null) {
                user.setSearch(new Search());
            }
            if (user.getPregnancyMonitoring() == null) {
                user.setPregnancyMonitoring(new PregnancyMonitoring());
            }

            user.getSearch().setAge(userDTO.search().getAge());
            user.getPregnancyMonitoring().setDayPregnancy(userDTO.pregnancyMonitoring().getDayPregnancy());
            user.getPregnancyMonitoring().setGestationWeeks(userDTO.pregnancyMonitoring().getGestationWeeks());
            user.getPregnancyMonitoring().setExpectedBirthDate(userDTO.pregnancyMonitoring().getExpectedBirthDate());

            usersRepository.save(user);

            return "redirect:/perfilG";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/enviar-denuncia")
    public String enviarDenuncia(@RequestParam("mensagem") String mensagem) {
    emailService.enviarDenuncia(mensagem);

    return "redirect:/perfil?denuncia=enviada";
    }

    @GetMapping("/perfilG")
    public String perfilGravidez(Model model, HttpSession session) {
        String idUser = (String) session.getAttribute("idUser");
        if (idUser == null) {
            return "redirect:/login";
        }

        Optional<Users> userOpt = usersRepository.findById(idUser);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            if (user.getSearch() == null) user.setSearch(new Search());
            UsersDTO dto = new UsersDTO(user);
            model.addAttribute("userDTO", dto);
            model.addAttribute("modoAtual", "gravidez");
            return "perfilG"; // nome do arquivo HTML do modo gravidez
        } else {
            return "redirect:/login";
        }
    }

}