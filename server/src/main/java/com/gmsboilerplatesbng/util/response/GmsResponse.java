package com.gmsboilerplatesbng.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * GmsResponse
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GmsResponse {

    private String message = "";
    private final ResponseData[] data;
}
