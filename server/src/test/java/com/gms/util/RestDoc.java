package com.gms.util;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * RestDoc
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 08, 2018
 */
public class RestDoc {
    public static final String APIDOC_LOCATION = "build/generated-snippets";

    private static final String IDENTIFIER = "{method-name}";

    private RestDoc() {
    }

    public static RestDocumentationResultHandler getRestDocumentationResultHandler() {
        return document(IDENTIFIER, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
    }
}
