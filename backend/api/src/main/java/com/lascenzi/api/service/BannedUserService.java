package com.lascenzi.api.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BannedUserService
{
    private final Set<String> bannedUsers = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "banned-users", groupId = "springboot-ban-consumer")
    public void onBannedUser(String userId) 
    {
        bannedUsers.add(userId);
        System.out.println("Banned user: " + userId);
    }

    public boolean isBanned(String userId) 
    {
        return bannedUsers.contains(userId);
    }
}
