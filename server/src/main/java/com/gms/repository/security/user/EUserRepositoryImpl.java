package com.gms.repository.security.user;

import com.gms.domain.security.user.EUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Iterator;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@Transactional
public class EUserRepositoryImpl implements EUserRepositoryCustom {

    /**
     * Instance of {@link PasswordEncoder}. This bean is provided by the Spring framework dependency manager.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Dependency on a container-managed {@link EntityManager} and its associated persistence context.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     * <p>
     * This method is not intended to be overridden. If a custom implementation is required, consider implementing
     * {@link EUserRepositoryCustom}
     */
    @Override
    public <S extends EUser> S save(final S s) {
        persist(s);

        return s;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not intended to be overridden. If a custom implementation is required, consider implementing
     * {@link EUserRepositoryCustom}
     */
    @Override
    public <S extends EUser> Iterable<S> saveAll(final Iterable<S> it) {
        final Iterator<S> userIterator = it.iterator();
        S user;
        while (userIterator.hasNext()) {
            user = userIterator.next();
            persist(user);
        }

        return it;
    }

    /**
     * Persist a single instance of {@link EUser}.
     *
     * @param u {@link EUser} to be persisted.
     */
    private void persist(final EUser u) {
        if (u.getPassword() != null) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        entityManager.persist(u);
    }

}
