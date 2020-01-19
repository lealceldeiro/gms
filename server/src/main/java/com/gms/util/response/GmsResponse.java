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

    /**
     * Response message.
     */
    private String message = "";
    /**
     * Array of {@link ResponseData} to be sent.
     */
    private final ResponseData<?>[] data;

}
