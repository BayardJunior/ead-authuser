package com.ead.authuser.infrastructure.components;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Log4j2
@Component
public class CourseComponentImpl {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    private final String DEFAULT_URI_COURSE_SERVICES = "http://localhost:8082/";

    public Page<CourseDto> findAllCoursesByUser(UUID userId, Pageable pageable) {

        ResponseEntity<ResponsePageDto<CourseDto>> result = null;
        String url = this.utilsService.getUrlToAllCoursesByUserId(userId, pageable);

        log.debug("Request Url: {}", url);
        log.info("Request Url: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {
            };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            log.debug("Response Number of Elements Url: {}", result.getBody().getContent().size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses: {}", e);
        }
        log.info("Ending request /courses userId {} ", userId);

        return result.getBody();
    }
}
