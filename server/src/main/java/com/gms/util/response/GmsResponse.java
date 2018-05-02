package com.gms.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GmsResponse {
    private String message = "";
    private final ResponseData[] data;
}
