package com.gmsboilerplatesbng.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@Data
@MappedSuperclass
public class GmsEntity implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;

    @Version
    protected Integer version;
}
