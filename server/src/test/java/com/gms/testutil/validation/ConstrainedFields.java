package com.gms.testutil.validation;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.util.StringUtils;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class ConstrainedFields {
    /**
     * Instance of {@link ConstraintDescriptions}.
     */
    private final ConstraintDescriptions constraintDescriptions;

    /**
     * Creates a {@link ConstrainedFields} from the given input.
     *
     * @param input {@link Class} to create the {@link ConstrainedFields} from.
     */
    public ConstrainedFields(final Class<?> input) {
        this.constraintDescriptions = new ConstraintDescriptions(input);
    }

    /**
     * Creates and returns a FieldDescriptor that describes a field with the given path.
     *
     * @param path Path to be described.
     * @return FieldDescriptor
     */
    public FieldDescriptor withPath(final String path) {
        return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                .collectionToDelimitedString(this.constraintDescriptions
                        .descriptionsForProperty(path), ". ")));
    }

}
