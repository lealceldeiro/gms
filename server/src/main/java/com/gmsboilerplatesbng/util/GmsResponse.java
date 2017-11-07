package com.gmsboilerplatesbng.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GmsResponse {

    private String message = "";
    private final ResponseData[] data;
}
