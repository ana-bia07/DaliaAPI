package com.dalia.ProjetoDalia.Controller;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Users.SearchService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import com.mongodb.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SearchService searchService;
    private final UsersServices userService;

    @GetMapping("/Mhome")
    public String homePage(Model model, @Nullable Principal principal) {
        if (principal == null) {
            model.addAttribute("userId", "visitante");
            return "homeM1";
        }


        UsersDTO usuario = userService.getByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        model.addAttribute("userId", usuario.id());
        return "homeM1";
    }

    @GetMapping("/saibaMais")
    public String redirectToLandingPage() {
        return "homeM2";
    }



    @ResponseBody
    @GetMapping("/api/ciclo5dias-home/{idUser}")
    public List<Map<String, String>> getEventosCiclo5Dias(@PathVariable String idUser) {
        Optional<SearchDTO> searchOpt = searchService.getSearchByIdUsers(idUser);
        if (searchOpt.isEmpty()) {
            return Collections.emptyList();
        }

        SearchDTO search = searchOpt.get();
        LocalDate ultimaMenstruacao = search.lastMenstruationDay();
        int ciclo = search.cycleDuration();
        int cicloMaisCurto = search.minCycleDuration();
        int cicloMaisLongo = search.maxCycleDuration();

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Map<String, String>> eventos = new ArrayList<>();

        for (int offset = -2; offset <= 2; offset++) {
            LocalDate data = hoje.plusDays(offset);
            String status = null;

            if (searchService.isMenstruacao(data, ultimaMenstruacao)) {
                status = "menstruacao";
            } else if (searchService.isPeriodoFertil(data, ultimaMenstruacao, cicloMaisCurto, cicloMaisLongo)) {
                status = "periodo_fertil";
            } else if (searchService.isOvulacao(data, ultimaMenstruacao, cicloMaisLongo)) {
                status = "ovulacao";
            }

            Map<String, String> evento = new HashMap<>();
            evento.put("data", data.format(formatter));
            if (status != null) {
                evento.put("status", status);
            }
            eventos.add(evento);
        }

        return eventos;
    }
}