package edu.cnm.deepdive.crossfyre.service.dao;

import edu.cnm.deepdive.crossfyre
    .model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByOauthKey(String oauthKey);

  Optional<User> findByExternalKey(UUID key);

}
