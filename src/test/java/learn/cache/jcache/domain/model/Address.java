package learn.cache.jcache.domain.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Address implements Serializable {

    private final String name;
}
