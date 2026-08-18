package com.dalia.ProjetoDalia.Services;

import com.dalia.ProjetoDalia.Model.DTOS.DashboardDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Reports.ReportDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Report;
import com.dalia.ProjetoDalia.Model.Repository.ReportRepository;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Posts.PostsService;
import com.dalia.ProjetoDalia.Services.Report.ReportService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private final UsersRepository usersRepository;
    private final PostsService postsService;
    private final ReportService reportService;

    public AdminService(UsersRepository usersRepository, PostsService postsService, ReportService reportService) {
        this.usersRepository = usersRepository;
        this.postsService = postsService;
        this.reportService = reportService;
    }

    public DashboardDTO getDashboard() {
        int modoGravidez = Math.toIntExact(usersRepository.countByPregnancyMonitoring_IsPregnantTrue());
        int modoMenstruacao = Math.toIntExact(usersRepository.count()) -  modoGravidez;
        List<Report> denuncias = reportService.getAllReports();
        Map<String, Integer> categorias = postsService.countByCategory();

        return new DashboardDTO(modoMenstruacao, modoGravidez, denuncias,categorias);
    }
}
