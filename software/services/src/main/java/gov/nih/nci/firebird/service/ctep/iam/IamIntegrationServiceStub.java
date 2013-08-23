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
package gov.nih.nci.firebird.service.ctep.iam;

import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.coppa.po.IdentifiedPerson;
import gov.nih.nci.coppa.services.structuralroles.identifiedperson.common.IdentifiedPersonI;
import gov.nih.nci.firebird.cagrid.UserDetails;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.user.UserRoleType;
import gov.nih.nci.firebird.security.UserSessionInformation;
import gov.nih.nci.firebird.service.person.local.LocalPersonDataService;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.iso._21090.II;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Simple stubbed out implementation to use prior to actual CTEP IAM integration.
 */
public class IamIntegrationServiceStub implements IamIntegrationService {

    private static final int RANDOM_PERSON_OFFSET_RANGE = 5000;
    private static final String CTEP_ROOT = "2.16.840.1.113883.3.26.6.1";
    private static final String VALID_TEST_PASSWORD = "F1reb1rd!!";
    private static final String REGISTRATION_COORDINATOR_USERNAME_SUBSTRING = "coord";
    private static final String INVESTIGATOR_USERNAME_SUBSTRING = "inv";
    private static final String SPONSOR_USERNAME_SUBSTRING = "sponsor";
    private static final String SPONSOR_DELEGATE_USERNAME_SUBSTRING = "del";

    private String usernamePrefix;
    private IdentifiedPersonI identifiedPersonClient;
    private LocalPersonDataService localPersonService;

    @Override
    public UserSessionInformation authenticateUser(String username, String password) {
        if (VALID_TEST_PASSWORD.equals(password)) {
            return createSessionInformation(username);
        } else {
            return null;
        }
    }

    private UserSessionInformation createSessionInformation(String username) {
        String ctepId = getCtepIdentifier();
        UserSessionInformation sessionInformation;
        sessionInformation = new UserSessionInformation(getUsernamePrefix() + username);
        sessionInformation.setAccount(createDummyCtepUser(username, ctepId));
        return sessionInformation;
    }

    private UserDetails createDummyCtepUser(String username, String ctepId) {
        UserDetails ctepUser = new UserDetails();
        ctepUser.setFirstName(username);
        ctepUser.setLastName(username);
        ctepUser.setAddress1("Executive Blvd");
        ctepUser.setCity("Rockville");
        ctepUser.setStateCode("MD");
        ctepUser.setZipCode("20852");
        ctepUser.setEmailAddress(username + "@ctep.cancer.gov");
        ctepUser.setOrganization("CTEP");
        ctepUser.setCtepId(ctepId);
        return ctepUser;
    }

    private String getCtepIdentifier() {
        return findValidUnusedCtepIdentifier();
    }

    private String findValidUnusedCtepIdentifier() {
        IdentifiedPerson identifiedPerson;
        do {
            identifiedPerson = getCtepIdentifiedPerson();
        } while (!hasAvailableCtepId(identifiedPerson));
        return identifiedPerson.getAssignedId().getExtension();
    }

    private IdentifiedPerson getCtepIdentifiedPerson() {
        IdentifiedPerson searchIdentifiedPerson = new IdentifiedPerson();
        II ii = new II();
        ii.setRoot(CTEP_ROOT);
        searchIdentifiedPerson.setAssignedId(ii);
        try {
            return identifiedPersonClient.query(searchIdentifiedPerson, createLimitOffset())[0];
        } catch (RemoteException e) {
            Logger.getLogger(getClass()).error("Couldn't load CTEP IDs", e);
            throw new IllegalStateException(e);
        }
    }

    private LimitOffset createLimitOffset() {
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setOffset(RandomUtils.nextInt(RANDOM_PERSON_OFFSET_RANGE));
        limitOffset.setLimit(1);
        return limitOffset;
    }

    private boolean hasAvailableCtepId(IdentifiedPerson identifiedPerson) {
        String nesId = identifiedPerson.getPlayerIdentifier().getExtension();
        Person localPerson = localPersonService.getByExternalId(nesId);
        return localPerson == null;
    }

    @Override
    public Set<String> getGroups(String username) {
        Set<String> groups = Sets.newHashSet();
        if (username.contains(REGISTRATION_COORDINATOR_USERNAME_SUBSTRING)) {
            groups.add(UserRoleType.CTEP_REGISTRATION_COORDINATOR.getGroupName());
        }
        if (username.contains(INVESTIGATOR_USERNAME_SUBSTRING)) {
            groups.add(UserRoleType.CTEP_INVESTIGATOR.getGroupName());
            groups.add(UserRoleType.CTEP_VERIFIED_INVESTIGATOR.getGroupName());
        }
        if (username.contains(SPONSOR_USERNAME_SUBSTRING)) {
            groups.add(UserRoleType.CTEP_SPONSOR.getGroupName());
        }
        if (username.contains(SPONSOR_DELEGATE_USERNAME_SUBSTRING)) {
            groups.add(UserRoleType.CTEP_SPONSOR_DELEGATE.getGroupName());
        }
        return groups;
    }

    private String getUsernamePrefix() {
        return usernamePrefix;
    }

    @Inject
    void setUsernamePrefix(@Named(FirebirdConstants.LOGIN_CTEP_IAM_USERNAME_PREFIX)
    String usernamePrefix) {
        this.usernamePrefix = usernamePrefix;
    }

    @Inject
    void setIdentifiedPersonClient(IdentifiedPersonI identifiedPersonClient) {
        this.identifiedPersonClient = identifiedPersonClient;
    }

    @Inject
    void setLocalPersonService(LocalPersonDataService localPersonService) {
        this.localPersonService = localPersonService;
    }

}
