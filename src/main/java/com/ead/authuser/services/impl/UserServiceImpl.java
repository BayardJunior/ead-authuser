package com.ead.authuser.services.impl;

import com.ead.authuser.enums.ActionType;
import com.ead.authuser.infrastructure.event.publishers.UserEventPublisher;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEventPublisher publisher;

    @Override
    public List<UserModel> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public void deleteUser(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAllUserPageble(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public UserModel saveAndPublish(UserModel userModel) {
        UserModel saved = this.save(userModel);
        this.publisher.publishUserEvent(saved.convert2UserEventDto(), ActionType.CREATE);
        return saved;
    }

    @Transactional
    @Override
    public void deleteUserAndPublish(UserModel userModel) {
        this.deleteUser(userModel);
        this.publisher.publishUserEvent(userModel.convert2UserEventDto(), ActionType.DELETE);
    }

    @Override
    public UserModel updateUserAndPublish(UserModel userModel) {
        UserModel saved = this.save(userModel);
        this.publisher.publishUserEvent(saved.convert2UserEventDto(), ActionType.UPDATE);
        return saved;
    }

    @Override
    public UserModel updateUserPassword(UserModel userModel) {
        return this.save(userModel);
    }
}
