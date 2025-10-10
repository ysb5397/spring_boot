package com.puzzlix.solid_task._global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponseDto<T> {

    private boolean isSuccess;
    private String message;
    private T data;

    public static <T> CommonResponseDto<T> success(T data, String message) {
        return new CommonResponseDto<>(true, message, data);
    }

    public static <T> CommonResponseDto<T> success(T data) {
        return success(data, null);
    }

    public static <T> CommonResponseDto<T> error(String message) {
        return new CommonResponseDto<>(false, message, null);
    }
}
