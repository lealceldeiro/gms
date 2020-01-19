package com.gms.controller.configuration;

import com.gms.service.configuration.ConfigurationService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(ResourcePath.CONFIGURATION)
public class ConfigurationController {

    /**
     * Instance of {@link ConfigurationService}.
     */
    private final ConfigurationService configService;

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return {@code true} if the registration of new users is allowed, {@code false} otherwise.
     */
    @GetMapping("sign-up")
    public boolean isUserRegistrationAllowed() {
        return configService.isUserRegistrationAllowed();
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @return {@code true} if the system manages multiple entities (a business concept) of new users is allowed,
     * {@code false} otherwise.
     */
    @GetMapping("multientity")
    public boolean isMultiEntity() {
        return configService.isMultiEntity();
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param key Configuration key.
     * @param id  {@link com.gms.domain.security.user.EUser} id.
     * @return Object which represent the configuration value for the given parameters.
     */
    @GetMapping
    public Object getConfig(@RequestParam(value = "key", required = false) final String key,
                            @RequestParam(value = "id", required = false) final Long id)
            throws NotFoundEntityException {
        return key != null ? getConfigByKey(key, id) : configService.getConfig();
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param id {@link com.gms.domain.security.user.EUser} id.
     * @return Map which represent the configurations key-value pairs for the given parameter.
     */
    @GetMapping("{id}")
    public Map<String, Object> getConfigByUser(@PathVariable(value = "id") final Long id) {
        return configService.getConfigByUser(id);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param configs {@link Map} with the key-value pairs to be saved as configuration.
     * @throws NotFoundEntityException if one of the configurations key is not valid.
     * @throws GmsGeneralException     if there is an error processing the user id provided in the {@link Map} argument
     *                                 {@code configs}.
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveConfig(@RequestBody final Map<String, Object> configs)
            throws NotFoundEntityException, GmsGeneralException {
        configService.saveConfig(configs);
    }

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param key    Key used to retrieve the requested configuration.
     * @param userId {@link com.gms.domain.security.user.EUser} id which the configuration is associated to.
     * @return Object represeting the requested foconfiguration.
     * @throws NotFoundEntityException if there is no configuration with the provided id.
     */
    private Object getConfigByKey(final String key, final Long userId) throws NotFoundEntityException {
        return userId != null ? configService.getConfig(key, userId) : configService.getConfig(key);
    }

}
