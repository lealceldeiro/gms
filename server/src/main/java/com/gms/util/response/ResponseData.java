package com.gms.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <T> Type of information to be sent.
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {

    /**
     * Key to be used to map the information.
     */
    private String key;

    /**
     * Information to be sent.
     */
    private T value;

}
