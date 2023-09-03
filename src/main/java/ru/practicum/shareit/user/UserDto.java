package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {

    //@NotNull
    private Long id;
    //@NotBlank
    private String name;
    //@NotNull
    @Email(message = "Not valid email")
    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
