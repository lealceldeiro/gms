package com.gms.service.security.ownedentity;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.repository.security.ownedentity.EOwnedEntityRepository;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.GmsGeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OwnedEntityService {

    /**
     * An instance of an {@link EOwnedEntityRepository}.
     */
    private final EOwnedEntityRepository entityRepository;

    /**
     * An instance of a {@link DefaultConstant}.
     */
    private final DefaultConstant defaultConstant;

    /**
     * An instance of a {@link ConfigurationService}.
     */
    private final ConfigurationService configurationService;

    //region default entity

    /**
     * Creates the default Owned Entity according to the values regarding to this resource in {@link DefaultConstant}.
     *
     * @return The just created resource {@link EOwnedEntity}
     */
    public EOwnedEntity createDefaultEntity() {
        return entityRepository.save(new EOwnedEntity(
                defaultConstant.getEntityDefaultName(),
                defaultConstant.getEntityDefaultUsername(),
                defaultConstant.getEntityDefaultDescription())
        );
    }
    //endregion

    /**
     * Creates and return an {@link EOwnedEntity}.
     *
     * @param e {@link EOwnedEntity} data.
     * @return The created {@link EOwnedEntity}
     * @throws GmsGeneralException if the server configuration is no set in order to create new Owned Entities
     */
    public EOwnedEntity create(final EOwnedEntity e) throws GmsGeneralException {
        if (configurationService.isMultiEntity()) {
            return entityRepository.save(e);
        }

        throw GmsGeneralException.builder()
                .messageI18N("entity.add.not_allowed")
                .finishedOK(true).httpStatus(HttpStatus.CONFLICT)
                .build();
    }

}
