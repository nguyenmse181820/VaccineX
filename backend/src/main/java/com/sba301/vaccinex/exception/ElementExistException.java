package com.sba301.vaccinex.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElementExistException extends RuntimeException {

    private String message;

    public ElementExistException(String message) {
        this.message = message;
    }

}
