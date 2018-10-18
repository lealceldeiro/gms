package com.gms.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@AllArgsConstructor
public class GmsResponse {

    private String message = "";
    private final ResponseData[] data;

}
