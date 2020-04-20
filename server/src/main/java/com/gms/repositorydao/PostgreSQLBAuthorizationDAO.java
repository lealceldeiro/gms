package com.gms.repositorydao;

import com.gms.domain.security.role.BRole;
import com.gms.service.db.QueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In order to get an instance of this class, you must call {@link DAOProvider#getBAuthorizationDAO()}.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * @see DAOProvider
 */
@Repository
@Transactional
class PostgreSQLBAuthorizationDAO extends BAuthorizationDAO {

    /**
     * Parameter userId.
     */
    private static final String USER_ID_PARAM = "userId";

    /**
     * Native query to select the entity usernames for those whose id is present in the authorization table for the user
     * id provided as argument.
     */
    private static final String SELECT_ENTITY_USERNAMES_FOR_AUTHORIZED_USERS =
            "SELECT e.username "
                    + "FROM eowned_entity e "
                    + "INNER JOIN bauthorization auth ON e.id = auth.entity_id "
                    + "WHERE auth.user_id = :userId";

    /**
     * Native query to select the role data for those whose id is present in the authorization table, that assigned to
     * a user over any entity.
     */
    private static final String SELECT_ROLE_DATA_FROM_THOSE_ROLES_ASSIGNED_TO_A_USER_OVER_ANY_ENTITY =
            "SELECT r.id, r.version, r.label, r.description, r.enabled "
                    + "FROM brole r "
                    + "INNER JOIN bauthorization auth ON r.id = auth.role_id "
                    + "INNER JOIN eowned_entity e ON auth.entity_id = e.id "
                    + "WHERE e.username = :eUsername "
                    + "AND auth.user_id = :userId";
    /**
     * Native query to select the role data for those whose id is present in the authorization table, that assigned to
     * a user over a specific entity.
     */
    private static final String SELECT_ROLE_DATA_FROM_THOSE_ROLES_ASSIGNED_TO_A_USER_OVER_A_SPECIFIC_ENTITY =
            "SELECT r.id, r.version, r.label, r.description, r.enabled "
                    + "FROM brole r "
                    + "INNER JOIN bauthorization auth ON r.id = auth.role_id "
                    + "INNER JOIN eowned_entity e ON auth.entity_id = e.id "
                    + "WHERE e.id = :entityId "
                    + "AND auth.user_id = :userId";

    /**
     * Creates a new instance of a {@link BAuthorizationDAO} from the given arguments.
     *
     * @param queryService An instance of {@link QueryService}.
     */
    PostgreSQLBAuthorizationDAO(final QueryService queryService) {
        super(queryService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<BRole>> getRolesForUserOverAllEntities(final long userId) {
        final Map<String, List<BRole>> rolesForUserOverAllEntities = new HashMap<>();

        final Query queryEntityUsernames =
                this.getQueryService().createNativeQuery(SELECT_ENTITY_USERNAMES_FOR_AUTHORIZED_USERS);
        queryEntityUsernames.setParameter(USER_ID_PARAM, userId);
        final List<String> oEntitiesUsernames = queryEntityUsernames.getResultList();

        List<Object[]> result;
        for (String entityUsername : oEntitiesUsernames) {
            final Query querySelectRoleData = this.getQueryService().createNativeQuery(
                    SELECT_ROLE_DATA_FROM_THOSE_ROLES_ASSIGNED_TO_A_USER_OVER_ANY_ENTITY
            );
            querySelectRoleData.setParameter("eUsername", entityUsername).setParameter(USER_ID_PARAM, userId);
            result = querySelectRoleData.getResultList();
            rolesForUserOverAllEntities.put(entityUsername, getRolesListFrom(result));
        }

        return rolesForUserOverAllEntities;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BRole> getRolesForUserOverEntity(final long userId, final long entityId) {
        Query queryR = this.getQueryService().createNativeQuery(
                SELECT_ROLE_DATA_FROM_THOSE_ROLES_ASSIGNED_TO_A_USER_OVER_A_SPECIFIC_ENTITY
        );
        queryR.setParameter("entityId", entityId).setParameter(USER_ID_PARAM, userId);
        List<Object[]> rawVal = queryR.getResultList();

        return getRolesListFrom(rawVal);
    }

}
