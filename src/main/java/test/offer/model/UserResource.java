package test.offer.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UserResource {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String country;

    private String phoneNumber;

    private String gender;

}
