package com.gms.repository.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.db.QueryService;
import lombok.RequiredArgsConstructor;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * BPermissionRepositoryImpl
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 13, 2018
 */
@RequiredArgsConstructor
@Transactional
public class BPermissionRepositoryImpl implements BPermissionRepositoryCustom {

    private final QueryService queryService;

    /**
     * Return the list of permissions associated to some role(s). This (theses) role(s) is(are) the one(s) associated to
     * a user over an entity. Some conditions must be met in order to return the permissions:
     * - The following properties from the user must be true: <code>enabled</code>, <code>accountNonExpired</code>,
     * <code>accountNonLocked</code> and <code>credentialsNonExpired</code>
     * - The following properties from the role must be true: <code>enabled</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId) {
        String sql = "" +
                "SELECT p.* " +
                "from" +
                "  bauthorization auth" +
                "  INNER JOIN euser u ON auth.user_id = u.id" +
                "  INNER JOIN eowned_entity e ON auth.entity_id = e.id" +
                "  INNER JOIN brole r ON auth.role_id = r.id" +
                "  INNER JOIN brole_bpermission rp ON r.id = rp.brole_id" +
                "  INNER JOIN bpermission p ON rp.bpermission_id = p.id " +
                "WHERE" +
                "  auth.entity_id = :entityId" +
                "  AND auth.user_id = :userId" +
                "  AND u.enabled = TRUE" +                                                          // by default users are not enabled
                "  AND (u.account_non_expired = TRUE OR u.account_non_expired IS NULL)" +            // by default users' account are not expired
                "  AND (u.account_non_locked = TRUE OR u.account_non_locked IS NULL)" +              // by default users are not locked
                "  AND (u.credentials_non_expired = TRUE OR u.credentials_non_expired IS NULL)" +    // by default users' credentials are not expired
                "  AND r.enabled = TRUE";                                                           // by default roles are not enabled

        final Query query = queryService.createNativeQuery(sql, BPermission.class);
        query.setParameter("userId", userId).setParameter("entityId", entityId);

        return query.getResultList();
    }
}
