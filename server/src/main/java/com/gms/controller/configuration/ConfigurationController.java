package com.gms.controller.configuration;

import com.gms.controller.BaseController;
import com.gms.service.configuration.ConfigurationService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(ResourcePath.CONFIGURATION)
public class ConfigurationController extends BaseController{

    private final ConfigurationService configService;

    @GetMapping("sign-up")
    @ResponseBody
    public boolean isUserRegistrationAllowed() {
        return configService.isUserRegistrationAllowed();
    }

    @GetMapping
    @ResponseBody
    public Object getConfig(@RequestParam(value = "key", required = false) String key,
                            @RequestParam(value = "id", required = false) Long id) throws NotFoundEntityException {
        return key != null ? getConfigByKey(key, id) : configService.getConfig();
    }

    @GetMapping("{id}")
    @ResponseBody
    public Map getConfigByUser(@PathVariable(value = "id") Long id) {
        return configService.getConfigByUser(id);
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveConfig(@RequestBody Map<String, Object> configs) throws NotFoundEntityException, GmsGeneralException {
        configService.saveConfig(configs);
    }

    private Object getConfigByKey(String key, Long userId) throws NotFoundEntityException {
        return userId != null ?
                configService.getConfig(key, userId) :
                configService.getConfig(key);
    }
}
