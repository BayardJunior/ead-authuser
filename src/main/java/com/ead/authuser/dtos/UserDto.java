package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //oculta os att com valores nulo a nivel de classe
public class UserDto {

    /**
     * UserView é utilizado quando se quer utilizar o mesmo DTO para outras finalidades,
     * precisa entender melhor o contexto em que se vai utilizar.
     */
    public interface UserView {
        public static interface RegistrationPost {
        }

        public static interface UserPut {
        }

        public static interface PasswordPut {
        }

        public static interface ImagePut {
        }
    }

    private UUID userId;
    @JsonView(UserView.RegistrationPost.class)
    private String userName;
    @JsonView(UserView.RegistrationPost.class)
    private String email;
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;
    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;
}
