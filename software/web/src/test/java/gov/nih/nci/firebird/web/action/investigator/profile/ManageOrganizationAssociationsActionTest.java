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
package gov.nih.nci.firebird.web.action.investigator.profile;

import static com.opensymphony.xwork2.Action.*;
import static gov.nih.nci.firebird.data.OrganizationRoleType.*;
import static gov.nih.nci.firebird.web.action.investigator.profile.ManageOrganizationAssociationsAction.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.nes.common.UnavailableEntityException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.organization.OrganizationSearchService;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.util.ComparisonUtil;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class ManageOrganizationAssociationsActionTest extends AbstractWebTest {

    private static final String VALID_OHRP_NUMBER = ValueGenerator.getUniqueOhrpNumber();

    @Inject
    private InvestigatorProfileService mockProfileService;
    @Inject
    private OrganizationSearchService mockOrganizationSearchService;
    @Inject
    private ManageOrganizationAssociationsAction action;
    private InvestigatorProfile profile = InvestigatorProfileFactory.getInstanceWithId().create();
    private FirebirdUser investigator = FirebirdUserFactory.getInstance().createInvestigator(profile);
    private Organization organization = OrganizationFactory.getInstance().create();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        organization.setId(12345L);
        profile.setId(12345L);
        action.setProfile(profile);
        action.setServletRequest(getMockRequest());
        when(mockProfileService.getById(profile.getId())).thenReturn(profile);
        when(mockOrganizationSearchService.getOrganization(organization.getId().toString())).thenReturn(organization);
    }

    @Test
    public void testPrepare_hasSearchKey() {
        action.setSearchKey(organization.getId().toString());
        action.prepare();
        assertEquals(organization, action.getAssociatedOrganization());
    }

    @Test
    public void testPrepare_hasNonMatchingId() {
        action.setSearchKey("2355wdgdsg");
        action.prepare();
        assertNull(action.getAssociatedOrganization());
    }

    @Test
    public void testPrepare_UnavailableEntityException() throws UnavailableEntityException {
        String searchKey = "NES1";
        when(mockOrganizationSearchService.getOrganization(searchKey)).thenThrow(
                new UnavailableEntityException(null, searchKey));
        action.setSearchKey(searchKey);
        action.prepare();
        assertTrue(action.hasActionErrors());
    }

    @Test
    public void testManageOrganizationAssociationEnter() {
        assertEquals(ActionSupport.SUCCESS, action.manageOrganizationAssociationEnter());
    }

    @Test(expected = NullPointerException.class)
    public void testManageOrganizationAssociationEnter_Fail() {
        action.setProfile(null);
        action.manageOrganizationAssociationEnter();
    }

    @Test
    public void testSelectAssociatedOrganization_CreateNew() throws Exception {
        action.setNesId(StringUtils.EMPTY);
        assertEquals(RETURN_MANAGE_ASSOCIATION_FIELDS, action.selectAssociatedOrganization());
        ComparisonUtil.assertEquivalent(new Organization(), action.getAssociatedOrganization());
        assertTrue(StringUtils.isEmpty(action.getAssociationOhrp()));
        assertTrue(StringUtils.isEmpty(action.getNesId()));
    }

    @Test
    public void testSelectAssociatedOrganization_PracticeSiteFirstAssociation() throws Exception {
        Organization nesOrg = OrganizationFactory.getInstance().create();
        when(mockOrganizationSearchService.getOrganization(nesOrg.getNesId())).thenReturn(nesOrg);

        action.setSearchKey(nesOrg.getNesId());
        action.setAssociationType(PRACTICE_SITE);
        action.prepare();
        assertEquals(RETURN_MANAGE_ASSOCIATION_FIELDS, action.selectAssociatedOrganization());
        ComparisonUtil.assertEquivalent(nesOrg, action.getAssociatedOrganization());
        assertTrue(StringUtils.isEmpty(action.getAssociationOhrp()));
    }

    @Test
    public void testSelectAssociatedOrganization_PracticeSiteAssociationAlreadyExistsInSystem() throws Exception {
        String ohrp = "12345";
        Organization nesOrg = OrganizationFactory.getInstance().create();
        PracticeSite practiceSite = (PracticeSite) nesOrg.createRole(PRACTICE_SITE);
        practiceSite.setOhrpAssuranceNumber(ohrp);
        when(mockOrganizationSearchService.getOrganization(nesOrg.getNesId())).thenReturn(nesOrg);

        action.setSearchKey(nesOrg.getNesId());
        action.setAssociationType(PRACTICE_SITE);
        action.prepare();
        assertEquals(RETURN_MANAGE_ASSOCIATION_FIELDS, action.selectAssociatedOrganization());
        ComparisonUtil.assertEquivalent(nesOrg, action.getAssociatedOrganization());
        assertEquals(ohrp, action.getAssociationOhrp());
    }

    @Test
    public void testSelectAssociatedOrganization_IRB() throws Exception {
        Organization nesOrg = OrganizationFactory.getInstance().create();
        when(mockOrganizationSearchService.getOrganization(nesOrg.getNesId())).thenReturn(nesOrg);

        action.setSearchKey(nesOrg.getNesId());
        action.setAssociationType(IRB);
        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.selectAssociatedOrganization());
    }

    @Test
    public void testSelectAssociatedOrganization_ClinicalLab() throws Exception {
        Organization nesOrg = OrganizationFactory.getInstance().create();
        when(mockOrganizationSearchService.getOrganization(nesOrg.getNesId())).thenReturn(nesOrg);

        action.setSearchKey(nesOrg.getNesId());
        action.setAssociationType(CLINICAL_LABORATORY);
        action.prepare();
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.selectAssociatedOrganization());
    }

    @Test
    public void testSelectAssociatedOrganization_AssociationAlreadyExists() throws Exception {
        Organization nesOrg = OrganizationFactory.getInstance().create();
        when(mockOrganizationSearchService.getOrganization(nesOrg.getNesId())).thenReturn(nesOrg);
        profile.addOrganizationAssociation(nesOrg, CLINICAL_LABORATORY);

        action.setSearchKey(nesOrg.getNesId());
        action.setAssociationType(CLINICAL_LABORATORY);
        action.prepare();
        assertEquals(ManageOrganizationAssociationsAction.RETURN_MANAGE_ASSOCIATION,
                action.selectAssociatedOrganization());
    }

    @Test
    public void testSaveOrganizationAjax_NewOrganization() throws Exception {
        Organization successOrg = OrganizationFactory.getInstance().create();

        action.setAssociatedOrganization(successOrg);
        action.setAssociationType(OrganizationRoleType.IRB);

        successOrg.setNesId(null);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedInstitutionalReviewBoard(profile, successOrg);
    }

    @Test
    public void testSaveOrganizationAjax_NesOrganization() throws Exception {
        Organization successOrg = OrganizationFactory.getInstance().create();
        String nesId = "nes_id";
        when(mockOrganizationSearchService.getOrganization(nesId)).thenReturn(successOrg);

        action.setAssociatedOrganization(successOrg);
        action.setAssociationType(OrganizationRoleType.IRB);
        action.setSearchKey(nesId);
        action.prepare();

        successOrg.setNesId(nesId);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedInstitutionalReviewBoard(profile, successOrg);
    }

    @Test
    public void testSaveOrganizationAjax_NesOrganization_ClinicalLab() throws Exception {
        Organization successOrg = OrganizationFactory.getInstance().create();
        String nesId = "nes_id";
        when(mockOrganizationSearchService.getOrganization(nesId)).thenReturn(successOrg);

        action.setAssociatedOrganization(successOrg);
        action.setAssociationType(OrganizationRoleType.CLINICAL_LABORATORY);
        action.setSearchKey(nesId);
        action.prepare();

        successOrg.setNesId(nesId);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedClinicalLab(profile, successOrg);
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite_NoOhrpOrTypeOrNesId() throws Exception {
        Organization successOrg = setUpForPracticeSiteSave(null, null, null);
        assertEquals(ActionSupport.INPUT, action.saveOrganizationAjax());
        verify(mockProfileService, never()).addAssociatedPracticeSite(eq(profile), eq(successOrg), anyString(),
                isNull(PracticeSiteType.class));
        assertTrue(action.hasFieldErrors());
        assertEquals(2, action.getFieldErrors().size());
        assertTrue(action.getFieldErrors().containsKey(OHRP_FIELD_NAME));
        assertTrue(action.getFieldErrors().containsKey(PRACTICE_SITE_TYPE_FIELD_NAME));
    }

    private Organization setUpForPracticeSiteSave(String nesId, String ohrp, PracticeSiteType type) throws Exception {
        Organization organization = OrganizationFactory.getInstance().create();
        String searchKey = "searchKey";
        when(mockOrganizationSearchService.getOrganization(searchKey)).thenReturn(organization);

        action.setAssociatedOrganization(organization);
        action.setAssociationType(PRACTICE_SITE);
        action.setSearchKey(searchKey);
        action.setOhrpRequired(true);
        action.setAssociationOhrp(ohrp);
        action.setPracticeSiteType(type);
        action.prepare();

        organization.setNesId(nesId);
        return organization;
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSiteWithNoTypeOrNesId() throws Exception {
        Organization successOrg = setUpForPracticeSiteSave(null, VALID_OHRP_NUMBER, null);
        assertEquals(ActionSupport.INPUT, action.saveOrganizationAjax());
        verify(mockProfileService, never()).addAssociatedPracticeSite(eq(profile), eq(successOrg), anyString(),
                isNull(PracticeSiteType.class));
        assertTrue(action.hasFieldErrors());
        assertEquals(1, action.getFieldErrors().size());
        assertTrue(action.getFieldErrors().containsKey(PRACTICE_SITE_TYPE_FIELD_NAME));
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSiteWithTypeAndNoNesId() throws Exception {
        Organization successOrg = setUpForPracticeSiteSave(null, VALID_OHRP_NUMBER, PracticeSiteType.CANCER_CENTER);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedPracticeSite(eq(profile), eq(successOrg), anyString(),
                eq(PracticeSiteType.CANCER_CENTER));
        assertFalse(action.hasFieldErrors());
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSiteWithNesIdAndNoType() throws Exception {
        Organization successOrg = setUpForPracticeSiteSave("nesId", VALID_OHRP_NUMBER, null);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedPracticeSite(eq(profile), eq(successOrg), anyString(),
                isNull(PracticeSiteType.class));
        assertFalse(action.hasFieldErrors());
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite() throws Exception {
        final String OHRP = VALID_OHRP_NUMBER;
        Organization successOrg = setUpForPracticeSiteSave("nesId", OHRP, PracticeSiteType.CANCER_CENTER);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedPracticeSite(profile, successOrg, OHRP, PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testSaveOrganizationAjax_AlreadyExists() throws Exception {

        Organization failOrg = OrganizationFactory.getInstance().create();

        when(mockProfileService.addAssociatedClinicalLab(eq(profile), eq(failOrg))).thenThrow(
                new AssociationAlreadyExistsException());

        action.setAssociationOhrp(null);
        action.setAssociatedOrganization(failOrg);
        action.setAssociationType(OrganizationRoleType.CLINICAL_LABORATORY);
        assertEquals(INPUT, action.saveOrganizationAjax());
        assertEquals(1, action.getActionErrors().size());
    }

    @Test
    public void testUpdateOhrp() throws Exception {
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        OrganizationAssociation association = profile.addOrganizationAssociation(org,
                OrganizationRoleType.PRACTICE_SITE);
        action.setAssociatedOrganization(org);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setAssociationType(OrganizationRoleType.PRACTICE_SITE);

        assertEquals(ActionSupport.NONE, action.updateOhrp());
        verify(mockProfileService).updateAssociationOhrp(association, VALID_OHRP_NUMBER);
    }

    @Test
    public void testUpdateOhrp_InvalidOhrpFormat() throws Exception {
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        profile.addOrganizationAssociation(org,
                OrganizationRoleType.PRACTICE_SITE);
        action.setAssociatedOrganization(org);
        action.setAssociationOhrp("ABCD1234567");
        action.setAssociationType(OrganizationRoleType.PRACTICE_SITE);

        assertEquals(ActionSupport.INPUT, action.updateOhrp());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("organization.association.error.ohrp.invalid"), action.getActionErrors().iterator()
                .next());
    }

    @Test
    public void testUpdateOhrp_NoOrganization() throws Exception {
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setAssociationType(OrganizationRoleType.PRACTICE_SITE);
        action.updateOhrp();
        assertEquals(ActionSupport.INPUT, action.updateOhrp());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("organization.association.error.ohrp.update"), action.getActionErrors().iterator()
                .next());
    }

    @Test
    public void testUpdateOhrp_NoMatchingAssociation() throws Exception {
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        action.setAssociatedOrganization(org);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setAssociationType(OrganizationRoleType.PRACTICE_SITE);
        action.updateOhrp();
        assertEquals(ActionSupport.INPUT, action.updateOhrp());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("organization.association.error.invalid.selection"), action.getActionErrors()
                .iterator().next());
    }

    @Test
    public void testUpdateOhrp_NoAssociationType() throws Exception {
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        profile.addOrganizationAssociation(org, OrganizationRoleType.PRACTICE_SITE);
        action.setAssociatedOrganization(org);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.updateOhrp();
        assertEquals(ActionSupport.INPUT, action.updateOhrp());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("organization.association.error.invalid.selection"), action.getActionErrors()
                .iterator().next());
    }

    @Test
    public void testUpdateOhrp_InvalidOhrp() throws Exception {
        String ohrp = null;
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        profile.addOrganizationAssociation(org, OrganizationRoleType.PRACTICE_SITE);
        action.setAssociatedOrganization(org);
        action.setAssociationOhrp(ohrp);
        action.setAssociationType(OrganizationRoleType.PRACTICE_SITE);
        assertEquals(ActionSupport.INPUT, action.updateOhrp());
        assertTrue(action.hasActionErrors());
        assertEquals(action.getText("organization.association.error.ohrp.invalid"), action.getActionErrors().iterator()
                .next());

    }

    @Test
    public void testRemoveOrganizationAssociation() throws Exception {
        Organization orgAssociation = OrganizationFactory.getInstance().create();
        profile.addOrganizationAssociation(orgAssociation, IRB);

        action.setAssociationType(IRB);
        action.setAssociatedOrganization(orgAssociation);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.removeOrganizationAssociation());
        verify(mockProfileService).deleteAssociatedOrganization(eq(profile), any(OrganizationAssociation.class));
    }

    @Test
    public void testRemoveOrganizationAssociation_NoAssociation() {

        action.setAssociationType(OrganizationRoleType.IRB);
        action.setAssociatedOrganization(null);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.removeOrganizationAssociation());
        verifyZeroInteractions(mockProfileService);
    }

    @Test
    public void testSaveOrganizationAjax_withoutRequiredOhrp() throws Exception {
        Organization org = OrganizationFactory.getInstance().create();
        org.setId(12345L);
        org.setNesId("1234t56");
        action.setAssociationType(PRACTICE_SITE);
        action.setOhrpRequired(true);
        action.setAssociatedOrganization(org);

        assertEquals(Action.INPUT, action.saveOrganizationAjax());
        assertTrue(action.hasFieldErrors());
        assertEquals(action.getText("organization.association.error.ohrp.invalid"),
                action.getFieldErrors().get(ManageOrganizationAssociationsAction.OHRP_FIELD_NAME).get(0));
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite_CtepUser_PhoneNumberIncluded() throws Exception {
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setPracticeSiteType(PracticeSiteType.CANCER_CENTER);
        action.setAssociatedOrganization(organization);
        action.setAssociationType(PRACTICE_SITE);
        investigator.setCtepUser(true);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        verify(mockProfileService).addAssociatedPracticeSite(profile, organization, VALID_OHRP_NUMBER,
                PracticeSiteType.CANCER_CENTER);
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite_CtepUser_PhoneNumberMissing_NewOrganization() throws Exception {
        organization.setPhoneNumber(null);
        organization.setNesId(null);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setPracticeSiteType(PracticeSiteType.CANCER_CENTER);
        action.setAssociatedOrganization(organization);
        action.setAssociationType(PRACTICE_SITE);
        investigator.setCtepUser(true);

        assertEquals(Action.INPUT, action.saveOrganizationAjax());
        assertTrue(action.hasFieldErrors());
        assertEquals(action.getText("phoneNumber.required"),
                action.getFieldErrors().get(ManageOrganizationAssociationsAction.PHONE_NUMBER_FIELD_NAME).get(0));
        verify(mockProfileService, never()).addAssociatedPracticeSite(profile, organization, VALID_OHRP_NUMBER,
                PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite_CtepUser_PhoneNumberMissing_ExistingOrganization()
            throws Exception {
        organization.setPhoneNumber(null);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);
        action.setPracticeSiteType(PracticeSiteType.CANCER_CENTER);
        action.setAssociatedOrganization(organization);
        action.setAssociationType(PRACTICE_SITE);
        investigator.setCtepUser(true);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        assertFalse(action.hasActionErrors());
        verify(mockProfileService).addAssociatedPracticeSite(profile, organization, VALID_OHRP_NUMBER,
                PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testSaveOrganizationAjax_PracticeSite_NonCtepUser_PhoneNumberNotRequired() throws Exception {
        organization.setPhoneNumber(null);
        action.setAssociatedOrganization(organization);
        action.setAssociationType(PRACTICE_SITE);
        action.setPracticeSiteType(PracticeSiteType.CANCER_CENTER);
        action.setAssociationOhrp(VALID_OHRP_NUMBER);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        assertFalse(action.hasActionErrors());
        verify(mockProfileService).addAssociatedPracticeSite(profile, organization, VALID_OHRP_NUMBER,
                PracticeSiteType.CANCER_CENTER);
    }

    @Test
    public void testSaveOrganizationAjax_Irb_CtepUser_PhoneNumberNotRequired() throws Exception {
        action.setAssociatedOrganization(organization);
        action.setAssociationType(IRB);
        investigator.setCtepUser(true);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveOrganizationAjax());
        assertFalse(action.hasActionErrors());
    }

}
