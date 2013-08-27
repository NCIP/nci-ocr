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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static gov.nih.nci.firebird.nes.NesIdTestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InstitutionalReviewBoard;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ExportCurationDataAction.OrganizationListing;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ExportCurationDataAction.OrganizationRoleListing;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Test;

import com.csvreader.CsvReader;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class ExportCurationDataActionTest extends AbstractWebTest {

    @Inject
    private ExportCurationDataAction action;
    @Inject
    private PersonService mockPersonService;
    @Inject
    private OrganizationAssociationService mockOrganizationAssociationService;
    @Inject
    private OrganizationService mockOrganizationService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action.setServletResponse(getMockResponse());
    }

    @Test
    public void testEnter() {
        assertEquals(ActionSupport.SUCCESS, action.enter());
    }

    @Test
    public void testExportPersons_Null() throws Exception {
        doExportPersonsTest(null);
    }

    private void doExportPersonsTest(List<Person> persons) throws Exception {
        when(mockPersonService.getPersonsToBeCurated()).thenReturn(persons);
        assertEquals(ActionSupport.NONE, action.exportPersons());
        IteratorPredicate<Person> predicate = null;
        if (CollectionUtils.isNotEmpty(persons)) {
            predicate = new IteratorPredicate<Person>(persons.iterator()) {
                @Override
                public boolean doApply(CsvReader input) throws Exception {
                    return new EqualsBuilder().append(current.getNesId(), input.get(0))
                            .append(current.getDisplayNameForList(), input.get(1)).isEquals();
                }
            };
        }

        checkCsv(ExportCurationDataAction.PERSON_EXPORT_HEADERS, persons, predicate);
    }

    private void checkCsv(String[] headers, List<?> entities, IteratorPredicate<?> predicate) throws Exception {
        String content = getMockResponse().getContentAsString();
        assertTrue(StringUtils.isNotBlank(content));
        CsvReader reader = new CsvReader(new StringReader(content), ',');
        checkCsvHeaders(reader, headers);

        if (CollectionUtils.isNotEmpty(entities)) {
            checkCsvContent(reader, headers.length, predicate);
        }
    }

    private void checkCsvContent(CsvReader reader, int expectedNumColumns, IteratorPredicate<?> predicate)
            throws Exception {
        while (reader.readRecord()) {
            assertEquals(expectedNumColumns, reader.getColumnCount());
            assertTrue(predicate.apply(reader));
        }
        predicate.verifySpent();
    }

    private void checkCsvHeaders(CsvReader reader, String[] headers) throws IOException {
        assertTrue(reader.readHeaders());
        assertEquals(headers.length, reader.getHeaderCount());
        for (int i = 0; i < headers.length; i++) {
            assertEquals(headers[i], reader.getHeader(i));
        }
    }

    @Test
    public void testExportPersons_None() throws Exception {
        doExportPersonsTest(new ArrayList<Person>());
    }

    @Test
    public void testExportPersons() throws Exception {
        doExportPersonsTest(createPersons());
    }

    private ArrayList<Person> createPersons() {
        return Lists.newArrayList(PersonFactory.getInstance().create(), PersonFactory.getInstance().create());
    }

    @Test
    public void testExportOrganizations_Null() throws Exception {
        doExportOrganizationsTest(null);
    }

    private void doExportOrganizationsTest(List<Organization> organizations) throws Exception {
        when(mockOrganizationService.getOrganizationsToBeCurated()).thenReturn(organizations);
        assertEquals(ActionSupport.NONE, action.exportOrganizations());
        IteratorPredicate<Organization> predicate = null;
        if (CollectionUtils.isNotEmpty(organizations)) {
            predicate = new IteratorPredicate<Organization>(organizations.iterator()) {
                @Override
                public boolean doApply(CsvReader input) throws Exception {
                    return new EqualsBuilder().append(getNesIdExtension(current.getNesId()), input.get(0))
                            .append(current.getName(), input.get(1)).isEquals();
                }
            };
        }

        checkCsv(ExportCurationDataAction.ORGANIZATION_EXPORT_HEADERS, organizations, predicate);
    }

    @Test
    public void testExportOrganizations_None() throws Exception {
        doExportOrganizationsTest(new ArrayList<Organization>());
    }

    @Test
    public void testExportOrganizations() throws Exception {
        doExportOrganizationsTest(createOrganizations());
    }

    private ArrayList<Organization> createOrganizations() {
        return Lists.newArrayList(OrganizationFactory.getInstance().create(), OrganizationFactory.getInstance()
                .create());
    }

    @Test
    public void testExportRoles_Null() throws Exception {
        doExportRolesTest(null);
    }

    private void doExportRolesTest(List<AbstractOrganizationRole> roles) throws Exception {
        when(mockOrganizationAssociationService.getRolesToBeCurated()).thenReturn(roles);
        assertEquals(ActionSupport.NONE, action.exportRoles());
        IteratorPredicate<AbstractOrganizationRole> predicate = null;
        if (CollectionUtils.isNotEmpty(roles)) {
            predicate = new IteratorPredicate<AbstractOrganizationRole>(roles.iterator()) {
                @Override
                public boolean doApply(CsvReader input) throws Exception {
                    return new EqualsBuilder().append(getNesIdExtension(current.getNesId()), input.get(0))
                            .append(ExportCurationDataAction.getNesRoleType(current).getDisplay(), input.get(1))
                            .append(getNesIdExtension(current.getOrganization().getPlayerIdentifier()), input.get(2))
                            .append(current.getOrganization().getName(), input.get(3)).isEquals();
                }
            };
        }

        checkCsv(ExportCurationDataAction.ROLE_EXPORT_HEADERS, roles, predicate);
    }

    @Test
    public void testExportRoles_None() throws Exception {
        doExportRolesTest(new ArrayList<AbstractOrganizationRole>());
    }

    @Test
    public void testExportRoles() throws Exception {
        doExportRolesTest(createRoles());
    }

    private ArrayList<AbstractOrganizationRole> createRoles() {
        Organization org = OrganizationFactory.getInstance().create();
        org.setPlayerIdentifier(TEST_NES_ID_STRING);
        PracticeSite practiceSite = new PracticeSite();
        ClinicalLaboratory lab = new ClinicalLaboratory();
        InstitutionalReviewBoard irb = new InstitutionalReviewBoard();

        org.addRole(practiceSite);
        org.addRole(lab);
        org.addRole(irb);
        return Lists.newArrayList(lab, irb, practiceSite);
    }

    private abstract class IteratorPredicate<T> implements Predicate<CsvReader> {

        protected Iterator<T> iter;
        protected T current;

        public IteratorPredicate(Iterator<T> iter) {
            this.iter = iter;
        }

        public void verifySpent() {
            assertFalse(iter.hasNext());
        }

        protected abstract boolean doApply(CsvReader input) throws Exception;

        @Override
        public boolean apply(CsvReader input) {
            try {
                current = iter.next();
                return doApply(input);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
    }

    @Test
    public void testGetPersonsToBeCuratedJson() throws JSONException {
        List<Person> persons = createPersons();
        when(mockPersonService.getPersonsToBeCurated()).thenReturn(persons);
        String json = action.getPersonsToBeCuratedJson();
        String expectedJson = JSONUtil.serialize(persons, null, null, false, true, false);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testGetOrganizationsToBeCuratedJson() throws JSONException {
        List<Organization> organizations = createOrganizations();
        when(mockOrganizationService.getOrganizationsToBeCurated()).thenReturn(organizations);
        String json = action.getOrganizationsToBeCuratedJson();

        String expectedJson = JSONUtil.serialize(toOrganizationListings(organizations));
        assertEquals(expectedJson, json);
    }

    private List<OrganizationListing> toOrganizationListings(List<Organization> organizations) {
        List<OrganizationListing> listings = Lists.newArrayList();
        for (Organization organization : organizations) {
            listings.add(new OrganizationListing(organization));
        }
        return listings;
    }

    @Test
    public void testGetRolesToBeCuratedJson() throws JSONException {
        List<AbstractOrganizationRole> roles = createRoles();
        when(mockOrganizationAssociationService.getRolesToBeCurated()).thenReturn(roles);
        String json = action.getRolesToBeCuratedJson();
        String expectedJson = JSONUtil.serialize(toOrganizationRoleListings(roles));
        assertEquals(expectedJson, json);
    }

    private List<OrganizationRoleListing> toOrganizationRoleListings(List<AbstractOrganizationRole> roles) {
        List<OrganizationRoleListing> listings = Lists.newArrayList();
        for (AbstractOrganizationRole role : roles) {
            listings.add(new OrganizationRoleListing(role));
        }
        return listings;
    }

}
