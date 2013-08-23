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
package gov.nih.nci.firebird.selenium2.tests.sponsor;

import static com.google.common.collect.Sets.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.nes.person.NesPersonData;
import gov.nih.nci.firebird.selenium2.framework.AbstractFirebirdWebDriverTest;
import gov.nih.nci.firebird.selenium2.pages.root.HomePage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.export.ExportCurationDataPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.export.OrganizationRolesTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.export.OrganizationsTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.representative.export.PersonsTab;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.data.DataSet;
import gov.nih.nci.firebird.test.data.DataSetBuilder;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

public class ExportCurationDataTest extends AbstractFirebirdWebDriverTest {

    @Inject
    private DataSetBuilder builder;
    private DataSet dataSet;
    private ExportCurationDataPage exportDataPage;
    private Organization activeOrganization;
    private Organization pendingOrganization;
    private Person activePerson;
    private Person pendingCurationPerson;
    private Person pendingUpdatesPerson;
    private Set<AbstractOrganizationRole> activeCurationOrganizationRoles;
    private Set<AbstractOrganizationRole> pendingCurationOrganizationRoles;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createDataSet();
        createTestOrganizations();
        createTestPersons();
        createTestOrganizationRoles();
        HomePage homePage = openHomePage(dataSet.getSponsorLogin());
        exportDataPage = homePage.getSponsorMenu().clickExportDataToCurate().getPage();
    }

    private void createTestOrganizations() {
        activeOrganization = OrganizationFactory.getInstance().create();
        pendingOrganization = OrganizationFactory.getInstance().create();
        pendingOrganization.setCurationStatus(CurationStatus.PENDING);
        dataSet.save(activeOrganization, pendingOrganization);
    }

    private void createTestPersons() {
        activePerson = PersonFactory.getInstance().create();
        pendingCurationPerson = PersonFactory.getInstance().create();
        pendingCurationPerson.setCurationStatus(CurationStatus.PENDING);
        pendingUpdatesPerson = PersonFactory.getInstance().create();
        ((NesPersonData) pendingUpdatesPerson.getExternalData()).requestUpdate();
        dataSet.save(activePerson, pendingCurationPerson, pendingUpdatesPerson);
    }

    private void createTestOrganizationRoles() {
        PracticeSite activePracticeSiteRole = getTestDataSource().getPracticeSite();
        ClinicalLaboratory activeClinicalLabRole = getTestDataSource().getClinicalLab();
        InstitutionalReviewBoard activeIrbRole = getTestDataSource().getIrb();
        PracticeSite pendingPracticeSiteRole = getTestDataSource().getPracticeSite();
        pendingPracticeSiteRole.getOrganization().setCurationStatus(CurationStatus.PENDING);
        ClinicalLaboratory pendingClinicalLabRole = getTestDataSource().getClinicalLab();
        pendingClinicalLabRole.getOrganization().setCurationStatus(CurationStatus.PENDING);
        InstitutionalReviewBoard pendingIrbRole = getTestDataSource().getIrb();
        pendingIrbRole.getOrganization().setCurationStatus(CurationStatus.PENDING);
        dataSet.save(activePracticeSiteRole, activeClinicalLabRole, activeIrbRole,
                pendingPracticeSiteRole, pendingClinicalLabRole, pendingIrbRole);
        activeCurationOrganizationRoles = newHashSet(activePracticeSiteRole, activeClinicalLabRole, activeIrbRole);
        pendingCurationOrganizationRoles = newHashSet(pendingPracticeSiteRole, pendingClinicalLabRole, pendingIrbRole);
    }

    private void createDataSet() {
        builder.createSponsor();
        dataSet = builder.build();
    }

    @Test
    public void testExportCurationData() throws IOException {
        checkPersons();
        checkOrganizations();
        checkRoles();
        curatePendingPerson();
        checkCuratedPersonRemoved();
    }

    private void checkPersons() throws IOException {
        PersonsTab personsTab = exportDataPage.clickPersonsTab();
        assertFalse(personsTab.getHelper().contains(activePerson));
        assertTrue(personsTab.getHelper().contains(pendingCurationPerson));
        assertTrue(personsTab.getHelper().contains(pendingUpdatesPerson));
        personsTab.getHelper().downloadAndCheckCsvFile(pendingCurationPerson,
                pendingUpdatesPerson);
    }

    private void checkOrganizations() throws IOException {
        OrganizationsTab organizationsTab = exportDataPage.clickOrganizationsTab();
        assertFalse(organizationsTab.getHelper().contains(activeOrganization));
        assertTrue(organizationsTab.getHelper().contains(pendingOrganization));
        for (AbstractOrganizationRole role : union(activeCurationOrganizationRoles,
                pendingCurationOrganizationRoles)) {
            assertFalse(organizationsTab.getHelper().contains(role.getOrganization()));
        }
        organizationsTab.getHelper().downloadAndCheckCsvFile(pendingOrganization);
    }

    private void checkRoles() throws IOException {
        OrganizationRolesTab organizationRolesTab = exportDataPage.clickOrganizationRolesTab();
        for (AbstractOrganizationRole organizationRole : activeCurationOrganizationRoles) {
            assertFalse(organizationRolesTab.getHelper().contains(organizationRole));
        }
        for (AbstractOrganizationRole organizationRole : pendingCurationOrganizationRoles) {
            assertTrue(organizationRolesTab.getHelper().contains(organizationRole));
        }
        organizationRolesTab.getHelper().downloadAndCheckCsvFile(pendingCurationOrganizationRoles);
    }

    private void curatePendingPerson() {
        pendingCurationPerson.setCurationStatus(CurationStatus.ACTIVE);
        dataSet.update(pendingCurationPerson);
    }

    private void checkCuratedPersonRemoved() throws IOException {
        PersonsTab personsTab = exportDataPage.clickPersonsTab();
        assertFalse(personsTab.getHelper().contains(activePerson));
        assertFalse(personsTab.getHelper().contains(pendingCurationPerson));
        assertTrue(personsTab.getHelper().contains(pendingUpdatesPerson));
        personsTab.getHelper().downloadAndCheckCsvFile(pendingUpdatesPerson);
    }

}
