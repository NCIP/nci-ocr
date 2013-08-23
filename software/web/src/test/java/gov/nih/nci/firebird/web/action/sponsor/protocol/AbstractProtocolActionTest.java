package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.investigatorprofile.ProfileRefreshService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Test;

import com.google.inject.Inject;

@SuppressWarnings("serial")
public class AbstractProtocolActionTest extends AbstractWebTest {

    @Inject
    private ProtocolService mockProtocolService;
    @Inject
    private ProfileRefreshService mockProfileRefreshService;

    private AbstractProtocolAction action;
    private FirebirdUser user;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action = new AbstractProtocolAction(mockProtocolService) {
        };
        action.setProfileRefreshService(mockProfileRefreshService);
        user = FirebirdUserFactory.getInstance().create();
        FirebirdWebTestUtility.setCurrentUser(action, user);
    }

    @Test
    public void testPrepare_NoProtocol() {
        action.setProtocol(null);
        action.prepare();
        assertNull(action.getProtocol());
        verifyZeroInteractions(mockProtocolService);
    }

    @Test
    public void testPrepare_ProtocolNoId() {
        action.setProtocol(new Protocol());
        action.prepare();
        assertNull(action.getProtocol());
        verifyZeroInteractions(mockProtocolService);
    }

    @Test
    public void testPrepare_ProtocolWithId() {
        Protocol protocol = ProtocolFactory.getInstanceWithId().create();
        action.setProtocol(protocol);
        action.prepare();
        verify(mockProtocolService).getById(protocol.getId());
    }
}
