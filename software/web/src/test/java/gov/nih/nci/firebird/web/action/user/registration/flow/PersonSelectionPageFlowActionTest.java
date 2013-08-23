/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.web.action.user.registration.flow;

import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.cagrid.UserSessionInformationFactory;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.person.external.InvalidatedPersonException;
import gov.nih.nci.firebird.service.user.FirebirdUserService;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValidationExceptionFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.common.registration.RegistrationFlowController;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class PersonSelectionPageFlowActionTest extends AbstractFlowActionTestBase {

    @Inject
    private PersonSelectionPageFlowAction action;
    @Inject
    private PersonService mockPersonService;
    @Inject
    private StateLookupService mockStateLookup;
    @Inject
    private CountryLookupService mockCountryLookup;
    @Inject
    private FirebirdUserService mockUserService;
    private UserSessionInformation userSessionInformation = UserSessionInformationFactory.getInstance().create(
            "username");

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
        setAccountConfigurationDataInSession(getAccountConfigurationData());
        userSessionInformation.getAccount().setFirstName(getUniqueString());
        userSessionInformation.getAccount().setLastName(getUniqueString());
        userSessionInformation.getAccount().setEmailAddress(getUniqueEmailAddress());
        FirebirdWebTestUtility.setUpGridSessionInformation(action, userSessionInformation);
        getMockSession().setAttribute(FirebirdUIConstants.REGISTRATION_FLOW_CONTROLLER,
                RegistrationFlowController.createRegistrationFlow());
    }

    @Test
    public void testPrepare_Lookups() {
        action.prepare();
        verify(mockStateLookup).getAll();
        verify(mockCountryLookup).getAll();
    }

    @Test
    public void testPrepare_NoExistingPerson() {
        action.prepare();
        assertTrue(StringUtils.isEmpty(action.getSelectedPersonExternalId()));
    }

    @Test
    public void testPrepare_ExistingPerson() {
        Person testPerson = PersonFactory.getInstance().create();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(testPerson.getExternalId(), action.getSelectedPersonExternalId());
    }

    @Test
    public void testEnterAction_PersonNotSelected() {
        action.prepare();
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.enterAction());
    }

    @Test
    public void testEnterAction_PersonReQuery() {
        Person testPerson = PersonFactory.getInstance().create();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        action.setNavigationOption(PersonSelectionPageFlowAction.PERSON_SEARCH);
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.enterAction());
    }

    @Test
    public void testEnterAction_PersonSelected() {
        Person testPerson = PersonFactory.getInstance().create();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(ActionSupport.INPUT, action.enterAction());
    }

    @Test
    public void testEnterAction_NewPersonSelected() {
        Person testPerson = new Person();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(ActionSupport.INPUT, action.enterAction());
    }

    @Test
    public void testEnterAction_InitialEntrySingleEmailMatch() throws InvalidatedPersonException {
        action.prepare();
        Person person = PersonFactory.getInstance().create();
        when(mockPersonService.search(anyString())).thenReturn(Lists.newArrayList(person));
        when(mockPersonService.getByExternalId(person.getExternalId())).thenReturn(person);
        assertEquals(PersonSelectionPageFlowAction.INPUT, action.enterAction());
        assertEquals(person, action.getAccountConfigurationData().getPerson());
    }

    @Test
    public void testEnterAction_InitialEntryMultipleEmailMatches() {
        action.prepare();
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        when(mockPersonService.search(anyString())).thenReturn(Lists.newArrayList(person1, person2));
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.enterAction());
        assertEquals(userSessionInformation.getAccount().getEmailAddress(), action.getPrepopulatedSearchString());
    }

    @Test
    public void testEnterAction_InitialEntryWithNameMatches() {
        action.prepare();
        Person person1 = PersonFactory.getInstance().create();
        Person person2 = PersonFactory.getInstance().create();
        List<Person> emptyList = Collections.emptyList();
        when(mockPersonService.search(anyString())).thenReturn(emptyList).thenReturn(
                Lists.newArrayList(person1, person2));
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.enterAction());
        String nameSearchString = userSessionInformation.getAccount().getLastName() + ", "
                + userSessionInformation.getAccount().getFirstName();
        assertEquals(nameSearchString, action.getPrepopulatedSearchString());
    }

    @Test
    public void testEnterAction_InitialEntryWithNoMatches() {
        action.prepare();
        List<Person> emptyList = Collections.emptyList();
        when(mockPersonService.search(anyString())).thenReturn(emptyList).thenReturn(emptyList);
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.enterAction());
        assertNull(action.getPrepopulatedSearchString());
    }

    @Test
    public void testPerformSave_ReQuery() {
        action.prepare();
        action.setNavigationOption(PersonSelectionPageFlowAction.PERSON_SEARCH);
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.performSave());
        verifyZeroInteractions(mockPersonService);
        verifyZeroInteractions(mockPersonService);
    }

    @Test
    public void testPerformSave_NewPerson() throws ValidationException {
        action.prepare();
        action.setNavigationOption(PersonSelectionPageFlowAction.CREATE_NEW);
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertNull(getAccountConfigurationData().getPerson().getId());
        assertEquals(userSessionInformation.getAccount().getFirstName(), getAccountConfigurationData().getPerson()
                .getFirstName());
        assertEquals(userSessionInformation.getAccount().getLastName(), getAccountConfigurationData().getPerson()
                .getLastName());
        assertEquals(userSessionInformation.getAccount().getEmailAddress(), getAccountConfigurationData().getPerson()
                .getEmail());
        assertEquals(CurationStatus.UNSAVED, getAccountConfigurationData().getPerson().getCurationStatus());
        verifyZeroInteractions(mockPersonService);
    }

    @Test
    public void testPerformSave_SearchedPerson() throws Exception {
        Person testPerson = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(testPerson.getExternalId())).thenReturn(testPerson);
        action.prepare();
        action.setSelectedPersonExternalId(testPerson.getExternalId());
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(testPerson, getAccountConfigurationData().getPerson());
    }

    @Test
    public void testPerformSave_SearchedPersonAlreadyAssociated() throws Exception {
        Person testPerson = PersonFactory.getInstance().create();
        when(mockPersonService.getByExternalId(testPerson.getExternalId())).thenReturn(testPerson);
        when(mockUserService.checkPersonAssociated(testPerson)).thenReturn(Boolean.TRUE);
        action.prepare();
        action.setSelectedPersonExternalId(testPerson.getExternalId());
        assertEquals(PersonSelectionPageFlowAction.PERSON_SEARCH, action.performSave());
    }

    @Test
    public void testPerformSave_PreviouslyVisited() throws ValidationException {
        Person testPerson = new Person();
        testPerson.setFirstName("Liam");
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        assertEquals(testPerson, getAccountConfigurationData().getPerson());
        verify(mockPersonService).validate(any(Person.class));
    }

    @Test
    public void testPerformSave_SelectSamePerson() throws Exception {
        Person testPersonOriginal = PersonFactory.getInstance().create();
        testPersonOriginal.setFirstName("Kyle");
        when(mockPersonService.getByExternalId(testPersonOriginal.getExternalId())).thenReturn(testPersonOriginal);

        Person testPersonUpdated = PersonFactory.getInstance().create();
        testPersonUpdated.setFirstName("Martin");
        testPersonUpdated.setExternalData(testPersonOriginal.getExternalData());
        getAccountConfigurationData().setPerson(testPersonUpdated);

        action.prepare();
        action.setSelectedPersonExternalId(testPersonUpdated.getExternalId());
        assertEquals(ActionSupport.SUCCESS, action.performSave());
        verify(mockPersonService).getByExternalId(testPersonUpdated.getExternalId());
        assertEquals(testPersonOriginal.getFirstName(), getAccountConfigurationData().getPerson().getFirstName());
        verifyZeroInteractions(mockPersonService);
    }

    @Test
    public void testSaveAndProceedNext_NoError() throws ValidationException {
        Person testPerson = new Person();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(ActionSupport.SUCCESS, action.saveAndProceedNext());
        verify(mockPersonService, times(2)).validate(eq(testPerson));
    }

    @Test
    public void testSaveAndProceedNext_ValidationError() throws ValidationException {
        ValidationException exception = ValidationExceptionFactory.getInstance().create();
        doThrow(exception).when(mockPersonService).validate(any(Person.class));
        Person testPerson = new Person();
        getAccountConfigurationData().setPerson(testPerson);
        action.prepare();
        assertEquals(ActionSupport.INPUT, action.saveAndProceedNext());
    }

    @Test
    public void testSaveAndProceedPrevious() throws ValidationException {
        Person person = PersonFactory.getInstanceWithId().create();
        getAccountConfigurationData().setPerson(person);
        action.prepare();
        assertEquals(ActionSupport.SUCCESS, action.saveAndProceedPrevious());
        verify(mockPersonService).validate(person);
    }
}
