package com.dalia.ProjetoDalia.Service;

import com.dalia.ProjetoDalia.Services.EmailService;
import com.dalia.ProjetoDalia.Services.TokenService;
import com.dalia.ProjetoDalia.builders.BuilderUser;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.IUsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties =
        {"spring.data.mongodb.uri=mongodb+srv://AnaBeatriz:YyxZd5W8EdzNyGHt@dalia10.bmanska.mongodb.net/dalia10?retryWrites=true&w=majority&appName=Dalia10"})
public class TISManterUserCRUDTests {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private UsersRepository usersRepository;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        usersRepository.deleteAll();
    }

    @Test
    void ct01_quando_informacoes_validas_cadastro_com_sucesso() {
        // DADO que as informacoes de usuario sao validas
        UsersDTO usuarioParaCadastrar = BuilderUser.umUsuario().buildDTO();

        // QUANDO o usuario e cadastrado pelo service
        UsersDTO usuarioRetornado = usersService.createUser(usuarioParaCadastrar);

        // ENTÃO o usuario e cadastrado com id gerado e dados persistidos
        assertNotNull(usuarioRetornado.id(), "O ID do usuário gerado pelo MongoDB não deve ser nulo");
        assertEquals("Ana", usuarioRetornado.name());
        assertEquals("Silva", usuarioRetornado.surname());
        assertEquals("ana.teste@dalia.com", usuarioRetornado.email());
        assertEquals("MENSTRUACAO", usuarioRetornado.modo());

        // Confirma que a entidade realmente foi gravada no banco
        assertTrue(usersRepository.findById(usuarioRetornado.id()).isPresent());
    }

    @Test
    void ct02_quando_informacoes_validas_retorna_os_detalhes_a_consulta() {
        // DADO que um usuario valido foi previamente cadastrado
        UsersDTO usuarioParaCadastrar = BuilderUser.umUsuario().buildDTO();
        UsersDTO usuarioCadastrado = usersService.createUser(usuarioParaCadastrar);

        // QUANDO consultado por ID atraves da interface IUsersService
        Optional<UsersDTO> usuarioConsultado = usersService.getUserById(usuarioCadastrado.id());

        // ENTÃO os dados corretos devem ser recuperados
        assertTrue(usuarioConsultado.isPresent(), "O usuário deve ser encontrado no banco");
        assertEquals(usuarioCadastrado.id(), usuarioConsultado.get().id());
        assertEquals("Ana", usuarioConsultado.get().name());
        assertEquals("ana.teste@dalia.com", usuarioConsultado.get().email());
    }

    @Test
    void ct03_quando_atualizar_usuario_dados_sao_modificados() {
        // DADO que um usuario existe no sistema
        UsersDTO usuarioCadastrado = usersService.createUser(BuilderUser.umUsuario().buildDTO());

        // QUANDO os dados sao atualizados
        UsersDTO dadosAtualizados = new UsersDTO(
                usuarioCadastrado.id(),
                "Beatriz",
                "Oliveira",
                usuarioCadastrado.email(),
                "NovaSenha123!",
                "NovaSenha123!",
                "GRAVIDEZ",
                true,
                null,
                null
        );
        Optional<UsersDTO> usuarioAtualizado = usersService.updateUser(usuarioCadastrado.id(), dadosAtualizados);

        // ENTÃO as alteracoes devem ser confirmadas
        assertTrue(usuarioAtualizado.isPresent());
        assertEquals("Beatriz", usuarioAtualizado.get().name());
        assertEquals("Oliveira", usuarioAtualizado.get().surname());
        assertEquals("GRAVIDEZ", usuarioAtualizado.get().modo());
    }

    @Test
    void ct04_quando_deletar_usuario_ele_nao_deve_mais_existir() {
        // DADO que um usuario existe no sistema
        UsersDTO usuarioCadastrado = usersService.createUser(BuilderUser.umUsuario().buildDTO());

        // QUANDO o usuario for deletado
        usersService.deleteUser(usuarioCadastrado.id());

        // ENTÃO a busca pelo id deve retornar vazio
        Optional<UsersDTO> usuarioConsultado = usersService.getUserById(usuarioCadastrado.id());
        assertFalse(usuarioConsultado.isPresent(), "O usuário não deve mais existir no banco de dados");
    }
}