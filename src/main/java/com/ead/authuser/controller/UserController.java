package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAlluser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUser());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalUserModel.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        userService.deleteUser(optionalUserModel.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted sucessfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        if (!optionalUserModel.get().getPassword().equals(userDto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        UserModel userModel = optionalUserModel.get();
        userModel.setImageURL(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
}
