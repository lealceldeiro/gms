package com.gms.service.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final ConfigurationService configService;


    //region default entity
    public EOwnedEntity createDefaultEntity() {
        return entityRepository.save(
                new EOwnedEntity(dc.getEntityDefaultName(), dc.getEntityDefaultUsername(), dc.getEntityDefaultDescription())
        );
    }
    //endregion

    public EOwnedEntity create(EOwnedEntity e) throws GmsGeneralException {
        if (configService.isMultiEntity()) {
            return entityRepository.save(e);
        }
        throw new GmsGeneralException("entity.add.not_allowed", true, HttpStatus.CONFLICT);
    }
}
