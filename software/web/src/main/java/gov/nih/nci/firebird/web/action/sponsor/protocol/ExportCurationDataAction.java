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

import gov.nih.nci.firebird.data.AbstractOrganizationRole;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.NesId;
import gov.nih.nci.firebird.service.organization.OrganizationAssociationService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.web.action.FirebirdActionSupport;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Namespaces;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.csvreader.CsvWriter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Action class for handling the navigation and activities related to exporting data to be curated.
 */
@Namespaces(value = { @Namespace("/sponsor/representative/export"), @Namespace("/sponsor/representative/export/ajax") })
public class ExportCurationDataAction extends FirebirdActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private final OrganizationService organizationService;
    private final PersonService personService;
    private final OrganizationAssociationService associationService;

    static final String[] PERSON_EXPORT_HEADERS = {"NES_ID", "PERSON_NAME" };
    static final String[] ORGANIZATION_EXPORT_HEADERS = {"NES_ID", "ORGANIZATION_NAME" };
    static final String[] ROLE_EXPORT_HEADERS = {"NES_ID", "STRUCTURAL_ROLE_TYPE", "ORGANIZATION_PLAYER_NES_ID",
            "ORGANIZATION_PLAYER_NAME" };

    /**
     * @param organizationService the organization service for looking up Organizations needing to be curated.
     * @param personService The Person Service for looking up Persons needing to be curated
     * @param associationService The Organization Association Service for looking up Structural Roles that need to be
     *            curated.
     */
    @Inject
    public ExportCurationDataAction(OrganizationService organizationService, PersonService personService,
            OrganizationAssociationService associationService) {
        this.organizationService = organizationService;
        this.personService = personService;
        this.associationService = associationService;
    }

    /**
     * Navigate to the Single Protocol View page or the browse protocols list page.
     *
     * @return the struts forward.
     */
    @Actions(value = {
            @Action(value = "enterDataToCurate", results = @Result(location = "export_data_to_be_curated.jsp")),
            @Action(value = "viewPersonsToCurate", results = @Result(location = "view_persons_to_curate.jsp")),
            @Action(value = "viewOrganizationsToCurate", results = @Result(
                    location = "view_organizations_to_curate.jsp")),
            @Action(value = "viewOrganizationRolesToCurate", results = @Result(
                    location = "view_organization_roles_to_curate.jsp")) })
    public String enter() {
        return SUCCESS;
    }

    /**
     * Writes the CSV formatted values to the output stream and returns NONE.
     *
     * @return NONE
     * @throws IOException when writing to output fails.
     */
    @Action("exportPersons")
    public String exportPersons() throws IOException {
        List<List<String>> personRecords = Lists.newArrayList();
        List<Person> personsToBeCurated = getPersonsToBeCurated();

        if (CollectionUtils.isNotEmpty(personsToBeCurated)) {
            for (Person person : personsToBeCurated) {
                List<String> record = Lists.newArrayList();
                record.add(person.getNesId());
                record.add(person.getDisplayNameForList());
                personRecords.add(record);
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + getText("sponsor.export.persons.file.name") + "\"");
        writeCsv(PERSON_EXPORT_HEADERS, personRecords);
        return NONE;
    }

    private void writeCsv(String[] headers, List<List<String>> data) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private, must-revalidate");
        CsvWriter writer = new CsvWriter(new OutputStreamWriter(response.getOutputStream()), ',');

        if (headers != null) {
            writer.writeRecord(headers);
        }
        if (CollectionUtils.isNotEmpty(data)) {
            for (List<String> record : data) {
                writer.writeRecord(record.toArray(new String[] {}));
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Writes the CSV formatted values to the output stream and returns NONE.
     *
     * @return NONE
     * @throws IOException if writing output fails.
     */
    @Action("exportOrganizations")
    public String exportOrganizations() throws IOException {
        List<List<String>> organizationRecords = Lists.newArrayList();
        List<Organization> organizationsToBeCurated = getOrganizationsToBeCurated();

        if (CollectionUtils.isNotEmpty(organizationsToBeCurated)) {
            for (Organization organization : organizationsToBeCurated) {
                List<String> record = Lists.newArrayList();
                record.add(new NesId(organization.getNesId()).getExtension());
                record.add(organization.getName());
                organizationRecords.add(record);
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + getText("sponsor.export.organizations.file.name") + "\"");
        writeCsv(ORGANIZATION_EXPORT_HEADERS, organizationRecords);
        return NONE;
    }

    /**
     * Writes the CSV formatted values to the output stream and returns NONE.
     *
     * @return NONE
     * @throws IOException if writing output fails.
     */
    @Action("exportRoles")
    public String exportRoles() throws IOException {
        List<List<String>> roleRecords = Lists.newArrayList();
        List<AbstractOrganizationRole> rolesToBeCurated = getRolesToBeCurated();

        if (CollectionUtils.isNotEmpty(rolesToBeCurated)) {
            for (AbstractOrganizationRole role : rolesToBeCurated) {
                List<String> record = Lists.newArrayList();
                record.add(new NesId(role.getNesId()).getExtension());
                record.add(getNesRoleType(role).getDisplay());
                record.add(new NesId(role.getOrganization().getPlayerIdentifier()).getExtension());
                record.add(role.getOrganization().getName());
                roleRecords.add(record);
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + getText("sponsor.export.organization.roles.file.name") + "\"");
        writeCsv(ROLE_EXPORT_HEADERS, roleRecords);
        return NONE;
    }

    static NesIIRoot getNesRoleType(AbstractOrganizationRole role) {
        String nesId = role.getOrganization().getNesId();
        return new NesId(nesId).getRootType();
    }

    @Override
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.response = servletResponse;
    }

    /**
     * @return JSON of Persons to be Curated.
     * @throws JSONException If there is is an exception serializing data
     */
    public String getPersonsToBeCuratedJson() throws JSONException {
        return JSONUtil.serialize(getPersonsToBeCurated(), null, null, false, true, false);
    }

    /**
     * @return the List of Persons to be Curated.
     */
    private List<Person> getPersonsToBeCurated() {
        return personService.getPersonsToBeCurated();
    }

    /**
     * @return JSON of Organizations to be Curated.
     * @throws JSONException If there is is an exception serializing data
     */
    public String getOrganizationsToBeCuratedJson() throws JSONException {
        return JSONUtil.serialize(getOrganizationListingsToBeCurated());
    }

    private List<OrganizationListing> getOrganizationListingsToBeCurated() {
        List<OrganizationListing> listings = Lists.newArrayList();
        for (Organization organization : getOrganizationsToBeCurated()) {
            listings.add(new OrganizationListing(organization));
        }
        return listings;
    }

    /**
     * @return the organizationsToBeCurated
     */
    private List<Organization> getOrganizationsToBeCurated() {
        return organizationService.getOrganizationsToBeCurated();
    }

    /**
     * @return JSON of Organization Roles to be Curated.
     * @throws JSONException If there is is an exception serializing data
     */
    public String getRolesToBeCuratedJson() throws JSONException {
        return JSONUtil.serialize(getRoleListingsToBeCurated());
    }

    private List<OrganizationRoleListing> getRoleListingsToBeCurated() {
        List<OrganizationRoleListing> listings = Lists.newArrayList();
        for (AbstractOrganizationRole role : getRolesToBeCurated()) {
            listings.add(new OrganizationRoleListing(role));
        }
        return listings;
    }

    /**
     * @return the rolesToBeCurated
     */
    private List<AbstractOrganizationRole> getRolesToBeCurated() {
        return associationService.getRolesToBeCurated();
    }

    /**
     * Class to describe an organization listing in the curation grid.
     */
    public static class OrganizationListing {

        private final Long id;
        private final String nesId;
        private final String name;

        /**
         * @param organization organization
         */
        public OrganizationListing(Organization organization) {
            id = organization.getId();
            nesId = new NesId(organization.getNesId()).getExtension();
            name = organization.getName();
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the nesId
         */
        public String getNesId() {
            return nesId;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

    }

    /**
     * Class to describe an organization role listing in the curation grid.
     */
    public static class OrganizationRoleListing {

        private final Long id;
        private final String roleNesId;
        private final String roleType;
        private final String organizationNesId;
        private final String name;

        /**
         * @param role role
         */
        public OrganizationRoleListing(AbstractOrganizationRole role) {
            id = role.getId();
            roleNesId = new NesId(role.getNesId()).getExtension();
            roleType = getNesRoleType(role).getDisplay();
            organizationNesId = new NesId(role.getOrganization().getPlayerIdentifier()).getExtension();
            name = role.getOrganization().getName();
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the roleNesId
         */
        public String getRoleNesId() {
            return roleNesId;
        }

        /**
         * @return the roleType
         */
        public String getRoleType() {
            return roleType;
        }

        /**
         * @return the organizationNesId
         */
        public String getOrganizationNesId() {
            return organizationNesId;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

    }

}