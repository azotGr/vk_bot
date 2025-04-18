package com.example.vk_bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config { 
    @Bean
    public CoreBot coreBot(
            @Value("${vk.bot.token}") String token
    ) {
        return new CoreBot(token);
    }
}

