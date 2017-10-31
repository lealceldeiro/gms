package com.gmsboilerplatesbng.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class GmsResponse {
    // TODO: define standardized attributes for this response class
    private final String message;
}
