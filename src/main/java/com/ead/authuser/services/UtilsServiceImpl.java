package com.ead.authuser.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    public String getUrlToAllCoursesByUserId(UUID userId, Pageable pageable) {
        return "/courses?userId="
                .concat(userId.toString())
                .concat("&page=".concat(String.valueOf(pageable.getPageNumber())))
                .concat("&size=".concat(String.valueOf(pageable.getPageSize())))
                .concat("&sort=").concat(pageable.getSort().toString().replaceAll(": ", ","));
    }
}
