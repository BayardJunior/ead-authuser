package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAllUser();

    Optional<UserModel> findById(UUID userId);

    void deleteUser(UserModel userModel);

    UserModel save(UserModel userModel);

    boolean existsByUsername(String userName);

    boolean existsByEmail(String email);

    Page<UserModel> findAllUserPageble(Specification<UserModel> spec, Pageable pageable);

    UserModel saveAndPublish(UserModel userModel);

    void deleteUserAndPublish(UserModel userModel);

    UserModel updateUserAndPublish(UserModel userModel);

    UserModel updateUserPassword(UserModel userModel);
}
