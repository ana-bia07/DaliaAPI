package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.DailyRecord;
import java.time.LocalDateTime;
import java.util.List;

public record DailyRecordDTO(
        String id,
        String idUser,
        LocalDateTime date,
        List<String> mood,
        List<String> habits,
        List<String> symptoms,
        List<String> physical_activity,
        List<String> sex,
        List<String> discharge
) {
    public DailyRecord toEntity() {
        return new DailyRecord(
                null,
                idUser,
                date,
                mood,
                habits,
                symptoms,
                physical_activity,
                sex,
                discharge
        );
    }

    public static DailyRecordDTO fromEntity(DailyRecord daily) {
        return new DailyRecordDTO(
                daily.getId(),
                daily.getIdUser(),
                daily.getDate(),
                daily.getMood(),
                daily.getHabits(),
                daily.getSymptoms(),
                daily.getPhysical_activity(),
                daily.getSex(),
                daily.getDischarge()
        );
    }
}
