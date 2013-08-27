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
package gov.nih.nci.firebird.test.data;

import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.test.LoginAccount.AllRolesLogin;
import gov.nih.nci.firebird.test.LoginAccount.CoordinatorLogin;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorDelegateLogin;
import gov.nih.nci.firebird.test.LoginAccount.SponsorLogin;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.util.Iterator;
import java.util.List;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class DataSetBuilder {

    private final Iterator<InvestigatorLogin> investigatorLoginIterator = Iterators
            .forArray(InvestigatorLogin.values());
    private final Iterator<CoordinatorLogin> coordinatorLoginIterator = Iterators.forArray(CoordinatorLogin.values());
    private final Iterator<SponsorLogin> sponsorLoginIterator = Iterators.forArray(SponsorLogin.values());
    private final Iterator<SponsorDelegateLogin> sponsorDelegateLoginIterator = Iterators.forArray(SponsorDelegateLogin
            .values());
    private final Iterator<AllRolesLogin> superUserIterator = Iterators.forArray(AllRolesLogin.values());

    private List<AbstractDataComponentBuilder<? extends PersistentObject>> componentBuilders = Lists.newArrayList();
    private final TestDataLoader dataLoader;
    private final TargetGridResources gridResources;
    private final CredentialTypesData credentialTypesData;
    private final FormTypesData formTypesData;

    @Inject
    public DataSetBuilder(TestDataLoader dataLoader, TargetGridResources gridResources) {
        this.dataLoader = dataLoader;
        this.gridResources = gridResources;
        getDataLoader().openSession();
        credentialTypesData = new CredentialTypesData(dataLoader);
        formTypesData = new FormTypesData(dataLoader);
    }

    public ProfileBuilder createProfile() {
        ProfileBuilder builder = new ProfileBuilder(gridResources, dataLoader, credentialTypesData);
        componentBuilders.add(builder);
        return builder;
    }

    public ProtocolBuilder createProtocol() {
        ProtocolBuilder builder = new ProtocolBuilder(gridResources, dataLoader, formTypesData);
        componentBuilders.add(builder);
        return builder;
    }

    public InvestigatorRegistrationBuilder createRegistration() {
        return createRegistration(createInvestigatorWithCompleteProfile().get());
    }

    public InvestigatorRegistrationBuilder createRegistration(InvestigatorProfile profile) {
        return createRegistration(createProtocol().get(), profile);
    }

    public InvestigatorRegistrationBuilder createRegistration(FirebirdUser investigator) {
        return createRegistration(createProtocol().get(), investigator);
    }

    public InvestigatorRegistrationBuilder createRegistration(Protocol protocol, FirebirdUser investigator) {
        return createRegistration(protocol, investigator.getInvestigatorRole().getProfile());
    }

    public InvestigatorRegistrationBuilder createRegistration(Protocol protocol, InvestigatorProfile profile) {
        InvestigatorRegistrationBuilder builder = new InvestigatorRegistrationBuilder(gridResources, dataLoader,
                protocol, profile);
        componentBuilders.add(builder);
        return builder;
    }

    public RevisedInvestigatorRegistrationBuilder createRevisedInvestigatorRegistration(
            InvestigatorRegistration registration) {
        RevisedInvestigatorRegistrationBuilder builder = new RevisedInvestigatorRegistrationBuilder(gridResources,
                dataLoader, registration);
        componentBuilders.add(builder);
        return builder;
    }

    public SubinvestigatorRegistrationBuilder createSubinvestigatorRegistration() {
        InvestigatorRegistration primaryRegistration = createRegistration().complete().get();
        FirebirdUser subinvestigator = createInvestigatorWithCompleteProfile().get();
        return createSubinvestigatorRegistration(subinvestigator, primaryRegistration);
    }

    public SubinvestigatorRegistrationBuilder createSubinvestigatorRegistration(FirebirdUser subinvestigator,
            InvestigatorRegistration primaryRegistration) {
        return createSubinvestigatorRegistration(subinvestigator.getInvestigatorRole().getProfile(),
                primaryRegistration);
    }

    public SubinvestigatorRegistrationBuilder createSubinvestigatorRegistration(InvestigatorProfile profile,
            InvestigatorRegistration primaryRegistration) {
        SubinvestigatorRegistrationBuilder builder = new SubinvestigatorRegistrationBuilder(gridResources, dataLoader,
                profile, primaryRegistration);
        componentBuilders.add(builder);
        return builder;
    }

    public InvestigatorBuilder createInvestigator() {
        return createInvestigator(createProfile().get());
    }

    public InvestigatorBuilder createInvestigatorWithCompleteProfile() {
        return createInvestigator(createProfile().complete().get());
    }

    public InvestigatorBuilder createInvestigator(InvestigatorProfile profile) {
        InvestigatorBuilder builder = new InvestigatorBuilder(gridResources, dataLoader, profile,
                investigatorLoginIterator);
        componentBuilders.add(builder);
        return builder;
    }

    public DataSet build() {
        List<PersistentObject> objectsToSave = Lists.newArrayList();
        for (AbstractDataComponentBuilder<? extends PersistentObject> builder : componentBuilders) {
            objectsToSave.add(builder.get());
        }
        dataLoader.save(objectsToSave);
        getDataLoader().closeSession();
        return new DataSet(dataLoader, credentialTypesData, componentBuilders);
    }

    public SponsorBuilder createSponsor() {
        SponsorBuilder builder = new SponsorBuilder(gridResources, dataLoader, sponsorLoginIterator,
                sponsorDelegateLoginIterator);
        componentBuilders.add(builder);
        return builder;
    }

    public CoordinatorBuilder createCoordinator() {
        CoordinatorBuilder builder = new CoordinatorBuilder(gridResources, dataLoader, coordinatorLoginIterator);
        componentBuilders.add(builder);
        return builder;
    }

    public SuperUserBuilder createSuperUser() {
        SuperUserBuilder builder = new SuperUserBuilder(gridResources, getDataLoader(), superUserIterator);
        componentBuilders.add(builder);
        return builder;
    }

    public AnnualRegistrationConfigurationBuilder createAnnualRegistrationConfiguration() {
        AnnualRegistrationConfigurationBuilder builder = new AnnualRegistrationConfigurationBuilder(gridResources,
                dataLoader, formTypesData);
        componentBuilders.add(builder);
        return builder;
    }

    public AnnualRegistrationBuilder createAnnualRegistration(FirebirdUser investigator,
            AnnualRegistrationConfiguration configuration) {
        AnnualRegistrationBuilder builder = new AnnualRegistrationBuilder(gridResources, dataLoader, investigator,
                configuration);
        componentBuilders.add(builder);
        return builder;
    }

    public AnnualRegistrationBuilder createAnnualRegistration(FirebirdUser investigator) {
        return createAnnualRegistration(investigator, createAnnualRegistrationConfiguration().get());
    }

    private TestDataLoader getDataLoader() {
        return dataLoader;
    }

}
