package test.offer.services;

import test.offer.mapper.UserMapper;
import test.offer.model.UserResource;
import test.offer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserMapper mapper;

  /**
   * Retrieve a user by username
   *
   * @param name name
   * @return a {@link UserResource} founded
   */
  public UserResource getUser(String name) {
    return mapper.carEntityToCarResource(repository.findOneByName(name));
  }

  /**
   * Register a user
   *
   * @param user a {@link UserResource} instance
   * @return a {@link UserResource} registered instance
   * @throws Exception if user already exists in database
   */
  public UserResource create(UserResource user) throws Exception {
    return mapper.carEntityToCarResource(repository.create(mapper.carResourceToCarEntity(user)));
  }

}
