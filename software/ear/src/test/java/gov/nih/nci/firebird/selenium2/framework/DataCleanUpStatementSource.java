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
package gov.nih.nci.firebird.selenium2.framework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public final class DataCleanUpStatementSource {

    private final Set<String> noCleanupNesIds = new HashSet<String>();

    @Inject
    DataCleanUpStatementSource(@Named("sponsor.organization.nes.ids") Set<String> sponsorNesIds,
                               @Named("nih.oer.organization.nes.id") String nihOerOrganizationNesId) {
        this.noCleanupNesIds.addAll(sponsorNesIds);
        this.noCleanupNesIds.add(nihOerOrganizationNesId);
    }

    public List<String> getDataRemovalStatements() {
        return Lists.newArrayList(
                "delete from submitted_credential_data_snapshots",
                "delete from credential_snapshot",
                "delete from laboratory_certificate_snapshot",
                "delete from submitted_form_1572_irb_snapshots",
                "delete from submitted_form_1572_lab_snapshots",
                "delete from submitted_form_1572_practice_site_snapshots",
                "delete from managed_investigator",
                "delete from registration_coordinator_role",
                "delete from sponsor_role",
                "delete from firebird_user",
                "update registration set current_registration_id = null",
                "update registration set primary_registration_id = null",
                "delete from revised_registration_subinvestigator_registration",
                "delete from annual_registration_notification_email_addresses",
                "delete from registration",
                "delete from curriculum_vitae",
                "delete from submitted_credential_data",
                "delete from registration_attachments",
                "delete from additional_attachments",
                "delete from financial_disclosure_documentation",
                "delete from financial_disclosure",
                "delete from ctep_financial_disclosure_pharmaceuticals",
                "delete from ctep_financial_disclosure_documentation",
                "delete from ctep_financial_disclosure",
                "delete from form1572_labs",
                "delete from form1572_practicesites",
                "delete from form1572_irbs",
                "delete from form_1572",
                "delete from submitted_form_1572_data",
                "delete from organization_role_snapshot",
                "delete from organization_snapshot",
                "delete from person_snapshot",
                "delete from ctepform1572_practicesites",
                "delete from ctepform1572_irbs",
                "delete from ctepform1572_labs",
                "delete from ctep_form_1572",
                "delete from supplemental_investigator_data_form",
                "delete from formphrc_certificate",
                "delete from formphrc_submitted_certificate",
                "delete from submitted_certificate",
                "delete from human_research_certificate_form",
                "delete from laboratory_certificate",
                "delete from credential",
                "delete from organization_association",
                "delete from sub_investigator",
                "delete from shipping_designee",
                "delete from ordering_designee",
                "delete from investigatorprofile_file",
                "delete from investigator_profile",
                "delete from organization_role",
                "delete from protocol_firebirdfile",
                "delete from firebird_file where byte_data_source_id in (select id from byte_data_source where discriminator = 'LOB_HOLDER') "
                    + "and id not in (select keystore_file_id from root_keystore)",
                "delete from byte_data_source where discriminator = 'LOB_HOLDER' and id not in (select byte_data_source_id from firebird_file)",
                "delete from protocol_modification",
                "delete from protocol_revision",
                "delete from protocol_protocolagent",
                "delete from protocol_agent",
                "delete from protocol_investigator_form_optionality",
                "delete from protocol_subinvestigator_form_optionality",
                "delete from protocol_lead_organization",
                "delete from protocol",
                "delete from organization where nes_id not in ('" + Joiner.on("', '").join(noCleanupNesIds)  + "')",
                "delete from person",
                "delete from form_type where form_type_enum is null",
                "delete from AuditLogDetail",
                "delete from AuditLogRecord",
                "delete from configured_form_optionalities where registration_form_set_configuration_id != " +
                        "(select form_set_configuration_id from annual_registration_configuration where timestamp = (select min(timestamp) from annual_registration_configuration))",
                "delete from annual_registration_configuration where timestamp != (select min(timestamp) from annual_registration_configuration)"
            );    }

}
