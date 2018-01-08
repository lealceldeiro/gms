package com.gms.service.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.util.constant.DefaultConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * OwnedEntityService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OwnedEntityService {

    private final EOwnedEntityRepository entityRepository;
    private final DefaultConst dc;


    //region default entity
    public EOwnedEntity createDefaultEntity() {
        return entityRepository.save(
                new EOwnedEntity(dc.getEntityDefaultName(), dc.getEntityDefaultUsername(), dc.getEntityDefaultDescription())
        );
    }
    //endregion

}
