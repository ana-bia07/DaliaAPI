package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.IUsersService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UsersServices implements IUsersService{

    private final UsersRepository usersRepository;

    public UsersServices(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UsersDTO createUser(UsersDTO usersDTO) {
        var user = usersDTO.toEntity();
        var savedUser = usersRepository.save(user);
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
}
