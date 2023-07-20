package com.db.rbazadatak.payload;

import com.db.rbazadatak.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object representing a person.")
public class PersonDTO {

    @Schema(description = "First name of the person.", example = "John")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Last name of the person.", example = "Doe")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Unique identification number of the person (OIB).", example = "83972579593")
    @NotBlank(message = "OIB is required")
    private String oib;

    @Schema(description = "Status of the person.", example = "ACTIVE")
    @NotNull(message = "Status is required")
    private Status status;
}
