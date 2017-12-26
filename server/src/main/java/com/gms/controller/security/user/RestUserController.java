package com.gms.controller.security.user;

import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsGeneralException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

/**
 * RestUserController
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 26, 2017
 */
@BasePathAwareController
@RestControllerAdvice
public class RestUserController extends ResponseEntityExceptionHandler {

    private final MessageResolver msg;

    private final DefaultConst dc;

    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService, MessageResolver messageResolver, DefaultConst defaultConst) {
        this.dc = defaultConst;
        this.userService = userService;
        this.msg = messageResolver;
    }


    /**
     * Registers a new {@link EUser} setting it as active with properties such as emailVerified and enabled to `true`.
     * This method overrides the default "user" url in the {@link com.gms.repository.security.user.EUserRepository}
     * by setting it path to 'user' (like in the `EUserRepository` interface, by doing `produces = 'application/hal+json'`
     * and by putting in within a controller annotated as `@BasePathAwareController`.
     * @param user {@link EUser} data to be created.
     * @param pra Injected automatically by Spring.
     * @return A {@link EUser} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = "user", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PersistentEntityResource register(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        EUser u = userService.signUp(user.getContent(), true, true);
        if (u != null) {
            return pra.toResource(u);
        }
        else throw new GmsGeneralException("user.add.error", false);
    }

    /**
     * Handles all exceptions which where not specifically treated.
     * @param ex {@link GmsGeneralException} exception.
     * @param req {@link WebRequest} request.
     * @return Formatted {@link org.springframework.http.ResponseEntity} depending on the requested format (i.e.: json, xml)
     * containing detailed information about the exception.
     */
    @ExceptionHandler(GmsGeneralException.class)
    protected ResponseEntity<Object> handleGmsGeneralException(GmsGeneralException ex, WebRequest req) {
        HashMap<String, Object> r = new HashMap<>();
        r.put(dc.getResMessageHolder(), (msg.getMessage(ex.getMessage()) + ". "
                + msg.getMessage(ex.finishedOK() ? "request.finished.OK" : "request.finished.KO")));
        return handleExceptionInternal(ex, r, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, req);
    }
}
