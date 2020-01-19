package com.gms.repositorydao;

import com.gms.domain.security.permission.BPermission;
import com.gms.service.db.QueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * In order to get an instance of this class, you must call {@link DAOProvider#getBPermissionDAO()}.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * @see DAOProvider
 */
@Repository
@Transactional
class PostgreSQLBPermissionDAO extends BPermissionDAO {

    /**
     * Creates a new instance of {@link PostgreSQLBPermissionDAO} from the given arguments.
     *
     * @param queryService Instance of {@link QueryService}.
     */
    PostgreSQLBPermissionDAO(final QueryService queryService) {
        super(queryService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BPermission> findPermissionsByUserIdAndEntityId(final long userId, final long entityId) {
        String sql = ""
                + "SELECT p.* "
                + "from "
                + "bauthorization auth "
                + "INNER JOIN euser u ON auth.user_id = u.id "
                + "INNER JOIN eowned_entity e ON auth.entity_id = e.id "
                + "INNER JOIN brole r ON auth.role_id = r.id "
                + "INNER JOIN brole_bpermission rp ON r.id = rp.brole_id "
                + "INNER JOIN bpermission p ON rp.bpermission_id = p.id "
                + "WHERE "
                + "auth.entity_id = :entityId "
                + "AND auth.user_id = :userId "
                + "AND u.enabled = TRUE "                                                       // default disabled
                + "AND (u.account_non_expired = TRUE OR u.account_non_expired IS NULL) "        // default non-expired
                + "AND (u.account_non_locked = TRUE OR u.account_non_locked IS NULL) "          // default non-locked
                + "AND (u.credentials_non_expired = TRUE OR u.credentials_non_expired IS NULL) "// default non-expired
                + "AND r.enabled = TRUE";                                                       // default non-enabled

        final Query query = this.getQueryService().createNativeQuery(sql, BPermission.class);
        query.setParameter("userId", userId).setParameter("entityId", entityId);

        return query.getResultList();
    }

}
