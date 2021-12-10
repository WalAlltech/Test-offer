package test.offer.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "users")
public class UserEntity {

    private String name;

    private LocalDate birthDate;

    private String country;

    private String phoneNumber;

    private String gender;


}
