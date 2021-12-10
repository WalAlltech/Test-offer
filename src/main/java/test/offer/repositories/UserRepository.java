package test.offer.repositories;

import test.offer.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final MongoTemplate mongoTemplate;

  /**
   * Retrieve a user by username
   *
   * @param name username
   * @return {@link UserEntity} instance
   */
  public UserEntity findOneByName(String name) {
    Query query = new Query();
    query.addCriteria(Criteria.where("name").is(name));
    log.info("Attempting to find user with name : {}", name);
    UserEntity userEntity = mongoTemplate.findOne(query, UserEntity.class);
    if (Objects.nonNull(userEntity)) {
      log.info("User found : {} - {} - {}", userEntity.getName(), userEntity.getCountry(),
          userEntity.getBirthDate());
    } else {
      log.info("No user found with the name : {}", name);
    }
    return userEntity;
  }

  /**
   * Register a user
   *
   * @param user a {@link UserEntity} instance
   * @return {@link UserEntity} registered instance
   * @throws Exception
   */
  public UserEntity create(UserEntity user) throws Exception {
    UserEntity newUser;
    if (!isAdult(user.getBirthDate())) {
      log.error("User must be an adult");
      throw new Exception("User must be an adult");
    }
    if (!isFrench(user.getCountry())) {
      log.error("User must be a french resident");
      throw new Exception("User must be a french resident");
    }
    log.info("Attempting to create user");
    if (Objects.isNull(findOneByName(user.getName()))) {
      newUser = mongoTemplate.insert(user);
      log.info("User created : {} - {} - {}", user.getName(), user.getCountry(),
          user.getPhoneNumber());
    } else {
      log.error("User already exists in database with matriculation : {}", user.getName());
      throw new Exception("User already exists in database");
    }
    return newUser;
  }

  /**
   * Verify if a user is an adult
   *
   * @param date
   * @return true if user is an adult
   */
  public boolean isAdult(final LocalDate date) {
    log.info("Verifying if the user is an adult ");
    final LocalDate today = LocalDate.now();
    return Objects.isNull(date) || (today.minusYears(18).compareTo(date) >= 0);
  }

  /**
   * Verify if a user reside in france
   *
   * @param value
   * @return true if user reside in france
   */
  public boolean isFrench(final String value) {
    log.info("Verifying if the user reside in france ");
    return Objects.isNull(value)
            || (value.equalsIgnoreCase("france")
            || (value.equalsIgnoreCase("french")));
  }

}
