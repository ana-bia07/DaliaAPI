package com.dalia.ProjetoDalia.Controller.Users;


import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;

@Controller
public class CallendarController {

    @GetMapping("/calendar")
    public String exibirCalendario(HttpSession session, Model model) {
        return "calendario";
    }

    @GetMapping("/calendar-data")
    @ResponseBody
    public List<Map<String, Object>> getCalendarData(HttpSession session) {
        Search search = (Search) session.getAttribute("search");

        if (search == null) {
            return List.of(Map.of("error", "Usuário não respondeu a pesquisa"));
        }

        LocalDate ultimaMenstruacao = search.getLastMenstruationDay();
        int duracaoCiclo = search.getCycleDuration();
        int duracaoMenstruacao = search.getMenstruationDuration();

        List<LocalDate> menstruacao = new ArrayList<>();
        List<LocalDate> fertil = new ArrayList<>();
        List<LocalDate> ovulacao = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LocalDate inicioCiclo = ultimaMenstruacao.plusDays((long) i * duracaoCiclo);

            // Menstruação
            for (int j = 0; j < duracaoMenstruacao; j++) {
                menstruacao.add(inicioCiclo.plusDays(j));
            }

            // Ovulação
            LocalDate diaOvulacao = inicioCiclo.plusDays(duracaoCiclo - 14);
            ovulacao.add(diaOvulacao);

            // Fase fértil (5 dias antes até 1 dia depois da ovulação)
            for (int j = -5; j <= 1; j++) {
                fertil.add(diaOvulacao.plusDays(j));
            }
        }

        List<Map<String, Object>> eventos = new ArrayList<>();
        eventos.addAll(agruparDatas(menstruacao, "Menstruação", "#e57373"));
        eventos.addAll(agruparDatas(fertil, "Fase Fértil", "#74c7eb"));
        eventos.addAll(agruparDatas(ovulacao, "Ovulação", "#83ce8f"));

        return eventos;
    }

    private List<Map<String, Object>> agruparDatas(List<LocalDate> datas, String titulo, String cor) {
        List<Map<String, Object>> eventos = new ArrayList<>();
        if (datas.isEmpty()) return eventos;

        datas.sort(Comparator.naturalOrder());

        LocalDate start = datas.get(0);
        LocalDate end = start;

        for (int i = 1; i < datas.size(); i++) {
            LocalDate atual = datas.get(i);
            if (atual.equals(end.plusDays(1))) {
                end = atual;
            } else {
                eventos.add(criarEvento(titulo, start, end, cor));
                start = atual;
                end = atual;
            }
        }

        eventos.add(criarEvento(titulo, start, end, cor));
        return eventos;
    }

    private Map<String, Object> criarEvento(String titulo, LocalDate start, LocalDate end, String cor) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("title", titulo);
        evento.put("start", start.toString());
        evento.put("end", end.plusDays(1).toString()); // end exclusivo no FullCalendar
        evento.put("rendering", "background");
        evento.put("color", cor);
        evento.put("allDay", true);
        return evento;
    }

    @PostMapping("/registrar-menstruacao")
    @ResponseBody
    public Map<String, String> registrarMenstruacao(@RequestBody Map<String, String> body, HttpSession session) {
        String dataStr = body.get("data");
        if (dataStr == null || dataStr.isEmpty()) {
            return Map.of("error", "Data não informada");
        }

        LocalDate novaData = LocalDate.parse(dataStr);

        Search search = (Search) session.getAttribute("search");
        if (search == null) {
            return Map.of("error", "Usuário não respondeu a pesquisa");
        }

        // Atualiza a data da última menstruação
        search.setLastMenstruationDay(novaData);


        // Atualiza o objeto na sessão (se necessário)
        session.setAttribute("search", search);

        return Map.of("status", "ok");
    }
}
