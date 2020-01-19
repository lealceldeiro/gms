package com.gms.domain.security;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Describes the authorization which has a user over some specific entity(ies) with some role(s). The public constructor
 * for all argument should be the one used when initializing this class. Never the no-args constructor,
 * since this exists only for orm-handling purposes only.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString(of = {"user", "entity", "role"})
@Entity
public class BAuthorization implements Serializable {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 2509372584237875468L;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BAuthorizationPk implements Serializable {

        /**
         * Version number for a Serializable class.
         */
        private static final long serialVersionUID = -6695307982823733462L;

        /**
         * {@link EUser}'s id to form the primary key.
         */
        @Column(nullable = false, updatable = false, name = "user_id")
        private long userId;

        /**
         * {@link EOwnedEntity}'s id to form the primary key.
         */
        @Column(nullable = false, updatable = false, name = "entity_id")
        private long entityId;

        /**
         * {@link BRole}'s id to form the primary key.
         */
        @Column(nullable = false, updatable = false, name = "role_id")
        private long roleId;

    }

    /**
     * Compounded primary key.
     */
    @EmbeddedId
    private BAuthorizationPk bAuthorizationPk;

    /**
     * {@link EUser} associated to this authorization.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private EUser user;

    /**
     * {@link EOwnedEntity} associated to this authorization.
     */
    @ManyToOne
    @JoinColumn(name = "entity_id", insertable = false, updatable = false)
    private EOwnedEntity entity;

    /**
     * {@link BRole} associated to this authorization.
     */
    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private BRole role;

}
