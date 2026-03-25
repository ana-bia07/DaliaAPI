package com.dalia.ProjetoDalia.Services.Interface;

import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;

import java.util.Optional;

public interface IUsersService {
    public UsersDTO createUser(UsersDTO usersDTO);
    public Optional<UsersDTO> updateUser(String idUser, UsersDTO usersDTO);
    public void deleteUser(String idUser);
    public Optional<UsersDTO> getUserById(String id);
}
