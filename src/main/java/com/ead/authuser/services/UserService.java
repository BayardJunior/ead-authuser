package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAllUser();

    Optional<UserModel> findById(UUID userId);

    void deleteUser(UserModel userModel);

    void save(UserModel userModel);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Page<UserModel> findAllUserPageble(Pageable pageable);
}
