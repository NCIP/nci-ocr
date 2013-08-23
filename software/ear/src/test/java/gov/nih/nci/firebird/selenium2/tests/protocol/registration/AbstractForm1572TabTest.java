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
package gov.nih.nci.firebird.selenium2.tests.protocol.registration;

import com.google.common.collect.Lists;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.AddOrganizationAssociationDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.SetPracticeSiteFieldsDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.Form1572Section;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.Form1572Tab;
import gov.nih.nci.firebird.test.ValueGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static gov.nih.nci.firebird.data.OrganizationRoleType.CLINICAL_LABORATORY;
import static gov.nih.nci.firebird.data.OrganizationRoleType.IRB;
import static gov.nih.nci.firebird.data.OrganizationRoleType.PRACTICE_SITE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test methods for the protocol and annual registration form 1572 tab tests.
 */
public abstract class AbstractForm1572TabTest extends AbstractFirebirdWebDriverTest {
    protected Form1572Tab form1572Tab;

    @Before
    public void setUpTab() throws Exception {
        setUpData();
        form1572Tab = openForm1572Tab();
        checkRegistrationPageHeader(form1572Tab);
        assertFalse(form1572Tab.isCommentsPresent());
    }

    protected abstract void setUpData();

    protected abstract Form1572Tab openForm1572Tab();

    protected abstract void checkRegistrationPageHeader(Form1572Tab form1572Tab);

    protected abstract AbstractRegistration getRegistration();

    /**
     * @return boolean indicating If OHRP Assurance number is required for submission.
     */
    protected abstract boolean isOhrpAssuranceNumberRequired();

    @Test
    public void testIRBSelectFunctionality() {
        List<OrganizationAssociation> irbs = Lists.newArrayList(getRegistration().getProfile()
                .getOrganizationAssociations(IRB));
        assertEquals(irbs.size(), form1572Tab.getIrbSection().getListings().size());

        for (OrganizationAssociation irb : irbs) {
            assertNotNull(form1572Tab.getIrbSection().getHelper().getListing(irb.getOrganizationRole()));
        }

        AbstractOrganizationRole irb1 = irbs.get(0).getOrganizationRole();
        AbstractOrganizationRole irb2 = irbs.get(1).getOrganizationRole();

        form1572Tab.getIrbSection().getHelper().getListing(irb1).select();
        verifySelectionsStatusAfterRefresh(IRB, irb1);

        form1572Tab.getIrbSection().getHelper().getListing(irb2).select();
        verifySelectionsStatusAfterRefresh(IRB, irb1, irb2);
    }

    protected void verifySelectionsStatusAfterRefresh(OrganizationRoleType roleType,
            AbstractOrganizationRole... selectedOrganizations) {
        verifySelections(roleType, selectedOrganizations);
        form1572Tab = refresh1572Tab(form1572Tab);
        verifySelections(roleType, selectedOrganizations);
    }

    protected abstract Form1572Tab refresh1572Tab(Form1572Tab form1572Tab2);

    private void verifySelections(OrganizationRoleType roleType, AbstractOrganizationRole... selectedOrganizations) {
        List<AbstractOrganizationRole> selectedOrganizationsList = Arrays.asList(selectedOrganizations);
        Form1572Section section = getOrganizationAssociationSection(roleType);
        for (OrganizationAssociation organizationAssociation : getRegistration().getProfile()
                .getOrganizationAssociations(roleType)) {
            if (selectedOrganizationsList.contains(organizationAssociation.getOrganizationRole())) {
                assertTrue(section.getHelper().getListing(organizationAssociation.getOrganizationRole()).isSelected());
            } else {
                assertFalse(section.getHelper().getListing(organizationAssociation.getOrganizationRole()).isSelected());
            }
        }
    }

    private Form1572Section getOrganizationAssociationSection(OrganizationRoleType roleType) {
        switch (roleType) {
        case CLINICAL_LABORATORY:
            return form1572Tab.getClinicalLabSection();
        case IRB:
            return form1572Tab.getIrbSection();
        case PRACTICE_SITE:
            return form1572Tab.getPracticeSiteSection();
        }
        return null;
    }

    @Test
    public void testIRBAddNewFunctionality() {
        InstitutionalReviewBoard irb = getTestDataSource().getIrb();
        addIrbTo1572(irb);
        form1572Tab.getIrbSection().getHelper().getListing(irb);
    }

    private void addIrbTo1572(InstitutionalReviewBoard irb) {
        AddOrganizationAssociationDialog addIrbDialog = form1572Tab.getIrbSection().clickAddAssociationButton();
        addIrbDialog.getHelper().searchAndSelectOrganization(irb);
    }

    @Test
    public void testPracticeSiteSelectFunctionality() {
        List<OrganizationAssociation> practiceSites = Lists.newArrayList(getRegistration().getProfile()
                .getOrganizationAssociations(PRACTICE_SITE));
        assertEquals(practiceSites.size(), form1572Tab.getPracticeSiteSection().getListings().size());
        for (OrganizationAssociation practiceSite : practiceSites) {
            assertNotNull(form1572Tab.getPracticeSiteSection().getHelper()
                    .getListing(practiceSite.getOrganizationRole()));
        }

        AbstractOrganizationRole practiceSite1 = practiceSites.get(0).getOrganizationRole();
        AbstractOrganizationRole practiceSite2 = practiceSites.get(1).getOrganizationRole();

        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite1).select();
        verifySelectionsStatusAfterRefresh(OrganizationRoleType.PRACTICE_SITE, practiceSite1);

        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite2).select();
        verifySelectionsStatusAfterRefresh(OrganizationRoleType.PRACTICE_SITE, practiceSite1, practiceSite2);

        form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite1).deselect();
        verifySelectionsStatusAfterRefresh(OrganizationRoleType.PRACTICE_SITE, practiceSite2);
    }

    @Test
    public void testPracticeSiteAddNewFunctionality() {
        PracticeSite practiceSite = getTestDataSource().getPracticeSite();
        addPracticeSiteTo1572(practiceSite);
        assertNotNull(form1572Tab.getPracticeSiteSection().getHelper().getListing(practiceSite));
    }

    protected void addPracticeSiteTo1572(PracticeSite practiceSite) {
        AddOrganizationAssociationDialog addPracticeSiteDialog = form1572Tab.getPracticeSiteSection()
                .clickAddAssociationButton();
        SetPracticeSiteFieldsDialog
                practiceSiteFieldsDialog = (SetPracticeSiteFieldsDialog) addPracticeSiteDialog.getHelper()
                .searchAndSelectOrganization(practiceSite);

        if (isOhrpAssuranceNumberRequired()) {
            practiceSiteFieldsDialog.typeOhrpNumber(ValueGenerator.getUniqueOhrpNumber());
        }
        form1572Tab = (Form1572Tab) practiceSiteFieldsDialog.clickSaveButton();
    }

    @Test
    public void testLabSelectFunctionality() {
        List<OrganizationAssociation> clinicalLabs = Lists.newArrayList(getRegistration().getProfile()
                .getOrganizationAssociations(CLINICAL_LABORATORY));
        assertEquals(clinicalLabs.size(), form1572Tab.getClinicalLabSection().getListings().size());
        for (OrganizationAssociation lab : clinicalLabs) {
            assertNotNull(form1572Tab.getClinicalLabSection().getHelper().getListing(lab.getOrganizationRole()));
        }

        AbstractOrganizationRole lab1 = clinicalLabs.get(0).getOrganizationRole();
        AbstractOrganizationRole lab2 = clinicalLabs.get(1).getOrganizationRole();

        form1572Tab.getClinicalLabSection().getHelper().getListing(lab1).select();
        verifySelectionsStatusAfterRefresh(CLINICAL_LABORATORY, lab1);

        form1572Tab.getClinicalLabSection().getHelper().getListing(lab2).select();
        verifySelectionsStatusAfterRefresh(CLINICAL_LABORATORY, lab1, lab2);

        form1572Tab.getClinicalLabSection().getHelper().getListing(lab1).deselect();
        verifySelectionsStatusAfterRefresh(CLINICAL_LABORATORY, lab2);
    }

    @Test
    public void testLabAddNewFunctionality() {
        ClinicalLaboratory lab = getTestDataSource().getClinicalLab();
        AddOrganizationAssociationDialog dialog = form1572Tab.getClinicalLabSection().clickAddAssociationButton();
        dialog.getHelper().searchAndSelectOrganization(lab);
        assertNotNull(form1572Tab.getClinicalLabSection().getHelper().getListing(lab));
    }

}
