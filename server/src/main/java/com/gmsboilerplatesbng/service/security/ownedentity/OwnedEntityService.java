package com.gmsboilerplatesbng.service.security.ownedentity;

import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * OwnedEntityService
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
public class OwnedEntityService {

    private final DefaultConst c;

    private final EOwnedEntityRepository entityRepository;


    @Autowired
    public OwnedEntityService(EOwnedEntityRepository entityRepository, DefaultConst defaultConst) {
        this.entityRepository = entityRepository;
        this.c = defaultConst;
    }

    //region default entity
    public EOwnedEntity createDefaultEntity() {
        return entityRepository.save(
                new EOwnedEntity(c.entityDefaultName, c.entityDefaultUsername, c.entityDefaultDescription)
        );
    }
    //endregion

}
