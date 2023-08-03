package com.ead.authuser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseRepository, UUID>, JpaSpecificationExecutor<UserCourseRepository> {

}
