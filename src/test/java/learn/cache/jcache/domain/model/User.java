package learn.cache.jcache.domain.model;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User implements Serializable {

    private final Long seq;

    private final String name;

    private final List<Address> addresses;
}
