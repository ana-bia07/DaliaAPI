package com.dalia.ProjetoDalia.Model.Entity;

import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments {
    private String idUsers;
    private String comment;
    private Instant createdAt;

}
