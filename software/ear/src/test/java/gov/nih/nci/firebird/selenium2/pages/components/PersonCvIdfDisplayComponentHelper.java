package gov.nih.nci.firebird.selenium2.pages.components;

import static org.apache.commons.lang.StringUtils.deleteWhitespace;
import static org.apache.commons.lang.StringUtils.trimToNull;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.Person;

public class PersonCvIdfDisplayComponentHelper {

    private final PersonCvIdfDisplayComponent component;

    public PersonCvIdfDisplayComponentHelper(PersonCvIdfDisplayComponent component) {
        this.component = component;
    }

    public void assertPersonDisplayed(Person person) {
        if (component.isNesIdPresent()) {
            assertEquals(person.getNesId(), component.getNesId());
        }
        assertEquals(person.getDisplayName(), component.getName());
        assertEquals(person.getProviderNumber(), trimToNull(component.getProvider()));
        assertEquals(person.getCtepId(), trimToNull(component.getInvestigatorNumber()));
        assertEquals(person.getEmail(), trimToNull(component.getEmail()));
        assertEquals(person.getPhoneNumber(), trimToNull(component.getPhone()));

        assertEquals(deleteWhitespace(person.getPostalAddress().toString()),
                trimToNull(deleteWhitespace(component.getPrimaryAddress())));
    }

}