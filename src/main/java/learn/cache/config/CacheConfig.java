package learn.cache.config;

import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.NoOpCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Cache cacheA() {
        return new NoOpCache("a"); // 아무 동작을 하지 않는 캐쉬
    }

    @Bean
    public Cache cacheB() {
        return new ConcurrentMapCache("b");
    }
}
