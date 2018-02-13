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
    public Boolean createDefaultPermissions() {
        Boolean ok = true;
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

    public List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId) {
        return repository.findPermissionsByUserIdAndEntityId(userId, entityId);
    }
}
