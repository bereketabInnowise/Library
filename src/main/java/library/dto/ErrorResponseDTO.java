package library.dto;

import java.time.ZonedDateTime;

public class ErrorResponseDTO {
    private String error;
    private int status;


    public ErrorResponseDTO(String error, int status) {
        this.error = error;
        this.status = status;

    }

    // Getters
    public String getError() { return error; }
    public int getStatus() { return status; }

}