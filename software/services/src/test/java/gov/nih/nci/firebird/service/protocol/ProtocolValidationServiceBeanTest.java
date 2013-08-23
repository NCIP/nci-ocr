package gov.nih.nci.firebird.service.protocol;

import com.google.inject.Provider;
import gov.nih.nci.firebird.common.ValidationUtility;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.test.ProtocolFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProtocolValidationServiceBeanTest {

    @Mock
    private Provider<Session> mockSessionProvider;
    @Mock
    private Session mockSession;
    @Mock
    private Query mockQuery;

    private ProtocolValidationServiceBean bean = new ProtocolValidationServiceBean();
    private ResourceBundle resources = ResourceBundle.getBundle("resources");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSessionProvider.get()).thenReturn(mockSession);
        when(mockSession.createQuery(anyString())).thenReturn(mockQuery);
        bean.setSessionProvider(mockSessionProvider);
        bean.setResources(resources);
    }

    @Test
    public void testUpdateProtocol_NoComment() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.setProtocolTitle("New Title");
        try {
            bean.validateChanges(modifiedProtocol, "");
            fail("Should have thrown a validation exception");
        } catch (ValidationException e) {
            assertEquals(1, e.getResult().getFailures().size());
            ValidationUtility.checkForFailureByMessageKey(e.getResult(), "comment", "protocol.change.comment.required");
            verify(mockSession).evict(modifiedProtocol);
        }
    }

    @Test
    public void testUpdateProtocol_InvalidInvestigatorFormOptionalities() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities().clear();
        modifiedProtocol.getRegistrationConfiguration().setInvestigatorOptionality(new FormType("name"),
                FormOptionality.OPTIONAL);
        try {
            bean.validateChanges(modifiedProtocol, "comments");
            fail("Should have thrown a validation exception");
        } catch (ValidationException e) {
            assertEquals(1, e.getResult().getFailures().size());
            ValidationUtility.checkForFailureByMessageKey(e.getResult(), null,
                    "protocol.investigator.optionalities.require.one.required.form");
            verify(mockSession).evict(modifiedProtocol);
        }
    }

    @Test
    public void testUpdateProtocol_InvalidSubinvestigatorFormOptionalities() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities()
                .clear();
        modifiedProtocol.getRegistrationConfiguration().setSubinvestigatorOptionality(new FormType("name"),
                FormOptionality.NONE);
        try {
            bean.validateChanges(modifiedProtocol, "comments");
            fail("Should have thrown a validation exception");
        } catch (ValidationException e) {
            assertEquals(1, e.getResult().getFailures().size());
            ValidationUtility.checkForFailureByMessageKey(e.getResult(), null,
                    "protocol.subinvestigator.optionalities.require.one.required.or.optional.form");
            verify(mockSession).evict(modifiedProtocol);
        }
    }

    @Test
    public void testUpdateProtocol_NoForms() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstance().createWithFormsDocuments();
        Protocol modifiedProtocol = protocol.createCopy();
        modifiedProtocol.getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities().clear();
        modifiedProtocol.getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities()
                .clear();
        try {
            bean.validateChanges(modifiedProtocol, "comments");
            fail("Should have thrown a validation exception");
        } catch (ValidationException e) {
            assertEquals(2, e.getResult().getFailures().size());
            ValidationUtility.checkForFailureByMessageKey(e.getResult(), null,
                    "protocol.investigator.optionalities.require.one.required.form");
            ValidationUtility.checkForFailureByMessageKey(e.getResult(), null,
                    "protocol.subinvestigator.optionalities.require.one.required.or.optional.form");
            verify(mockSession).evict(modifiedProtocol);
        }
    }
}
