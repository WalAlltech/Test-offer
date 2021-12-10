package test.offer.controller;

import test.offer.model.UserResource;
import test.offer.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * Retrieve user by username
   *
   * @param matriculation id
   * @return user
   */
  @GetMapping("/{name}")
  public ResponseEntity<UserResource> getUser(@PathVariable("name") String matriculation) {
    return Optional.ofNullable(userService.getUser(matriculation))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Register a user. Throw an Exception if already exists in database.
   *
   * @param car a {@link UserResource} to be registered
   * @return a {@link UserResource} registered
   */
  @PostMapping("/create")
  public ResponseEntity<Object> create(@RequestBody @Validated UserResource car) {
    try {
      return new ResponseEntity<>(userService.create(car), HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
