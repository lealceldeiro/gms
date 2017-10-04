package com.gmsboilerplatesbng.security.bpermission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsboilerplatesbng.Application;
import com.gmsboilerplatesbng.domain.secuirty.permission.BPermission;
import com.gmsboilerplatesbng.repository.security.permission.BPermissionRepository;
import com.gmsboilerplatesbng.util.GMSRandom;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class RepositoryTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired private WebApplicationContext context;

    @Autowired private BPermissionRepository repository;

    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;

    private RestDocumentationResultHandler restDocResHandler;

    private GMSRandom random = new GMSRandom();

    @Before
    public void setUp() {
        this.restDocResHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.restDocResHandler)
                .build();
    }
    @Test
    public void listPermissions() throws Exception {
        String reqString = "permission";
        String rS1 = random.nextString(), rS2 = random.nextString();
        createSamplePermission("defaultPN " + rS1, "defaultPL " + rS1);
        createSamplePermission("defaultPN " + rS2, "defaultPL " + rS2);

        this.restDocResHandler.snippets(
                responseFields(
                        fieldWithPath("_embedded." + reqString).description("Array of permissions"),
                        fieldWithPath("_links").description("Available link for querying other webservices related to permissions"),
                        fieldWithPath("page").description("Options for paging the permissions")
                )
        );

        this.mvc.perform(
                get("/api/" + reqString).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    private BPermission createSamplePermission(String name, String label) {
        return this.repository.save(new BPermission(name, label));
    }
}
