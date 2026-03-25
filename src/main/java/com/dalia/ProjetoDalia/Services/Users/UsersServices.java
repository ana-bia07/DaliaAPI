package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.LoginDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.VerificationDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.EmailService;
import com.dalia.ProjetoDalia.Services.Interface.IUsersService;
import com.dalia.ProjetoDalia.Services.TokenService;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UsersServices implements IUsersService{

    private final UsersRepository usersRepository;
    private final EmailService emailService;
    public final BCryptPasswordEncoder passwordEncoder;
    public final TokenService tokenService;

    public UsersServices(UsersRepository usersRepository, EmailService emailService, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usersRepository = usersRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    @Override
    public UsersDTO createUser(UsersDTO usersDTO) {
        var user = usersDTO.toEntity();

        //criptografia de senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());

        //cria token e coloca no banco
        String token = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationToken(token);
        user.setTokenExpirantion((LocalDateTime.now().plusMinutes(15)));
        var savedUser = usersRepository.save(user);

        //envia email do token
        emailService.sendToken(savedUser.getEmail(),token);

        return new UsersDTO(savedUser);
    }
    @Override
    public Optional<UsersDTO> getUserById(String id) {
        Optional<Users> userEntity = usersRepository.findById(id);
        return userEntity.map(UsersDTO::new);
    }

    @Override
    public Optional<UsersDTO> updateUser(String id, UsersDTO usersDTO) {
        Optional<Users> existingUserOpt = usersRepository.findById(id);

        return existingUserOpt.map(existingUser -> {
            updateUserFields(existingUser, usersDTO);
            Users updatedUser = usersRepository.save(usersDTO.toEntity());
            return new UsersDTO(updatedUser);
        });
    }

    @Override
    public void deleteUser(String id) {
        usersRepository.deleteById(id);
    }

    public Optional<UsersDTO> getByEmail(String email) {
        Optional<Users> userEntity = usersRepository.findByEmail(email);
        return userEntity.map(UsersDTO::new);
    }

    private Optional<Users> updateUserFields(Users existingUser, UsersDTO usersDTO) {
        if (StringUtils.hasText(usersDTO.id())) existingUser.setId(usersDTO.id());
        if (StringUtils.hasText(usersDTO.name())) existingUser.setName(usersDTO.name());
        if (StringUtils.hasText(usersDTO.surname())) existingUser.setSurname(usersDTO.surname());
        if (StringUtils.hasText(usersDTO.email())) existingUser.setEmail(usersDTO.email());
        if (StringUtils.hasText(usersDTO.password())) existingUser.setPassword(usersDTO.password());
        if (usersDTO.search() != null) existingUser.setSearch(usersDTO.search());
        if (usersDTO.pregnancyMonitoring() != null) existingUser.setPregnancyMonitoring(usersDTO.pregnancyMonitoring());

        usersRepository.save(existingUser);
        return Optional.of(existingUser);
    }

    //verica email
    public String verifyEmail(VerificationDTO verificationDTO){
        var userOptional = usersRepository.findByEmail(verificationDTO.email());
        if(userOptional.isEmpty()){
            return "Usuaria não encontrada";
        }

        Users user = userOptional.get();

        if(user.getVerificationToken().equals(verificationDTO.token()) &&
        user.getTokenExpirantion().isAfter(LocalDateTime.now())){
            user.setEnable(true);
            user.setVerificationToken(null);
            usersRepository.save(user);

            return "E-mail verificado com sucesso!";
        }

        return "Codigo invalido";
    }

    //faz o login
    public String login(LoginDTO loginDTO){
        var usersOptional = usersRepository.findByEmail(loginDTO.email());

        if(usersOptional.isEmpty()){
            return "E-mail invalido";
        }

        Users user = usersOptional.get();
        //verifica se ja foi validado o email
        if(!user.isEnable()){
            return "Precisa confirmar o email";
        }
        //compara a senha com o banco
        if(passwordEncoder.matches(loginDTO.password(), user.getPassword())){
            return tokenService.generateToken(user);
        }

        return "E-mail ou senha invalido";
    }
}
