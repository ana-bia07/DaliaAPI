package com.dalia.ProjetoDalia.Model.Repository;

import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends MongoRepository<Search, String> {
}
