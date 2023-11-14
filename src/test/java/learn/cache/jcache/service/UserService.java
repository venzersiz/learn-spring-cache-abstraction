package learn.cache.jcache.service;

import learn.cache.jcache.domain.model.User;
import learn.cache.jcache.domain.repository.DummyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DummyUserRepository userRepository;

    public User findOne(long id) {
        System.out.println("비즈니스 로직");

        return userRepository.findOne(id);
    }
}
