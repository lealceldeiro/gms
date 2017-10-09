package com.gmsboilerplatesbng.domain.secuirty;

import com.gmsboilerplatesbng.domain.secuirty.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.domain.secuirty.role.BRole;
import com.gmsboilerplatesbng.domain.secuirty.user.EUser;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Describes the authorization which has a user over some specific entity(ies) with some role(s)
 */

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
public class BAuthorization {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    private static class Pk implements Serializable {
        @Column(nullable = false, updatable = false)
        private Long userId;

        @Column(nullable = false, updatable = false)
        private Long entityId;

        @Column(nullable = false, updatable = false)
        private Long roleId;
    }

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private EUser userId;
    @ManyToOne
    @JoinColumn(name = "entity_id", insertable = false, updatable = false)
    private EOwnedEntity entityId;
    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private BRole roleId;
}
