package com.example.f1driverscatalogue.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {

    private int status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime localDateTime;

    private String url;

    public ApiError(int status, String message, String url) {
        this();
        this.status = status;
        this.message = message;
        this.url = url;
    }

    private ApiError() {
        this.localDateTime = LocalDateTime.now();
    }
}
