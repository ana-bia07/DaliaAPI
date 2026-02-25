package com.dalia.ProjetoDalia.Model.DTOS.Users;

public record UserCycleDataDTO (
    int minCycleDuration,
    int maxCycleDuration
){
    public int getMinCycleDuration() {
        return minCycleDuration;
    }

    public int getMaxCycleDuration() {
        return maxCycleDuration;
    }
}
