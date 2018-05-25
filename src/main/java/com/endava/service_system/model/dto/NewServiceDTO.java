package com.endava.service_system.model.dto;

import com.endava.service_system.constraints.ServiceTitleInUseConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ServiceTitleInUseConstraint.List({@ServiceTitleInUseConstraint(fieldName = "title", id = "id")})
public class NewServiceDTO {

        @NotNull(message = "Please, provide a title for new service")
        @Pattern(regexp = "^[^<'\">]+$",message = "Please, do not use tags and double quotes")
        private String title;

        @NotNull
        @NotEmpty
        @Pattern(regexp = "^[^<'\">]+$",message = "Please, do not use tags and double quotes")
        private String category;

        @NotNull
        @NotEmpty
        @Pattern(regexp = "^[^<'\">]+$",message = "Please, do not use tags and double quotes")
        private String description;

        @NotNull
        @DecimalMin(value = "0.00", message = "Please, provide a number greater than 0.0")
        @DecimalMax("999999999999.00")
        private BigDecimal price;

        long id;
}
