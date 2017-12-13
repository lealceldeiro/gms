package com.gmsboilerplatesbng.domain.security;

import com.gmsboilerplatesbng.domain.security.ownedentity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * BAuthorization
 * Describes the authorization which has a user over some specific entity(ies) with some role(s).
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@Entity
public class BAuthorization implements Serializable{

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
    private BAuthorizationPk bAuthorizationPk;

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
