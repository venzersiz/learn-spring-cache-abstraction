package learn.cache.jcache;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import learn.cache.jcache.domain.model.Address;
import learn.cache.jcache.domain.model.User;
import learn.cache.jcache.service.UserService;
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

    @Autowired
    UserService userService;

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

    @Test
    void declarativeAnnotationBasedCaching() {
        User user = userService.findOne(1L);
        // 1. 비즈니스 로직
        // 2. DB 조회
        // -> 캐쉬에 없어 DB 조회

        assertThat(user.getSeq()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("김백세");
        assertThat(user.getAddresses().get(0).getName()).isEqualTo("주소1");
        assertThat(user.getAddresses().get(1).getName()).isEqualTo("주소2");

        User cachedUser = userService.findOne(1L);
        // 1. 비즈니스 로직
        // -> 캐쉬에 있어 DB 조회 X
        assertThat(user).isNotEqualTo(cachedUser); // 캐쉬 값을 가져오긴 하는데 인스턴스 주소값이 다르다

        userService.findOne(2L);
        // 1. 비즈니스 로직
        // 2. DB 조회
        // -> 캐쉬에 없어 DB 조회
    }
}
