package com.gms.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@MappedSuperclass
public class GmsEntity implements Serializable {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = -7947488456967226793L;

    /**
     * Entity's id.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Entity's version. Used by Hibernate.
     */
    @Version
    private Integer version;

}
