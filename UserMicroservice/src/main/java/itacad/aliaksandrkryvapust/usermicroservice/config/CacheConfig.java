package itacad.aliaksandrkryvapust.usermicroservice.config;

import itacad.aliaksandrkryvapust.usermicroservice.repository.cache.CacheStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public CacheStorage<Object> tokenCache() {
        return new CacheStorage<>(360, TimeUnit.SECONDS);
    }
}
