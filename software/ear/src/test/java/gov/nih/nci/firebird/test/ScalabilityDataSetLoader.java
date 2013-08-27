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
package gov.nih.nci.firebird.test;

import gov.nih.nci.firebird.cagrid.CaGridModule;
import gov.nih.nci.firebird.commons.selenium2.test.WebDriverModule;
import gov.nih.nci.firebird.commons.test.TestDataRemover;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.nes.NesIntegrationModule;
import gov.nih.nci.firebird.selenium2.framework.DataCleanUpStatementSource;
import gov.nih.nci.firebird.test.LoginAccount.CoordinatorLogin;
import gov.nih.nci.firebird.test.LoginAccount.InvestigatorLogin;
import gov.nih.nci.firebird.test.data.DataSetBuilder;
import gov.nih.nci.firebird.test.data.TestDataLoader;
import gov.nih.nci.firebird.test.nes.NesTestDataSource;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.awt.Toolkit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;

public class ScalabilityDataSetLoader {
    
    private static final boolean CREATE_TEST_USERS = true;

//    private static final int PROTOCOLS = 0;
//    private static final int REGISTRATIONS_PER_PROTOCOL = 10;
//    private static final int CTEP_INVESTIGATORS = 0;
//    private static final int CHUNK_SIZE = 10;
//
    private static final int PROTOCOLS = 20;
    private static final int REGISTRATIONS_PER_PROTOCOL = 10;
    private static final int CTEP_INVESTIGATORS = 200;
    private static final int CHUNK_SIZE = 10;

//    private static final int PROTOCOLS = 2000;
//    private static final int REGISTRATIONS_PER_PROTOCOL = 10;
//    private static final int CTEP_INVESTIGATORS = 20000;
//    private static final int CHUNK_SIZE = 100;

    private static int investigatorNumber = 1;
    private List<Person> testInvestigators;

    @Test
    public void loadScalabilityDataSet() throws Exception {
        System.out.println("Starting to load Scalibility Data Set");
        if (CREATE_TEST_USERS) {
            loadTestUsers();
        }
        loadCtepInvestigatorsAndRegistrations();
        loadProtocolRegistrations();
    }

    private void loadTestUsers() {
        DataSetBuilder builder = new DataSetBuilder(dataLoader, gridResources);
        List<FirebirdUser> investigators = Lists.newArrayList();
        for (InvestigatorLogin login : InvestigatorLogin.values()) {
            FirebirdUser investigator = builder.createInvestigatorWithCompleteProfile().withLogin(login).get();
            investigators.add(investigator);
            System.out.println(investigator.getPerson().getFirstName() + " " + investigator.getPerson().getLastName()
                    + " - " + investigator.getPerson().getEmail());
            for (OrganizationAssociation association : investigator.getInvestigatorRole().getProfile()
                    .getOrganizationAssociations(OrganizationRoleType.CLINICAL_LABORATORY)) {
                ClinicalLaboratory lab = (ClinicalLaboratory) association.getOrganizationRole();
                LaboratoryCertificate cert = new LaboratoryCertificate(LaboratoryCertificateType.CAP);
                cert.setEffectiveDate(DateUtils.addYears(new Date(), -1));
                cert.setExpirationDate(DateUtils.addYears(new Date(), 1));
                cert.setCertificateFile(FirebirdFileFactory.getInstance().create());
                lab.addCertificate(cert);
            }
        }

        testInvestigators = Lists.newArrayList(Lists.transform(investigators, new Function<FirebirdUser, Person>() {
            public Person apply(FirebirdUser user) {
                return user.getPerson();
            }
        }));

        for (CoordinatorLogin login : CoordinatorLogin.values()) {
            builder.createCoordinator().withLogin(login).withApprovedMangedInvestigator(investigators.remove(0));
        }
        builder.createSponsor();
        builder.build();
    }

    private void loadCtepInvestigatorsAndRegistrations() {
        for (int i = 0; i < CTEP_INVESTIGATORS / CHUNK_SIZE; i++) {
            Stopwatch stopwatch = new Stopwatch().start();
            DataSetBuilder builder = new DataSetBuilder(dataLoader, gridResources);
            AnnualRegistrationConfiguration configuration = builder.createAnnualRegistrationConfiguration().get();
            for (int j = 1; j <= CHUNK_SIZE; j++) {
                createInvestigatorAndRegistrations(builder, configuration);
            }
            builder.build();
            System.out.println("Finished creating " + ((i + 1) * CHUNK_SIZE) + " CTEP investigators in "
                    + stopwatch.elapsedTime(TimeUnit.SECONDS) + " seconds");
        }
    }

    private void createInvestigatorAndRegistrations(DataSetBuilder builder,
            AnnualRegistrationConfiguration configuration) {
        FirebirdUser investigator = builder.createInvestigatorWithCompleteProfile().asCtepUser()
                .withLogin("investigator" + investigatorNumber++).get();
        replacePersonIfNecessary(investigator);
        builder.createAnnualRegistration(investigator, configuration).complete()
                .withStatus(RegistrationStatus.APPROVED).withType(AnnualRegistrationType.INITIAL);
        builder.createAnnualRegistration(investigator, configuration).complete()
                .withStatus(RegistrationStatus.APPROVED).withType(AnnualRegistrationType.RENEWAL);
        builder.createAnnualRegistration(investigator, configuration).complete()
                .withStatus(RegistrationStatus.APPROVED).withType(AnnualRegistrationType.RENEWAL);
        builder.createAnnualRegistration(investigator, configuration).complete()
                .withStatus(RegistrationStatus.APPROVED).withType(AnnualRegistrationType.RENEWAL);
        builder.createAnnualRegistration(investigator, configuration).complete()
                .withStatus(RegistrationStatus.APPROVED).withType(AnnualRegistrationType.RENEWAL);
    }

    private void replacePersonIfNecessary(FirebirdUser investigator) {
        while (testInvestigators != null && testInvestigators.contains(investigator.getInvestigatorRole().getProfile().getPerson())) {
            investigator.getInvestigatorRole().getProfile().setPerson(gridResources.getNesTestDataSource().getPerson());
        }
    }

    private void loadProtocolRegistrations() {
        for (int i = 0; i < PROTOCOLS / CHUNK_SIZE; i++) {
            Stopwatch stopwatch = new Stopwatch().start();
            DataSetBuilder builder = new DataSetBuilder(dataLoader, gridResources);
            for (int j = 1; j <= CHUNK_SIZE; j++) {
                createProtocolAndRegistrations(builder);
            }
            builder.build();
            int protocolCount = (i + 1) * CHUNK_SIZE;
            int investigatorCount = protocolCount * REGISTRATIONS_PER_PROTOCOL;
            System.out.println("Finished creating " + protocolCount + " protocols and " + investigatorCount
                    + " investigators and protocol registrations in " + stopwatch.elapsedTime(TimeUnit.SECONDS)
                    + " seconds");
        }
    }

    private void createProtocolAndRegistrations(DataSetBuilder builder) {
        Protocol protocol = builder.createProtocol().get();
        for (int k = 0; k < REGISTRATIONS_PER_PROTOCOL; k++) {
            FirebirdUser investigator = builder.createInvestigatorWithCompleteProfile()
                    .withLogin("investigator" + investigatorNumber++).get();
            replacePersonIfNecessary(investigator);
            builder.createRegistration(protocol, investigator);
        }
    }

    /**************************** Loader Setup *************************************/

    @Inject
    private DataCleanUpStatementSource cleanUpStatementSource;

    @Inject
    private TargetGridResources gridResources;

    @Inject
    @Named("NesScalabilityTestDataSource")
    private NesTestDataSource testDataSource;

    @Inject
    private TestDataLoader dataLoader;

    @Inject
    private TestDataRemover dataRemover;

    private static Injector injector;

    @After
    public void tearDown() throws InterruptedException {
        Toolkit.getDefaultToolkit().beep();
        Thread.sleep(100);
        Toolkit.getDefaultToolkit().beep();
    }

    @Before
    public void setUp() throws Exception {
        getInjector().injectMembers(this);
        gridResources.setNesTestDataSource(testDataSource);
        dataRemover.doInitialCleanIfNecessary(getDataRemovalStatements());
    }

    private Injector getInjector() {
        if (injector == null) {
            Set<Module> allModules = Sets.newHashSet(getModules());
            allModules.add(new WebDriverModule());
            injector = Guice.createInjector(allModules);
        }
        return injector;
    }

    protected Set<? extends Module> getModules() {
        return Sets.newHashSet(new SeleniumEarTestModule(), new CaGridModule(), new NesIntegrationModule());
    }

    protected List<String> getDataRemovalStatements() {
        return cleanUpStatementSource.getDataRemovalStatements();
    }

}