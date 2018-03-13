package com.gms.service.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.util.constant.BPermissionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * PermissionService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

    private final BPermissionRepository repository;

    //region default permissions

    /**
     * Creates the default Permissions.
     * @return <code>true</code> if all the permissions where created successfully, <code>false</code> otherwise.
     */
    public boolean createDefaultPermissions() {
        boolean ok = true;
        final BPermissionConst[] constPermissions = BPermissionConst.values();
        for (BPermissionConst p : constPermissions) {
            ok = ok && repository.save(
                    new com.gms.domain.security.permission.BPermission(
                            p.toString(), p.toString().replaceAll("__", " ").replaceAll("_", " ")
                    )
            ) != null;
        }
        return ok;
    }
    //endregion

    /**
     * Lists all permissions associated to a {@link com.gms.domain.security.user.EUser} over an
     * {@link com.gms.domain.security.ownedentity.EOwnedEntity} through some role(s).
     * @param userId {@link com.gms.domain.security.user.EUser#id}
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity#id}
     * @return A {@link List} of {@link BPermission} with all the found permissions.
     */
    public List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId) {
        return repository.findPermissionsByUserIdAndEntityId(userId, entityId);
    }
}
