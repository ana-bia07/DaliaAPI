package com.dalia.ProjetoDalia.Model.Repository;

import com.dalia.ProjetoDalia.Model.Entity.Users.DailyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRecordRepository extends MongoRepository<DailyRecord, String> {
}
