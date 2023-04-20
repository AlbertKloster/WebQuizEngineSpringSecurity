package engine.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record UserEmailPasswordDTO(
        @NotNull
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,
        @NotNull
        @Size(min = 5)
        String password
) {
}
