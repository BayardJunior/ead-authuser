package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAlluser(SpecificationTemplate.UserSpec userSpec,
                                                      @PageableDefault(page = 0, size = 10, sort = "userId",
                                                              direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> page = userService.findAllUserPageble(userSpec, pageable);
        if (!page.isEmpty()) {
            page.forEach(userModel -> userModel.add(linkTo(methodOn(UserController.class).getUserById(userModel.getUserId())).withSelfRel()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        log.debug("GET getUserById userId received {}", userId);
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("GET getUserById userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        log.debug("GET getUserById userId {}", userId);
        log.info("User userId {} found!", userId);
        return ResponseEntity.status(HttpStatus.OK).body(optionalUserModel.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE deleteUser userId received {}", userId);
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("DELETE deleteUser userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        userService.deleteUser(optionalUserModel.get());
        log.debug("DELETE deleteUser userId deleted {}", userId);
        log.info("User userId {} deleted!", userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted sucessfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        log.debug("PUT updateUser userId received {}", userId);

        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("PUT updateUser userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        log.debug("PUT updateUser userId updated {}", userId);
        log.info("User userId {} deleted!", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        log.debug("PUT updatePassword userId received {}", userId);

        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("PUT updatePassword userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        if (!optionalUserModel.get().getPassword().equals(userDto.getOldPassword())) {
            log.warn("PUT updatePassword userId {} Mismatched old password", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        log.debug("PUT updatePassword userId updated {}", userId);
        log.info("User userId {} updated password!", userId);

        return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {

        log.debug("PUT updateImage userId received {}", userId);

        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("PUT updateImage userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setImageURL(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);
        log.debug("PUT updateImage userId {} updated", userId);
        log.info("User userId {} updated image!", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
}
