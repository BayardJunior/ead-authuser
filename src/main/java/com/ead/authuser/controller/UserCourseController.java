package com.ead.authuser.controller;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.SubscriptionCourseDto;
import com.ead.authuser.infrastructure.components.CourseComponentImpl;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/courses")
public class UserCourseController {


    @Autowired
    UserCourseService service;

    @Autowired
    CourseComponentImpl courseComponent;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<CourseDto>> findAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                                @PathVariable(value = "userId") UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(this.courseComponent.findAllCoursesByUser(userId, pageable));
    }


    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscrptionUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                              @RequestBody @Valid SubscriptionCourseDto subscriptionCourseDto) {

        log.debug("POST saveSubscrptionUserInCourse userId received {}", userId);
        Optional<UserModel> optionalUserModel = this.userService.findById(userId);
        if (!optionalUserModel.isPresent()) {
            log.warn("POST saveSubscrptionUserInCourse userId {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        log.debug("POST saveSubscrptionUserInCourse userId {}", userId);
        log.info("User userId {} found!", userId);
        if (this.service.existsUserIdInCourse(optionalUserModel.get(), subscriptionCourseDto.getCourseId())) {
            log.error("Course {} already subscripted for this user {}!", subscriptionCourseDto.getCourseId(), userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        UserCourseModel userCourseModel = this.service.save(optionalUserModel.get().convertToCourseUserModel(subscriptionCourseDto.getCourseId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

}
