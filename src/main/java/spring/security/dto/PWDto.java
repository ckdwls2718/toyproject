package spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PWDto {

    @NotBlank
    private String beforePW;

    @NotBlank
    private String newPW;
}
