package com.gms.domain.security;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Describes the authorization which has a user over some specific entity(ies) with some role(s). The public constructor
 * for all argument should be the one used when initializing this class. Never the no-args constructor, since this exists
 * only for orm-handling purposes only.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
public class BAuthorization implements Serializable{

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BAuthorizationPk implements Serializable {
        @Column(nullable = false, updatable = false, name = "user_id")
        private long userId;

        @Column(nullable = false, updatable = false, name = "entity_id")
        private long entityId;

        @Column(nullable = false, updatable = false, name = "role_id")
        private long roleId;
    }

    @EmbeddedId
    private BAuthorizationPk bAuthorizationPk;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private EUser user;

    @ManyToOne
    @JoinColumn(name = "entity_id", insertable = false, updatable = false)
    private EOwnedEntity entity;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private BRole role;
}
