package com.gms.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {
    private String key;
    private T value;
}