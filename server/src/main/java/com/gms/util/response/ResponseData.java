package com.gms.util.response;

import lombok.Data;

/**
 * ResponseData
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
public class ResponseData<T> {
    private final String key;
    private final T value;
}