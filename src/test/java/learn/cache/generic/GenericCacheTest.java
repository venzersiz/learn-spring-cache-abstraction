package learn.cache.generic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.NoOpCache;

@SpringBootTest
class GenericCacheTest {

    @Autowired
    CacheManager cacheManager; // SimpleCacheManager

    @Test
    void simpleTest() {
        Cache cacheA = cacheManager.getCache("a");
        assertThat(cacheA).isInstanceOf(NoOpCache.class);

        cacheA.put("key1", "value1");
        assertThat(cacheA.get("key1")).isNull(); // NoOpCache는 캐쉬로서의 동작을 하지 않음

        Cache cacheB = cacheManager.getCache("b");
        assertThat(cacheB).isInstanceOf(ConcurrentMapCache.class);

        cacheB.put("key2", "value2");
        assertThat(cacheB.get("key2").get()).isEqualTo("value2");

        Cache cacheC = cacheManager.getCache("c");
        assertThat(cacheC).isNull(); // Generic 캐쉬 제공자를 사용하면 빈으로 등록되지 않은 캐쉬 조회 시 null 반환
        // spring.cache.cache-names 프라퍼티에 캐쉬명을 기입하여도 적용되지 않았다
    }
}
