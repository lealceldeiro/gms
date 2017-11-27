package com.gmsboilerplatesbng.service.security.ownedEntity;

import com.gmsboilerplatesbng.domain.security.ownedEntity.EOwnedEntity;
import com.gmsboilerplatesbng.repository.security.ownedEntity.EOwnedEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OwnedEntityService {

    @Value("${default.gmsentity.name}")
    private String defaultEntityName = "HOME";

    @Value("${default.gmsentity.username}")
    private String defaultEntityUsername = "home";

    @Value("${default.gmsentity.description}")
    private String defaultEntityDescription = "Default entity";

    private final EOwnedEntityRepository entityRepository;


    @Autowired
    public OwnedEntityService(EOwnedEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    //region default user
    public EOwnedEntity createDefaultEntity() {
        return this.entityRepository.save(
                new EOwnedEntity(this.defaultEntityName, this.defaultEntityUsername, this.defaultEntityDescription)
        );
    }
    //endregion

}
