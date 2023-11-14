package learn.cache.jcache;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.cache.jcache.JCacheCacheManager;

@SpringBootTest
class EhcacheTest {

    @Autowired
    CacheManager cacheManager;

    @Test
    void getCache() {
        assertThat(cacheManager).isInstanceOf(JCacheCacheManager.class); // Eh107CacheManager

        Cache cacheA = cacheManager.getCache("a"); // application.yml
        assertThat(cacheA).isInstanceOf(JCacheCache.class);

        Cache cacheB = cacheManager.getCache("b"); // ehcache.xml
        assertThat(cacheB).isInstanceOf(JCacheCache.class);

        Cache cacheC = cacheManager.getCache("c"); // ehcache.xml
        assertThat(cacheC).isInstanceOf(JCacheCache.class);
    }

    @Test
    void putAndGet() {
        Cache cacheA = cacheManager.getCache("a"); // application.yml

        cacheA.put("key1", "value1");
        assertThat(cacheA.get("key1").get()).isEqualTo("value1");
        assertThat(cacheA.get("key2")).isNull();

        Cache cacheB = cacheManager.getCache("b"); // ehcache.xml

        cacheB.put("key1", "value1");
        assertThat(cacheB.get("key1").get()).isEqualTo("value1");
        assertThat(cacheB.get("key2")).isNull();
    }

    @Test
    void expiry() throws InterruptedException {
        Cache cache = cacheManager.getCache("usingExpiry"); // TTL: 5초

        cache.put("key1", "value1");
        assertThat(cache.get("key1").get()).isEqualTo("value1");

        Thread.sleep(5_000);

        assertThat(cache.get("key1")).isNull(); // 캐쉬 만료됨
    }
}
