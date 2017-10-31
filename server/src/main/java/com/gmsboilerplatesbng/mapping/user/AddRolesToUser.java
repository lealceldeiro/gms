package com.gmsboilerplatesbng.mapping.user;

import lombok.Data;

import java.util.List;

@Data
public class AddRolesToUser {
    Long userId;
    Long entityId;
    List<Long> rolesId;
}
