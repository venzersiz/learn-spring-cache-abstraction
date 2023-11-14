package learn.cache.jcache;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    @Test
    void valueAsObject() {
        Cache cache = cacheManager.getCache("a");

        List<Address> addresses = List.of(new Address("주소1"), new Address("주소2"));
        User user1 = new User(1L, "김백세", addresses);
        cache.put("user1", user1);

        User cachedUser1 = (User) cache.get("user1").get();
        assertThat(cachedUser1.getSeq()).isEqualTo(1L);
        assertThat(cachedUser1.getName()).isEqualTo("김백세");
        assertThat(cachedUser1.getAddresses().get(0).getName()).isEqualTo("주소1");
        assertThat(cachedUser1.getAddresses().get(1).getName()).isEqualTo("주소2");
    }

    @RequiredArgsConstructor
    @Getter
    static class Address implements Serializable {

        private final String name;
    }

    @RequiredArgsConstructor
    @Getter
    static class User implements Serializable {

        private final Long seq;

        private final String name;

        private final List<Address> addresses;
    }
}
