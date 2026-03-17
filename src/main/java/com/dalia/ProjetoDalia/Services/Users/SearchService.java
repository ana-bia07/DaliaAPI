package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UserCycleDataDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.SearchRepository;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService {

    private final UsersRepository usersRepository;
    private final SearchRepository SearchRepository;

    public Optional<SearchDTO> saveOrUpdateSearchForUser(String userId, SearchDTO searchDTO) {
        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty()) return Optional.empty();

        Users user = userOpt.get();
        Search search = user.getSearch();

        // Se o usuário ainda não tiver pesquisa, cria uma nova
        if (search == null) {
            search = searchDTO.toEntity();
            if (search.getCycleDuration() > 0) {
                search.setMinCycleDuration(search.getCycleDuration());
                search.setMaxCycleDuration(search.getCycleDuration());
            }
        } else {
            // Atualiza os campos existentes
            search.setAge(searchDTO.age());
            search.setRegularMenstruation(searchDTO.regularMenstruation());
            search.setUseContraceptive(searchDTO.useContraceptive());
            search.setContraceptiveType(searchDTO.contraceptiveType());
            search.setLastMenstruationDay(searchDTO.lastMenstruationDay());
            search.setCycleDuration(searchDTO.cycleDuration());
            search.setMenstruationDuration(searchDTO.menstruationDuration());

            // Atualiza histórico
            if (search.getCycleHistory() == null) {
                search.setCycleHistory(new ArrayList<>());
            }
            search.getCycleHistory().add(searchDTO.cycleDuration());
        }

        // Salva a pesquisa no Mongo
        Search savedSearch = SearchRepository.save(search);

        // Vincula ao usuário e salva
        user.setSearch(savedSearch);
        usersRepository.save(user);

        return Optional.of(new SearchDTO(
                savedSearch.getAge(),
                savedSearch.isRegularMenstruation(),
                savedSearch.isUseContraceptive(),
                savedSearch.getContraceptiveType(),
                savedSearch.getLastMenstruationDay(),
                savedSearch.getCycleDuration(),
                savedSearch.getMenstruationDuration(),
                savedSearch.getCycleHistory(),
                savedSearch.getMinCycleDuration(),
                savedSearch.getMaxCycleDuration()
        ));
    }


    @Override
    public boolean deleteSearch(String idUsers) {
        Optional<Users> userOpt = usersRepository.findById(idUsers);
        if (userOpt.isEmpty()) return false;

        Users user = userOpt.get();
        user.setSearch(null);
        usersRepository.save(user);

        return true;
    }

    @Override
    public Optional<SearchDTO> getSearchByIdUsers(String idUsers) {
        return usersRepository.findById(idUsers)
                .map(Users::getSearch)
                .map(search -> new SearchDTO(
                        search.getAge(),
                        search.isRegularMenstruation(),
                        search.isUseContraceptive(),
                        search.getContraceptiveType(),
                        search.getLastMenstruationDay(),
                        search.getCycleDuration(),
                        search.getMenstruationDuration(),
                        search.getCycleHistory(),
                        search.getMinCycleDuration(),
                        search.getMaxCycleDuration()
                ));
    }

    public UserCycleDataDTO registrarCliqueBotao(String userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Search search = user.getSearch();
        if (search == null) throw new RuntimeException("Usuário ainda não fez a pesquisa inicial");

        search.setLastMenstruationDay(LocalDate.now());

        usersRepository.save(user);

        return getStatusCalendario(userId);
    }



    //Calculamos o ciclo atravez do metodo ogino-knaus masio conhecido como tabelinha
    // Verifica se a data está dentro do período da menstruação (5 dias a partir da última menstruação)
    public boolean isMenstruacao(LocalDate data, LocalDate ultimaMenstruacao) {
        LocalDate fimMenstruacao = ultimaMenstruacao.plusDays(4); // 5 dias (0 a 4)
        return !data.isBefore(ultimaMenstruacao) && !data.isAfter(fimMenstruacao);
    }

    // Verifica se a data está no período fértil (dias -5 a 0 antes da ovulação)
    public boolean isPeriodoFertil(LocalDate data, LocalDate ultimaMenstruacao, int cicloMaisCurto, int cicloMaisLongo) {
        // Subtrai 18 do ciclo mais curto para achar o início do período fértil
        int inicioPeriodoFertil = cicloMaisCurto - 18;
        // Subtrai 11 do ciclo mais longo para encontrar o fim
        int fimPeriodoFertil = cicloMaisLongo - 11;

        LocalDate dataInicioFertil = ultimaMenstruacao.plusDays(inicioPeriodoFertil);
        LocalDate dataFimFertil = ultimaMenstruacao.plusDays(fimPeriodoFertil);

        return !data.isBefore(dataInicioFertil) && !data.isAfter(dataFimFertil);
    }

    // Verifica se a data é o dia da ovulação
    public boolean isOvulacao(LocalDate data, LocalDate ultimaMenstruacao, int cicloMaisLongo) {
        LocalDate ovulacao = calcularDiaOvulacao(ultimaMenstruacao, cicloMaisLongo);
        return data.isEqual(ovulacao);
    }

    public LocalDate calcularDiaOvulacao(LocalDate ultimaMenstruacao, int cicloMaisLongo) {
        // Calcula o fim do período fértil (ciclo mais longo - 11)
        int fimPeriodoFertil = cicloMaisLongo - 11;
        LocalDate dataFimFertil = ultimaMenstruacao.plusDays(fimPeriodoFertil);

        // O dia da ovulação é 2 dias antes do fim do período fértil
        return dataFimFertil.minusDays(2);
    }

    public UserCycleDataDTO getStatusCalendario(String userId){
        Users user = usersRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        //pega pesquisa
        var search = user.getSearch();
        //pega data de hoje
        LocalDate hoje =LocalDate.now();
        //pega a duração do ciclo maximo e o minino e a ultima a data menstruação
        int min = search.getMinCycleDuration();
        int max = search.getMaxCycleDuration();
        LocalDate ultimaM = search.getLastMenstruationDay();
        // recebe true ou false de acordo com os metodos
        boolean m = isMenstruacao(hoje, ultimaM);
        boolean f = isPeriodoFertil(hoje, ultimaM, min, max);
        boolean o = isOvulacao(hoje, ultimaM, max);
        //cria uma varivel atraso para os dias que era pra menstrar e não foi registrado
        //menstruação
        long atrasos = 0;
        LocalDate previsao =  ultimaM.plusDays(search.getCycleDuration());
        //se hoje for depois do dia da previsãoe diferente de menstraução true
        if(hoje.isAfter(previsao) && !m){
            //add dias de atraso
            atrasos = java.time.temporal.ChronoUnit.DAYS.between(previsao, hoje);
        }

        LocalDate fimM = ultimaM.plusDays(search.getMenstruationDuration() - 1);
        LocalDate iniFertil = ultimaM.plusDays(min - 18);
        LocalDate fimFertil = ultimaM.plusDays(max - 11);
        LocalDate diaOvu = fimFertil.minusDays(2);

        return new UserCycleDataDTO(min, max, ultimaM, m, f, o, atrasos,
                fimM, iniFertil, fimFertil, diaOvu);
    }
}