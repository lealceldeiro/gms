package com.gmsboilerplatesbng.service.security.ownedEntity;

import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OwnedEntityService {

    private final EOwnedEntityRepository entityRepository;


    @Autowired
    public OwnedEntityService(EOwnedEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    //region default user
    public EOwnedEntity createDefaultEntity() {
        return this.entityRepository.save(
                new EOwnedEntity(DefaultConst.ENTITY_NAME, DefaultConst.ENTITY_USERNAME, DefaultConst.ENTITY_DESCRIPTION)
        );
    }
    //endregion

}
