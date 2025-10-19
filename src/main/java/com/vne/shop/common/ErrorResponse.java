package com.vne.shop.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Стандартная ошибка API")
public class ErrorResponse {
    @Schema(description = "Код ошибки", example = "не найдено")
    private String error;
    @Schema(description = "Сообщение об ошибке", example = "Товар 123 не найден")
    private String message;

    public ErrorResponse() {}
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
