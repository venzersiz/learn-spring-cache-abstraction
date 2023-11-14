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

        Cache cacheA = cacheManager.getCache("a");
        assertThat(cacheA).isInstanceOf(JCacheCache.class);

        Cache cacheB = cacheManager.getCache("b");
        assertThat(cacheB).isNull();
    }

    @Test
    void putAndGet() {
        Cache cacheA = cacheManager.getCache("a");

        cacheA.put("key1", "value1");
        assertThat(cacheA.get("key1").get()).isEqualTo("value1");

        assertThat(cacheA.get("key2")).isNull();
    }
}
