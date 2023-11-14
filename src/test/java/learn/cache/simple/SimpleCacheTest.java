package learn.cache.simple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

@SpringBootTest
class SimpleCacheTest {

    @Autowired
    CacheManager cacheManager; // ConcurrentMapCacheManager

    @Test
    void getCache() {
        Cache cacheA = cacheManager.getCache("a"); // ConcurrentMapCache
        assertThat(cacheA).isNotNull();

        Cache cacheB = cacheManager.getCache("b");
        assertThat(cacheB).isNotNull();

        Cache cacheC = cacheManager.getCache("c");
        assertThat(cacheC).isNull(); // spring.cache.cache-names 프라퍼티 설정으로 캐쉬를 정의하면 더이상 자동으로 캐쉬를 생성하지 않는다
    }

    @Test
    void getAsValueWrapper() {
        Cache cacheA = cacheManager.getCache("a"); // ConcurrentMapCache
        cacheA.put("key1", null);

        ValueWrapper valueWrapper = cacheA.get("key1");
        assertThat(valueWrapper).isNotNull(); // 해당 키로 매핑된 것이 있지만
        assertThat(valueWrapper.get()).isNull(); // 값 자체는 null이다

        ValueWrapper valueWrapper2 = cacheA.get("key2");
        assertThat(valueWrapper2).isNull(); // 해당 키로 매핑된 것이 없다
    }

    @Test
    void getAsValue() {
        Cache cacheA = cacheManager.getCache("a"); // ConcurrentMapCache
        cacheA.put("key1", null);

        String value = cacheA.get("key1", String.class);
        assertThat(value).isNull(); // 해당 키로 매핑된 것이 null이다

        String value2 = cacheA.get("key2", String.class);
        assertThat(value2).isNull(); // 해당 키로 매핑된 것이 없다
    }

    @Test
    void putAndGet() {
        Cache cacheA = cacheManager.getCache("a"); // ConcurrentMapCache
        cacheA.put("key1", "value1");

        assertThat(cacheA.get("key1").get()).isEqualTo("value1");
    }
}
