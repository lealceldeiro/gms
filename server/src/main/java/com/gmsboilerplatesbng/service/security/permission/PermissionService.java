package com.gmsboilerplatesbng.service.security.permission;

import com.gmsboilerplatesbng.repository.security.permission.BPermissionRepository;
import com.gmsboilerplatesbng.util.constant.BPermissionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * PermissionService
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
public class PermissionService {

    private final BPermissionRepository repository;


    @Autowired
    public PermissionService(BPermissionRepository repository) {
        this.repository = repository;
    }

    //region default permissions
    public Boolean createDefaultPermissions() {
        Boolean ok = true;
        final BPermissionConst[] constPermissions = BPermissionConst.values();
        for (BPermissionConst p : constPermissions) {
            ok = ok && repository.save(
                    new com.gmsboilerplatesbng.domain.security.permission.BPermission(
                            p.toString(), p.toString().replaceAll("__", " ").replaceAll("_", " ")
                    )
            ) != null;
        }
        return ok;
    }
    //endregion

}
