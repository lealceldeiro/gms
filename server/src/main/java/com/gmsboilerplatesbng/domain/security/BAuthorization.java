package com.gmsboilerplatesbng.domain.security;

import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Describes the authorization which has a user over some specific entity(ies) with some role(s)
 */

@Data
@NoArgsConstructor(force = true)
@Entity
public class BAuthorization {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BAuthorizationPk implements Serializable {
        @Column(nullable = false, updatable = false)
        private Long userId;

        @Column(nullable = false, updatable = false)
        private Long entityId;

        @Column(nullable = false, updatable = false)
        private Long roleId;
    }

    @EmbeddedId
    private BAuthorizationPk BAuthorizationPk;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private EUser user;

    @ManyToOne
    @JoinColumn(name = "entityId", insertable = false, updatable = false)
    private EOwnedEntity entity;

    @ManyToOne
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private BRole role;
}
