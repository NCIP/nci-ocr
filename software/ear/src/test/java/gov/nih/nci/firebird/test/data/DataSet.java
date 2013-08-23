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

import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RevisedInvestigatorRegistration;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.test.LoginAccount;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DataSet {

    private final List<AbstractDataComponentBuilder<? extends PersistentObject>> componentBuilders;
    private final TestDataLoader dataLoader;
    private final CredentialTypesData credentialTypesData;
    private final FormTypesData formTypesData;

    DataSet(TestDataLoader dataLoader, CredentialTypesData credentialTypesData,
            FormTypesData formTypesData, List<AbstractDataComponentBuilder<? extends PersistentObject>> componentBuilders) {
        this.dataLoader = dataLoader;
        this.credentialTypesData = credentialTypesData;
        this.formTypesData = formTypesData;
        this.componentBuilders = componentBuilders;
    }

    public FirebirdUser getInvestigator() {
        return getInvestigators().get(0);
    }

    public List<FirebirdUser> getInvestigators() {
        List<InvestigatorBuilder> builders = getBuilders(InvestigatorBuilder.class);
        return getFirebirdUsers(builders);
    }

    private <T extends AbstractDataComponentBuilder<? extends PersistentObject>> List<T> getBuilders(
            Class<T> builderClass) {
        return Lists.newArrayList(Iterables.filter(componentBuilders, builderClass));
    }

    private List<FirebirdUser> getFirebirdUsers(List<? extends AbstractFirebirdUserBuilder<?>> builders) {
        return Lists.transform(builders, new Function<AbstractFirebirdUserBuilder<?>, FirebirdUser>() {
            @Override
            public FirebirdUser apply(AbstractFirebirdUserBuilder<?> builder) {
                return builder.get();
            }
        });
    }

    public LoginAccount getInvestigatorLogin() {
        return getInvestigatorLogins().get(0);
    }

    public List<LoginAccount> getInvestigatorLogins() {
        List<InvestigatorBuilder> builders = getBuilders(InvestigatorBuilder.class);
        return getLogins(builders);
    }

    public FirebirdUser getCoordinator() {
        return getCoordinators().get(0);
    }

    public List<FirebirdUser> getCoordinators() {
        List<CoordinatorBuilder> builders = getBuilders(CoordinatorBuilder.class);
        return getFirebirdUsers(builders);
    }

    public FirebirdUser getSuperUser() {
        List<SuperUserBuilder> builders = getBuilders(SuperUserBuilder.class);
        return getFirebirdUsers(builders).get(0);
    }

    public LoginAccount getCoordinatorLogin() {
        return getCoordinatorLogins().get(0);
    }

    public List<LoginAccount> getCoordinatorLogins() {
        List<CoordinatorBuilder> builders = getBuilders(CoordinatorBuilder.class);
        return getLogins(builders);
    }

    public FirebirdUser getSponsor() {
        return getSponsors().get(0);
    }

    public List<FirebirdUser> getSponsors() {
        List<SponsorBuilder> builders = getBuilders(SponsorBuilder.class);
        return getFirebirdUsers(builders);
    }

    public LoginAccount getSponsorLogin() {
        return getSponsorLogins().get(0);
    }

    public List<LoginAccount> getSponsorLogins() {
        List<SponsorBuilder> builders = getBuilders(SponsorBuilder.class);
        return getLogins(builders);
    }

    public LoginAccount getSuperUserLogin() {
        List<SuperUserBuilder> builders = getBuilders(SuperUserBuilder.class);
        return getLogins(builders).get(0);
    }

    private List<LoginAccount> getLogins(List<? extends AbstractFirebirdUserBuilder<?>> builders) {
        return Lists.transform(builders, new Function<AbstractFirebirdUserBuilder<?>, LoginAccount>() {
            @Override
            public LoginAccount apply(AbstractFirebirdUserBuilder<?> builder) {
                return builder.getLogin();
            }
        });
    }

    public InvestigatorProfile getInvestigatorProfile() {
        return getInvestigatorProfiles().get(0);
    }

    public List<InvestigatorProfile> getInvestigatorProfiles() {
        return Lists.transform(getInvestigators(), new Function<FirebirdUser, InvestigatorProfile>() {
            @Override
            public InvestigatorProfile apply(FirebirdUser investigator) {
                return investigator.getInvestigatorRole().getProfile();
            }
        });
    }

    public AnnualRegistrationConfiguration getAnnualRegistrationConfiguration() {
        return getAnnualRegistrationConfigurations().get(0);
    }

    public List<AnnualRegistrationConfiguration> getAnnualRegistrationConfigurations() {
        List<AnnualRegistrationConfigurationBuilder> builders = getBuilders(AnnualRegistrationConfigurationBuilder.class);
        return Lists.transform(builders,
                new Function<AnnualRegistrationConfigurationBuilder, AnnualRegistrationConfiguration>() {
                    @Override
                    public AnnualRegistrationConfiguration apply(AnnualRegistrationConfigurationBuilder builder) {
                        return builder.get();
                    }
                });
    }

    public AnnualRegistration getAnnualRegistration() {
        return getAnnualRegistrations().get(0);
    }

    public List<AnnualRegistration> getAnnualRegistrations() {
        List<AnnualRegistrationBuilder> builders = getBuilders(AnnualRegistrationBuilder.class);
        return Lists.transform(builders, new Function<AnnualRegistrationBuilder, AnnualRegistration>() {
            @Override
            public AnnualRegistration apply(AnnualRegistrationBuilder builder) {
                return builder.get();
            }
        });
    }

    public InvestigatorRegistration getInvestigatorRegistration() {
        return getInvestigatorRegistrations().get(0);
    }

    public List<InvestigatorRegistration> getInvestigatorRegistrations() {
        List<InvestigatorRegistrationBuilder> builders = getBuilders(InvestigatorRegistrationBuilder.class);
        return Lists.transform(builders, new Function<InvestigatorRegistrationBuilder, InvestigatorRegistration>() {
            @Override
            public InvestigatorRegistration apply(InvestigatorRegistrationBuilder builder) {
                return builder.get();
            }
        });
    }

    public RevisedInvestigatorRegistration getRevisedInvestigatorRegistration() {
        return getRevisedInvestigatorRegistrations().get(0);
    }

    public List<RevisedInvestigatorRegistration> getRevisedInvestigatorRegistrations() {
        List<RevisedInvestigatorRegistrationBuilder> builders = getBuilders(RevisedInvestigatorRegistrationBuilder.class);
        return Lists.transform(builders,
                new Function<RevisedInvestigatorRegistrationBuilder, RevisedInvestigatorRegistration>() {
                    @Override
                    public RevisedInvestigatorRegistration apply(RevisedInvestigatorRegistrationBuilder builder) {
                        return builder.get();
                    }
                });
    }

    public Protocol getProtocol() {
        return getProtocols().get(0);
    }

    public List<Protocol> getProtocols() {
        List<ProtocolBuilder> builders = getBuilders(ProtocolBuilder.class);
        return Lists.transform(builders, new Function<ProtocolBuilder, Protocol>() {
            @Override
            public Protocol apply(ProtocolBuilder builder) {
                return builder.get();
            }
        });
    }

    public SubInvestigatorRegistration getSubInvestigatorRegistration() {
        return getSubInvestigatorRegistrations().get(0);
    }

    public List<SubInvestigatorRegistration> getSubInvestigatorRegistrations() {
        List<SubinvestigatorRegistrationBuilder> builders = getBuilders(SubinvestigatorRegistrationBuilder.class);
        return Lists.transform(builders,
                new Function<SubinvestigatorRegistrationBuilder, SubInvestigatorRegistration>() {
                    @Override
                    public SubInvestigatorRegistration apply(SubinvestigatorRegistrationBuilder builder) {
                        return builder.get();
                    }
                });
    }

    public CredentialTypesData getCredentialTypesData() {
        return credentialTypesData;
    }

    public FormTypesData getFormTypesData() {
        return formTypesData;
    }

    public void reload() {
        dataLoader.openSession();
        for (AbstractDataComponentBuilder<? extends PersistentObject> builder : componentBuilders) {
            builder.reload();
        }
        dataLoader.closeSession();
    }

    public <T extends PersistentObject> T reloadObject(T objectToReload) {
        dataLoader.openSession();
        T reloadedObject = dataLoader.reload(objectToReload);
        if (reloadedObject != null) {
            lazyInitializeCollections(reloadedObject);
        }
        dataLoader.closeSession();
        return reloadedObject;
    }

    public void save(PersistentObject... dataComponents) {
        dataLoader.openSession();
        dataLoader.save(Arrays.asList(dataComponents));
        dataLoader.closeSession();
    }

    public void update(PersistentObject... dataComponents) {
        dataLoader.openSession();
        dataLoader.update(Arrays.asList(dataComponents));
        dataLoader.closeSession();
    }

    public void delete(PersistentObject objectToDelete) {
        try {
            dataLoader.openSession();
            dataLoader.delete(objectToDelete);
        } finally {
            dataLoader.closeSession();
        }
    }

    public <T extends PersistentObject> T getLastCreatedObject(Class<T> objectClass) {
        dataLoader.openSession();
        T lastCreatedObject = dataLoader.getLastCreatedObject(objectClass);
        lazyInitializeCollections(lastCreatedObject);
        dataLoader.closeSession();
        return lastCreatedObject;
    }

    private void lazyInitializeCollections(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (isCollectionGetter(method)) {
                try {
                    Object result = method.invoke(object);
                    Hibernate.initialize(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isCollectionGetter(Method method) {
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (!Collection.class.isAssignableFrom(method.getReturnType())) {
            return false;
        }
        return true;
    }

}
