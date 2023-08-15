package com.ead.authuser.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String getUrlToAllCoursesByUserId(UUID userId, Pageable pageable);

    String deleteUserInCourseUrl(UUID userId);
}
