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
package gov.nih.nci.firebird.nes;

import gov.nih.nci.coppa.po.Organization;
import gov.nih.nci.coppa.po.Person;
import java.rmi.RemoteException;
import static org.junit.Assert.*;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.coppa.services.structuralroles.healthcarefacility.common.HealthCareFacilityI;
import gov.nih.nci.coppa.services.structuralroles.healthcareprovider.common.HealthCareProviderI;
import gov.nih.nci.coppa.services.structuralroles.organizationalcontact.common.OrganizationalContactI;
import gov.nih.nci.coppa.services.structuralroles.oversightcommittee.common.OversightCommitteeI;
import gov.nih.nci.coppa.services.structuralroles.researchorganization.common.ResearchOrganizationI;

import org.junit.Test;

import com.google.inject.Inject;
import gov.nih.nci.coppa.common.LimitOffset;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.firebird.nes.person.PersonTranslator;
import gov.nih.nci.firebird.test.AbstractIntegrationTest;
import gov.nih.nci.iso21090.extensions.Id;
import org.apache.commons.lang.StringUtils;
import org.iso._21090.ENON;
import org.iso._21090.ENXP;
import org.iso._21090.TEL;
import org.iso._21090.TELPhone;

public class NesConnectionTest extends AbstractIntegrationTest {

    @Inject
    private PersonI personI;
    @Inject
    private OrganizationI orgServiceI;
    @Inject
    private HealthCareFacilityI healthCareFacilityI;
    @Inject
    private OversightCommitteeI oversightCommitteeI;
    @Inject
    private ResearchOrganizationI researchOrganizationI;
    @Inject
    private HealthCareProviderI healthCareProviderI;
    @Inject
    private OrganizationalContactI organizationalContactI;

    private final LimitOffset lo = new LimitOffset();

    {
        lo.setOffset(0);
        lo.setLimit(20);
    }

    @Test
    public void testPersonService() throws InterruptedException {
        doRunClients(new PersonRunner(), new PersonRunner(), new PersonRunner(), new PersonRunner(), new PersonRunner());
    }

    @Test
    public void testOrganizationService() throws InterruptedException {
        doRunClients(new OrganizationRunner(), new OrganizationRunner(), new OrganizationRunner(),
                new OrganizationRunner(), new OrganizationRunner());
    }

    @Test
    public void testHealthCareProviderService() throws InterruptedException {
        doRunClients(new HealthCareProviderRunner(), new HealthCareProviderRunner(), new HealthCareProviderRunner(),
                new HealthCareProviderRunner(), new HealthCareProviderRunner());
    }

    @Test
    public void testOrganizationalContactService() throws InterruptedException {
        doRunClients(new OrganizationalContactRunner(), new OrganizationalContactRunner(),
                new OrganizationalContactRunner(), new OrganizationalContactRunner(), new OrganizationalContactRunner());
    }

    @Test
    public void testHealthCareFacilityService() throws InterruptedException {
        doRunClients(new HealthCareFacilityRunner(), new HealthCareFacilityRunner(), new HealthCareFacilityRunner(),
                new HealthCareFacilityRunner(), new HealthCareFacilityRunner());
    }

    @Test
    public void testOversightCommitteeService() throws InterruptedException {
        doRunClients(new OversightCommitteeRunner(), new OversightCommitteeRunner(), new OversightCommitteeRunner(),
                new OversightCommitteeRunner(), new OversightCommitteeRunner());
    }

    @Test
    public void testResearchOrganizationService() throws InterruptedException {
        doRunClients(new ResearchOrganizationRunner(), new ResearchOrganizationRunner(),
                new ResearchOrganizationRunner(), new ResearchOrganizationRunner(), new ResearchOrganizationRunner());
    }

    @Test
    public void testAllAtOnce() throws InterruptedException {
        doRunClients(new PersonRunner(), new PersonRunner(), new PersonRunner(), new OrganizationRunner(),
                new OrganizationRunner(), new OrganizationRunner(), new HealthCareProviderRunner(),
                new HealthCareProviderRunner(), new HealthCareProviderRunner(), new OrganizationalContactRunner(),
                new OrganizationalContactRunner(), new OrganizationalContactRunner(), new HealthCareFacilityRunner(),
                new HealthCareFacilityRunner(), new HealthCareFacilityRunner(), new OversightCommitteeRunner(),
                new OversightCommitteeRunner(), new OversightCommitteeRunner(), new ResearchOrganizationRunner(),
                new ResearchOrganizationRunner(), new ResearchOrganizationRunner());
    }

    private void doRunClients(ClientRunner... threads) throws InterruptedException {
        for (ClientRunner t : threads) {
            t.start();
        }

        for (ClientRunner t : threads) {
            t.join();
        }

        for (ClientRunner t : threads) {
            if (t.error != null) {
                t.error.printStackTrace();
                fail(t.error.toString());
            }
        }
    }

    private Id[] getPersonPlayerIds() throws RemoteException {
        gov.nih.nci.coppa.po.Person nesPersons = PersonTranslator.buildNesPerson("", "smi");
        gov.nih.nci.coppa.po.Person[] results = personI.query(nesPersons, lo);
        Id[] players = new Id[results.length];
        for (int i = 0; i < results.length; i++) {
            players[i] = PersonTranslator.buildId(NesTranslatorHelperUtils.handleIi(results[i].getIdentifier()));
        }
        return players;
    }

    private Id[] getOrganizationPlayerIds() throws RemoteException {
        gov.nih.nci.coppa.po.Organization searchOrganization = createSearchOrganization();
        gov.nih.nci.coppa.po.Organization[] results = orgServiceI.query(searchOrganization, lo);
        Id[] players = new Id[results.length];
        for (int i = 0; i < results.length; i++) {
            NesId nesId = new NesId(results[i].getIdentifier());
            players[i] = nesId.toId();
        }
        return players;
    }

    private Organization createSearchOrganization() {
        Organization organization = new Organization();
        ENON name = new ENON();
        ENXP namePart = new ENXP();
        namePart.setValue("can");
        name.getPart().add(namePart);
        organization.setName(name);
        return organization;
    }

    private abstract class ClientRunner extends Thread {
        Exception error;

        @Override
        public final void run() {
            try {
                doRun();
            } catch (Exception ex) {
                error = ex;
            }
        }

        protected abstract void doRun() throws Exception;

    }

    private class PersonRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            gov.nih.nci.coppa.po.Person nesObject = PersonTranslator.buildNesPerson("", "smi");
            gov.nih.nci.coppa.po.Person[] results = personI.query(nesObject, lo);
            nesObject = personI.getById(PersonTranslator.buildId(NesTranslatorHelperUtils.handleIi(results[0]
                    .getIdentifier()))); // update
            fixTelFormat(nesObject);
            personI.update(nesObject);
        }

        private void fixTelFormat(Person nesObject) {
            for (TEL tel : nesObject.getTelecomAddress().getItem()) {
                if (tel instanceof TELPhone) {
                    tel.setValue(StringUtils.replaceChars(tel.getValue(), "()", ""));
                }
            }
        }
    }

    private class OrganizationRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            gov.nih.nci.coppa.po.Organization searchOrganization = createSearchOrganization();
            gov.nih.nci.coppa.po.Organization[] results = orgServiceI.query(searchOrganization, lo);
            NesId nesId = new NesId(results[0].getIdentifier());
            Organization organization = orgServiceI.getById(nesId.toId());
            orgServiceI.update(organization);
        }
    }

    private class HealthCareProviderRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            Id[] players = getPersonPlayerIds();
            healthCareProviderI.getByPlayerIds(players);
        }
    }

    private class OrganizationalContactRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            Id[] players = getPersonPlayerIds();
            organizationalContactI.getByPlayerIds(players);
        }
    }

    private class HealthCareFacilityRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            Id[] players = getOrganizationPlayerIds();
            healthCareFacilityI.getByPlayerIds(players);
        }
    }

    private class OversightCommitteeRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            Id[] players = getOrganizationPlayerIds();
            oversightCommitteeI.getByPlayerIds(players);
        }
    }

    private class ResearchOrganizationRunner extends ClientRunner {
        @Override
        protected void doRun() throws Exception {
            Id[] players = getOrganizationPlayerIds();
            researchOrganizationI.getByPlayerIds(players);
        }
    }
}
