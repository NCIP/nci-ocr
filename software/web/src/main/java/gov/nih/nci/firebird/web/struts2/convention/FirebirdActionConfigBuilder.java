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
package gov.nih.nci.firebird.web.struts2.convention;

import gov.nih.nci.firebird.web.action.FileDownloadAction;
import gov.nih.nci.firebird.web.action.MyAccountAction;
import gov.nih.nci.firebird.web.action.RemoveRoleAction;
import gov.nih.nci.firebird.web.action.TaskListAction;
import gov.nih.nci.firebird.web.action.coordinator.BrowseManagedInvestigatorsAction;
import gov.nih.nci.firebird.web.action.coordinator.ManageInvestigatorsAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.AnnualRegistrationAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.AnnualRegistrationTabAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.BrowseAnnualRegistrationsAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ManagePharmaceuticalCompaniesAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.SaveFda1572Action;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.SupplementalInvestigatorDataFormTabAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.AnnualRegistrationFormDownloadAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.ReactivateInvestigatorAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.ViewRegistrationApprovedNotificationAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.ViewOverviewAction;
import gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.WithdrawSubmissionAction;
import gov.nih.nci.firebird.web.action.investigator.profile.AddOrderingDesigneeAction;
import gov.nih.nci.firebird.web.action.investigator.profile.BrowseRegistrationCoordinatorsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ClinicalLabCertificateAction;
import gov.nih.nci.firebird.web.action.investigator.profile.CreatePrimaryOrganizationAction;
import gov.nih.nci.firebird.web.action.investigator.profile.CredentialsTabAction;
import gov.nih.nci.firebird.web.action.investigator.profile.DesigneeAssociationTabAction;
import gov.nih.nci.firebird.web.action.investigator.profile.FileTabAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageCertificateCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageCertificationCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageDegreeCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageExperienceAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageFellowshipCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageInternshipCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageLicenseCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageOrganizationAssociationsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManagePersonAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageRegistrationCoordinatorsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageResidencyCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageSpecialtiesCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.AddSubinvestigatorAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ManageWorkHistoryCredentialsAction;
import gov.nih.nci.firebird.web.action.investigator.profile.OrganizationAssociationsTabAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ProfessionalContactTabAction;
import gov.nih.nci.firebird.web.action.investigator.profile.ProfileHomeAction;
import gov.nih.nci.firebird.web.action.investigator.profile.RemoveOrderingDesigneeAction;
import gov.nih.nci.firebird.web.action.investigator.profile.RemoveShippingDesigneeAction;
import gov.nih.nci.firebird.web.action.investigator.profile.RemoveSubinvestigatorAction;
import gov.nih.nci.firebird.web.action.investigator.profile.SelectPrimaryOrganizationAction;
import gov.nih.nci.firebird.web.action.investigator.profile.SetShippingDesigneeAction;
import gov.nih.nci.firebird.web.action.investigator.profile.SubInvestigatorsAssociationTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.AdditionalAttachmentsTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.CompletionAction;
import gov.nih.nci.firebird.web.action.investigator.registration.FinancialDisclosureTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.HumanResearchCertificateTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.IrbSectionAction;
import gov.nih.nci.firebird.web.action.investigator.registration.LabSectionAction;
import gov.nih.nci.firebird.web.action.investigator.registration.PracticeSiteSectionAction;
import gov.nih.nci.firebird.web.action.investigator.registration.ProtocolRegistrationFormDownloadAction;
import gov.nih.nci.firebird.web.action.investigator.registration.RegistrationAction;
import gov.nih.nci.firebird.web.action.investigator.registration.RegistrationCertificateTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.RegistrationInvitationAction;
import gov.nih.nci.firebird.web.action.investigator.registration.RegistrationTabAction;
import gov.nih.nci.firebird.web.action.investigator.registration.AddSubinvestigatorToProtocolAction;
import gov.nih.nci.firebird.web.action.investigator.registration.SubinvestigatorTabAction;
import gov.nih.nci.firebird.web.action.investigator.settings.ManageCoordinatorAction;
import gov.nih.nci.firebird.web.action.login.LoginAction;
import gov.nih.nci.firebird.web.action.login.LogoutAction;
import gov.nih.nci.firebird.web.action.login.PrivacyDisclaimerSplashPageAction;
import gov.nih.nci.firebird.web.action.search.ClinicalLabSearchAction;
import gov.nih.nci.firebird.web.action.search.CtepInvestigatorSearchAction;
import gov.nih.nci.firebird.web.action.search.InstitutionalReviewBoardSearchAction;
import gov.nih.nci.firebird.web.action.search.InvestigatorSearchAction;
import gov.nih.nci.firebird.web.action.search.OrganizationSearchAction;
import gov.nih.nci.firebird.web.action.search.PersonSearchAction;
import gov.nih.nci.firebird.web.action.search.PharmaceuticalCompanySearchAction;
import gov.nih.nci.firebird.web.action.search.PracticeSiteSearchAction;
import gov.nih.nci.firebird.web.action.search.PrimaryOrganizationSearchAction;
import gov.nih.nci.firebird.web.action.sponsor.BrowseSponsorDelegatesAction;
import gov.nih.nci.firebird.web.action.sponsor.ajax.RemoveSponsorDelegateAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.investigator.BrowseCtepInvestigatorsAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.investigator.ViewRegistrationWithdrawalRequestAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.ApproveRegistrationAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.BrowseRegistrationsAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.RequiredFormsAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.ReviewRegistrationAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.ajax.DisqualifyInvestigatorAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.ajax.EditAnnualRegistrationFormAction;
import gov.nih.nci.firebird.web.action.sponsor.annual.registration.ajax.ToggleReviewOnHoldAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.AgentSearchAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.CreateProtocolAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.EditRegistrationFormsAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ExportCurationDataAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ImportProtocolsAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.InvestigatorsTabAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ManageRegistrationInvestigatorsAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.PacketOverviewAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ProtocolTabAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ProtocolsAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.RegistrationFormTabAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.RegistrationPacketAccessAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ReviewPacketAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ReviewRegistrationAjaxAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ReviewRegistrationFormAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.SelectInvestigatorAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.SubinvestigatorsTabAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.UpdateProtocolAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.ajax.CreateLeadOrganizationAction;
import gov.nih.nci.firebird.web.action.sponsor.protocol.export.ExportProtocolsTabAction;
import gov.nih.nci.firebird.web.action.user.InvestigatorVerificationPendingAction;
import gov.nih.nci.firebird.web.action.user.MissingExpectedInvestigatorRoleAction;
import gov.nih.nci.firebird.web.action.user.RequestAccountAction;
import gov.nih.nci.firebird.web.action.user.SponsorVerificationPendingAction;
import gov.nih.nci.firebird.web.action.user.registration.FlowEntryAction;
import gov.nih.nci.firebird.web.action.user.registration.FlowNavigationAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.InvestigatorSelectionPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.OrganizationSelectionPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.PersonSelectionPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.RoleSelectionPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.SponsorDelegatePageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.SponsorOrganizationsPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.VerificationPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.ViewRolesPageFlowAction;
import gov.nih.nci.firebird.web.action.user.registration.flow.ajax.FunctionalityWarningAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.convention.PackageBasedActionConfigBuilder;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

/**
 * Struts action config.
 */
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class FirebirdActionConfigBuilder extends PackageBasedActionConfigBuilder {

    /**
     * Builds the config builder.
     *
     * @param configuration the config
     * @param container the container
     * @param objectFactory the factory
     * @param redirectToSlash redirect to slash?
     * @param defaultParentPackage the default parent package.
     */
    @Inject
    public FirebirdActionConfigBuilder(Configuration configuration, Container container, ObjectFactory objectFactory,
            @Inject("struts.convention.redirect.to.slash")
            String redirectToSlash, @Inject("struts.convention.default.parent.package")
            String defaultParentPackage) {
        super(configuration, container, objectFactory, redirectToSlash, defaultParentPackage);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    protected Set<Class> findActions() {
        Set<Class> classes = new HashSet<Class>();
        classes.add(FileDownloadAction.class);
        classes.add(ProtocolRegistrationFormDownloadAction.class);
        classes.add(AnnualRegistrationFormDownloadAction.class);
        classes.add(LoginAction.class);
        classes.add(LogoutAction.class);
        classes.add(OrganizationSearchAction.class);
        classes.add(ClinicalLabSearchAction.class);
        classes.add(PracticeSiteSearchAction.class);
        classes.add(InstitutionalReviewBoardSearchAction.class);
        classes.add(ProfessionalContactTabAction.class);
        classes.add(FileTabAction.class);
        classes.add(ProfileHomeAction.class);
        classes.add(ManagePersonAction.class);
        classes.add(SelectPrimaryOrganizationAction.class);
        classes.add(CreatePrimaryOrganizationAction.class);
        classes.add(OrganizationAssociationsTabAction.class);
        classes.add(ManageOrganizationAssociationsAction.class);
        classes.add(SubInvestigatorsAssociationTabAction.class);
        classes.add(AddSubinvestigatorAction.class);
        classes.add(RemoveSubinvestigatorAction.class);
        classes.add(CredentialsTabAction.class);
        classes.add(ManageDegreeCredentialsAction.class);
        classes.add(ManageCertificateCredentialsAction.class);
        classes.add(ProtocolTabAction.class);
        classes.add(UpdateProtocolAction.class);
        classes.add(RegistrationFormTabAction.class);
        classes.add(ProtocolsAction.class);
        classes.add(ExportProtocolsTabAction.class);
        classes.add(AgentSearchAction.class);
        classes.add(InvestigatorsTabAction.class);
        classes.add(SubinvestigatorsTabAction.class);
        classes.add(DesigneeAssociationTabAction.class);
        classes.add(ManageSpecialtiesCredentialsAction.class);
        classes.add(TaskListAction.class);
        classes.add(RegistrationInvitationAction.class);
        classes.add(ManageCertificationCredentialsAction.class);
        classes.add(ManageLicenseCredentialsAction.class);
        classes.add(ManageExperienceAction.class);
        classes.add(RegistrationAction.class);
        classes.add(SubinvestigatorTabAction.class);
        classes.add(RegistrationTabAction.class);
        classes.add(PracticeSiteSectionAction.class);
        classes.add(LabSectionAction.class);
        classes.add(IrbSectionAction.class);
        classes.add(AddSubinvestigatorToProtocolAction.class);
        classes.add(ClinicalLabCertificateAction.class);
        classes.add(FinancialDisclosureTabAction.class);
        classes.add(
                gov.nih.nci.firebird.web.action.investigator.annual.registration.FinancialDisclosureTabAction.class);
        classes.add(ManagePharmaceuticalCompaniesAction.class);
        classes.add(HumanResearchCertificateTabAction.class);
        classes.add(RegistrationCertificateTabAction.class);
        classes.add(AdditionalAttachmentsTabAction.class);
        classes.add(CompletionAction.class);
        classes.add(ReviewRegistrationAjaxAction.class);
        classes.add(PacketOverviewAction.class);
        classes.add(ReviewRegistrationFormAction.class);
        classes.add(ReviewPacketAction.class);
        classes.add(FlowNavigationAction.class);
        classes.add(FlowEntryAction.class);
        classes.add(RoleSelectionPageFlowAction.class);
        classes.add(PersonSelectionPageFlowAction.class);
        classes.add(OrganizationSelectionPageFlowAction.class);
        classes.add(VerificationPageFlowAction.class);
        classes.add(SponsorOrganizationsPageFlowAction.class);
        classes.add(InvestigatorSelectionPageFlowAction.class);
        classes.add(ManageCoordinatorAction.class);
        classes.add(InvestigatorSearchAction.class);
        classes.add(BrowseManagedInvestigatorsAction.class);
        classes.add(ManageInvestigatorsAction.class);
        classes.add(MyAccountAction.class);
        classes.add(PersonSearchAction.class);
        classes.add(BrowseRegistrationCoordinatorsAction.class);
        classes.add(ManageRegistrationCoordinatorsAction.class);
        classes.add(PrivacyDisclaimerSplashPageAction.class);
        classes.add(RequestAccountAction.class);
        classes.add(SponsorDelegatePageFlowAction.class);
        classes.add(SponsorVerificationPendingAction.class);
        classes.add(InvestigatorVerificationPendingAction.class);
        classes.add(FunctionalityWarningAction.class);
        classes.add(RemoveRoleAction.class);
        classes.add(RegistrationPacketAccessAction.class);
        classes.add(CreateProtocolAction.class);
        classes.add(CreateLeadOrganizationAction.class);
        classes.add(ManageRegistrationInvestigatorsAction.class);
        classes.add(SelectInvestigatorAction.class);
        classes.add(EditRegistrationFormsAction.class);
        classes.add(BrowseSponsorDelegatesAction.class);
        classes.add(RemoveSponsorDelegateAction.class);
        classes.add(ViewRolesPageFlowAction.class);
        classes.add(ImportProtocolsAction.class);
        classes.add(MissingExpectedInvestigatorRoleAction.class);
        classes.add(ExportCurationDataAction.class);
        classes.add(RequiredFormsAction.class);
        classes.add(BrowseAnnualRegistrationsAction.class);
        classes.add(ViewOverviewAction.class);
        classes.add(AnnualRegistrationAction.class);
        classes.add(AnnualRegistrationTabAction.class);
        classes.add(gov.nih.nci.firebird.web.action.investigator.annual.registration.IrbSectionAction.class);
        classes.add(gov.nih.nci.firebird.web.action.investigator.annual.registration.LabSectionAction.class);
        classes.add(gov.nih.nci.firebird.web.action.investigator.annual.registration.PracticeSiteSectionAction.class);
        classes.add(SaveFda1572Action.class);
        classes.add(PharmaceuticalCompanySearchAction.class);
        classes.add(SupplementalInvestigatorDataFormTabAction.class);
        classes.add(
                gov.nih.nci.firebird.web.action.investigator.annual.registration.AdditionalAttachmentsTabAction.class);
        classes.add(BrowseRegistrationsAction.class);
        classes.add(gov.nih.nci.firebird.web.action.investigator.annual.registration.ajax.CompletionAction.class);
        classes.add(ReviewRegistrationAction.class);
        classes.add(EditAnnualRegistrationFormAction.class);
        classes.add(gov.nih.nci.firebird.web.action.sponsor.annual.registration.ReviewRegistrationFormAction.class);
        classes.add(ApproveRegistrationAction.class);
        classes.add(ToggleReviewOnHoldAction.class);
        classes.add(ViewRegistrationApprovedNotificationAction.class);
        classes.add(WithdrawSubmissionAction.class);
        classes.add(BrowseCtepInvestigatorsAction.class);
        classes.add(CtepInvestigatorSearchAction.class);
        classes.add(ManageInternshipCredentialsAction.class);
        classes.add(ManageResidencyCredentialsAction.class);
        classes.add(ManageFellowshipCredentialsAction.class);
        classes.add(ManageWorkHistoryCredentialsAction.class);
        classes.add(ViewRegistrationWithdrawalRequestAction.class);
        classes.add(ReactivateInvestigatorAction.class);
        classes.add(SetShippingDesigneeAction.class);
        classes.add(AddOrderingDesigneeAction.class);
        classes.add(RemoveOrderingDesigneeAction.class);
        classes.add(RemoveShippingDesigneeAction.class);
        classes.add(DisqualifyInvestigatorAction.class);
        classes.add(PrimaryOrganizationSearchAction.class);
        return classes;
    }
}
