package com.gmsboilerplatesbng.util.response;

import lombok.Data;

@Data
public class ResponseData<T> {
    private final String key;
    private final T value;
}