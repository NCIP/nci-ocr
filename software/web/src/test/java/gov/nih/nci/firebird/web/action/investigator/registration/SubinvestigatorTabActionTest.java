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

package gov.nih.nci.firebird.web.action.investigator.registration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.ResourcesUtility;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.InvitationStatus;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class SubinvestigatorTabActionTest extends AbstractWebTest {

    @Inject
    private PersonService personService;
    @Inject
    private ProtocolRegistrationService registrationService;
    @Inject
    private SubinvestigatorTabAction action;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
    }

    @Test
    public void testPrepare_NoSubInvestigatorRegistration() {
        getSearchableSubInvestigatorRegistration();

        action.prepare();
        assertNull(action.getSubinvestigatorRegistration());
    }

    private SubInvestigatorRegistration getSearchableSubInvestigatorRegistration() {
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration();
        when(registrationService.getById(subInvestigatorRegistration.getId())).thenReturn(subInvestigatorRegistration);
        return subInvestigatorRegistration;
    }

    @Test
    public void testPrepare_NoSubInvestigatorRegistrationId() {
        SubInvestigatorRegistration subInvestigatorRegistration = getSearchableSubInvestigatorRegistration();

        action.setSubinvestigatorRegistration(new SubInvestigatorRegistration());
        action.prepare();
        assertNotSame(subInvestigatorRegistration, action.getSubinvestigatorRegistration());
    }

    @Test
    public void testPrepare_ValidSubInvestigatorRegistrationId() {
        SubInvestigatorRegistration subInvestigatorRegistration = getSearchableSubInvestigatorRegistration();

        action.setSubinvestigatorRegistration(new SubInvestigatorRegistration());
        action.getSubinvestigatorRegistration().setId(subInvestigatorRegistration.getId());
        action.prepare();
        assertSame(subInvestigatorRegistration, action.getSubinvestigatorRegistration());
    }

    @Test
    public void testEnter() {
        assertEquals(ActionSupport.SUCCESS, action.enter());
    }

    @Test
    public void testAddFromProfile_Single() {
        InvestigatorRegistration registration = getSearchableInvestigatorRegistration();
        Person person1 = getSearchablePerson();

        action.setRegistration(new InvestigatorRegistration());
        action.getRegistration().setId(registration.getId());
        action.setSelectedIds(Collections.singletonList(person1.getId()));

        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.addFromProfile());
        verify(registrationService).createSubinvestigatorRegistrations(eq(registration), anyListOf(Long.class));
        verify(registrationService).setReturnedOrRevisedRegistrationsFormStatusesToRevised(
                action.getRegistration().getProfile(), FormTypeEnum.FORM_1572);
    }

    private InvestigatorRegistration getSearchableInvestigatorRegistration() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        when(registrationService.getById(registration.getId())).thenReturn(registration);
        return registration;
    }

    private Person getSearchablePerson() {
        Person person = PersonFactory.getInstanceWithId().create();
        when(personService.getById(person.getId())).thenReturn(person);

        return person;
    }

    @Test
    public void testAddFromProfile_Multiple() {
        InvestigatorRegistration registration = getSearchableInvestigatorRegistration();
        Person person1 = getSearchablePerson();
        Person person2 = getSearchablePerson();

        action.setRegistration(new InvestigatorRegistration());
        action.getRegistration().setId(registration.getId());
        action.setSelectedIds(Lists.newArrayList(person1.getId(), person2.getId()));

        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.addFromProfile());
        verify(registrationService).setReturnedOrRevisedRegistrationsFormStatusesToRevised(
                action.getRegistration().getProfile(), FormTypeEnum.FORM_1572);
    }

    @Test
    public void testDelete() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration(registration, InvestigatorProfileFactory.getInstance().create());
        registration.getSubinvestigatorRegistrations().add(subInvestigatorRegistration);
        when(registrationService.getById(registration.getId())).thenReturn(registration);
        when(registrationService.getById(subInvestigatorRegistration.getId())).thenReturn(subInvestigatorRegistration);

        action.setRegistration(registration);
        action.setSubinvestigatorRegistration(subInvestigatorRegistration);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.delete());

        verify(registrationService).removeSubInvestigatorRegistrationAndNotify(subInvestigatorRegistration);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSubinvestigators() throws JSONException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        Person p = PersonFactory.getInstance().create();
        p.setId(1L);
        InvestigatorProfile profile = new InvestigatorProfile();
        profile.setId(99L);
        profile.setPerson(p);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration, profile);
        subInvestigatorRegistration.setId(555L);
        registration.getSubinvestigatorRegistrations().add(subInvestigatorRegistration);

        action.setRegistration(registration);
        String json = action.getRegistrationsAsJson();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) JSONUtil.deserialize(json);
        assertEquals(1, rows.size());
        assertEquals(subInvestigatorRegistration.getId().toString(), rows.get(0).get("id").toString());
        assertEquals(p.getId().toString(), ((Map<String, Object>) rows.get(0).get("investigator")).get("id").toString());
    }

    @Test
    public void testGetUnaddedProfileSubinvestigators_all() throws AssociationAlreadyExistsException, JSONException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        Person person = PersonFactory.getInstance().create();
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        registration.setProfile(profile);
        profile.addSubInvestigator(person);
        action.setRegistration(registration);
        assertEquals(1, action.getUnaddedProfileSubinvestigators().size());
        assertTrue(action.getUnaddedProfileSubinvestigators().contains(person));

        // JSON
        String expectedJson = JSONUtil.serialize(Collections.singleton(person));
        assertEquals(expectedJson, action.getProfileSubinvestigatorsAsJson());
    }

    @Test
    public void testGetUnaddedProfileSubinvestigators_Unadded() throws AssociationAlreadyExistsException, JSONException {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        Person person = PersonFactory.getInstance().create();
        person.setId(1L);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstance()
                .createSubinvestigatorRegistration(registration);
        Person subInvestigator = subInvestigatorRegistration.getProfile().getPerson();
        subInvestigator.setId(2L);
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        registration.setProfile(profile);
        profile.addSubInvestigator(subInvestigator);
        profile.addSubInvestigator(person);
        action.setRegistration(registration);
        assertEquals(1, action.getUnaddedProfileSubinvestigators().size());
        assertTrue(action.getUnaddedProfileSubinvestigators().contains(person));
        assertFalse(action.getUnaddedProfileSubinvestigators().contains(subInvestigator));

        // JSON
        String expectedJson = JSONUtil.serialize(Collections.singleton(person));
        assertEquals(expectedJson, action.getProfileSubinvestigatorsAsJson());
    }

    @Test
    public void testInvite() {
        InvestigatorRegistration registration = getSearchableInvestigatorRegistration();
        SubInvestigatorRegistration subReg1 = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration(registration);
        registration.getSubinvestigatorRegistrations().add(subReg1);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration(registration);
        registration.getSubinvestigatorRegistrations().add(subInvestigatorRegistration);

        action.setRegistration(new InvestigatorRegistration());
        action.getRegistration().setId(registration.getId());
        action.setInvitedRegistrations(Collections.singletonList(subReg1.getId()));

        action.prepare();
        verify(registrationService).getById(registration.getId());
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.invite());
        verify(registrationService).inviteToRegistration(subReg1);
        verifyNoMoreInteractions(registrationService);
    }

    @Test
    public void testEnterTab_IncompleteWithUninvitedSubinvestigator() {
        InvestigatorRegistration registration = getRegistrationWithSubinvestigatorRegistration(
                RegistrationStatus.INCOMPLETE, InvitationStatus.NOT_INVITED);
        action.setRegistration(registration);
        action.enterTab();
        String subinvestigator = Iterables.getOnlyElement(registration.getSubinvestigators()).getDisplayName();
        String expectedErrorMessage = ResourcesUtility.getMessage("validation.failures.subinvestigator.not.invited",
                subinvestigator);
        assertEquals(expectedErrorMessage, Iterables.getOnlyElement(action.getActionErrors()));
        assertEquals(1, action.getInvalidSubinvestigatorIds().size());
        Long subinvestigatorId = Iterables.getOnlyElement(registration.getSubinvestigatorRegistrations()).getProfile().getPerson().getId();
        assertTrue(action.getInvalidSubinvestigatorIds().contains(subinvestigatorId));
    }

    private InvestigatorRegistration getRegistrationWithSubinvestigatorRegistration(
            RegistrationStatus investigatorRegistrationStatus, InvitationStatus subinvestigatorInvitationStatus) {
        InvestigatorRegistration investigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        investigatorRegistration.setStatus(investigatorRegistrationStatus);
        SubInvestigatorRegistration subInvestigatorRegistration = RegistrationFactory.getInstanceWithId()
                .createSubinvestigatorRegistration(investigatorRegistration, investigatorRegistration.getProfile());
        subInvestigatorRegistration.getInvitation().setInvitationStatus(subinvestigatorInvitationStatus);
        investigatorRegistration.getSubinvestigatorRegistrations().add(subInvestigatorRegistration);
        return investigatorRegistration;
    }

    @Test
    public void testEnterTab_InProgressWithUninvitedSubinvestigator() {
        InvestigatorRegistration registration = getRegistrationWithSubinvestigatorRegistration(
                RegistrationStatus.IN_PROGRESS, InvitationStatus.NOT_INVITED);
        action.setRegistration(registration);
        action.enterTab();
        assertTrue(action.getActionErrors().isEmpty());
        assertTrue(action.getInvalidSubinvestigatorIds().isEmpty());
    }

    @Test
    public void testEnterTab_IncompleteWithInvitedSubinvestigator() {
        InvestigatorRegistration registration = getRegistrationWithSubinvestigatorRegistration(
                RegistrationStatus.INCOMPLETE, InvitationStatus.NO_RESPONSE);
        action.setRegistration(registration);
        action.enterTab();
        assertTrue(action.getActionErrors().isEmpty());
        assertTrue(action.getInvalidSubinvestigatorIds().isEmpty());
    }

    @Test
    public void testEnterTab_IncompleteWithNoSubinvestigator() {
        InvestigatorRegistration registration = RegistrationFactory.getInstanceWithId()
                .createInvestigatorRegistration();
        registration.setStatus(RegistrationStatus.INCOMPLETE);
        action.setRegistration(registration);
        action.enterTab();
        assertTrue(action.getActionErrors().isEmpty());
        assertTrue(action.getInvalidSubinvestigatorIds().isEmpty());
    }
}