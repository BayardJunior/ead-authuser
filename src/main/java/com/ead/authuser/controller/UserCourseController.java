package com.ead.authuser.controller;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.infrastructure.components.CourseComponentImpl;
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

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/courses")
public class UserCourseController {

    @Autowired
    CourseComponentImpl courseComponent;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<CourseDto>> findAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                                @PathVariable(value = "userId") UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(this.courseComponent.findAllCoursesByUser(userId, pageable));
    }

}
