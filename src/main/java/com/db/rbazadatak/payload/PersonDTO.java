package com.db.rbazadatak.payload;

import com.db.rbazadatak.model.Status;
import lombok.Data;

@Data
public class PersonRequest {

    private String name;
    private String surname;
    private String oib;
    private Status status;
}
