package gov.nih.nci.firebird.web.interceptor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Map;

import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.web.action.login.LoginAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ReviewRegistrationAjaxAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.RoleSelectionPageFlowAction;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class RegistrationRequiredInterceptorTest extends AbstractWebTest {

    private RegistrationRequiredInterceptor interceptor;
    private Map<String, Object> sessionMap = Maps.newHashMap();
    private UserSessionInformation validGridSessionInformation = UserSessionInformationFactory.getInstance().create("WHEE/A/USER/NAME!");
    @Mock private FirebirdUserService userService;
    @Mock ActionInvocation invocation;
    @Mock ActionContext actionContext;
    
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(invocation.getInvocationContext()).thenReturn(actionContext);
        when(actionContext.getSession()).thenReturn(sessionMap);
        interceptor = new RegistrationRequiredInterceptor(userService);
    }
    
    @Test
    public void testInterceptExcludedAction() throws Exception {
        Object mockAction = mock(LoginAction.class);
        when(invocation.getAction()).thenReturn(mockAction);
        interceptor.intercept(invocation);
        verify(invocation).invoke();
    }
    
    @Test
    public void testInterceptExcludedPageFlowAction() throws Exception {
        Object mockAction = mock(RoleSelectionPageFlowAction.class);
        when(invocation.getAction()).thenReturn(mockAction);
        interceptor.intercept(invocation);
        verify(invocation).invoke();
    }
    
    @Test
    public void testInterceptNoUsername() throws Exception {
        Object mockAction = mock(ReviewRegistrationAjaxAction.class);
        FirebirdUser mockUser = mock(FirebirdUser.class);
        when(invocation.getAction()).thenReturn(mockAction);
        when(userService.getUserInfo(any(UserSessionInformation.class))).thenReturn(mockUser);
        assertEquals(FirebirdUIConstants.RETURN_REGISTRATION_FLOW_ENTER, interceptor.intercept(invocation));
        verify(invocation, times(0)).invoke();
    }
    
    @Test
    public void testInterceptNoAccount() throws Exception {
        Object mockAction = mock(ReviewRegistrationAjaxAction.class);
        sessionMap.put(FirebirdConstants.USER_SESSION_INFORMATION, validGridSessionInformation);
        when(invocation.getAction()).thenReturn(mockAction);
        when(userService.getUserInfo(validGridSessionInformation)).thenReturn(null);
        assertEquals(FirebirdUIConstants.RETURN_REGISTRATION_FLOW_ENTER, interceptor.intercept(invocation));
        verify(invocation, times(0)).invoke();
    }
    
    @Test
    public void testInterceptAlreadyHasAccount() throws Exception {
        Object mockAction = mock(ReviewRegistrationAjaxAction.class);
        sessionMap.put(FirebirdConstants.USER_SESSION_INFORMATION, validGridSessionInformation);
        FirebirdUser mockUser = mock(FirebirdUser.class);
        when(invocation.getAction()).thenReturn(mockAction);
        when(userService.getUserInfo(validGridSessionInformation)).thenReturn(mockUser);
        interceptor.intercept(invocation);
        verify(invocation, times(1)).invoke();
    }
    
    
}
