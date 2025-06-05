package library.dto;

import java.time.ZonedDateTime;

public class ErrorResponseDTO {
    private String error;
    private int status;
    private ZonedDateTime timestamp;

    public ErrorResponseDTO(String error, int status, ZonedDateTime timestamp) {
        this.error = error;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getError() { return error; }
    public int getStatus() { return status; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}