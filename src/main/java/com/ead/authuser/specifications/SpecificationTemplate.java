package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.UUID;

public class SpecificationTemplate {

    //Pode utilizar o @Or para buscar, dependendo de como os filtros estao sendo montado
    @And({
            @Spec(path = "UserType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class)
    })
    public interface UserSpec extends Specification<UserModel> {
    }

    public static Specification<UserModel> userCourseIdSpec(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<UserModel, UserCourseModel> usersCourses = root.join("userCourses");
            return cb.equal(usersCourses.get("courseId"), courseId);
        };
    }
}
