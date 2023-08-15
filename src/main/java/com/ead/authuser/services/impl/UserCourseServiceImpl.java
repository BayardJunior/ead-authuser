package com.ead.authuser.services.impl;

import com.ead.authuser.infrastructure.components.CourseComponentImpl;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    UserCourseRepository repository;

    @Autowired
    CourseComponentImpl courseComponent;

    @Override
    public boolean existsUserIdInCourse(UserModel userModel, UUID courseId) {

        return this.repository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {

        return this.repository.save(userCourseModel);
    }

    @Override
    public void delete(UserModel userModel) {
        List<UserCourseModel> userCourseModel = this.repository.findAllUserCourseModelIntoUser(userModel.getUserId());
        if (!userCourseModel.isEmpty()) {
            this.courseComponent.deleteUserInCourse(userModel.getUserId());
        }
        this.repository.deleteAll(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {

        return this.repository.existsByCourseId(courseId);
    }

    @Transactional
    @Override
    public void deleteUserCourseByCourse(UUID courseId) {
        this.repository.deleteAllByCourseId(courseId);
    }

}
