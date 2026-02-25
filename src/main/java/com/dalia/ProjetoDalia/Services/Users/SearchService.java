package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UserCycleDataDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.SearchRepository;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    // Dentro do SearchService

    public Optional<UserCycleDataDTO> getUserCycleData(String userId) {
        return usersRepository.findById(userId)
                .map(Users::getSearch)
                .filter(Objects::nonNull) // Garante que o objeto search existe
                .map(search -> {
                    Integer min = search.getMinCycleDuration();
                    Integer max = search.getMaxCycleDuration();

                    // Retorna um Optional com o novo DTO se os dados existirem
                    if (min != null && max != null) {
                        return new UserCycleDataDTO(min, max);
                    }
                    return null;
                });
    }

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
}