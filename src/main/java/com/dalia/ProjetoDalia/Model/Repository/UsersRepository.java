package com.dalia.ProjetoDalia.Model.Repository;

import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByName(String name);

    Optional<Users> findById(String id);
}


