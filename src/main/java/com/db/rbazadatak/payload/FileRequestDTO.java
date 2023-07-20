package com.db.rbazadatak.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object representing a person.")
public class FileRequestDTO {

    @Schema(description = "Unique identification number of the person (OIB).", example = "83972579593")
    private String oib;

    @Schema(description = "Status of the person.", example = "ACTIVE")
    private String status;
}
