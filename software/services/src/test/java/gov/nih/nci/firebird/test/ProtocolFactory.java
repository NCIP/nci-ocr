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

import static gov.nih.nci.firebird.test.ValueGenerator.*;
import static gov.nih.nci.firebird.test.util.ComparisonUtil.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolModification;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.data.ProtocolRegistrationConfiguration;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.RegistrationFormSetConfiguration;
import gov.nih.nci.firebird.test.util.ComparisonUtil;

import java.util.Iterator;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;

/**
 * Creates test file instances.
 */
public final class ProtocolFactory {

    private static final ProtocolFactory NO_ID_INSTANCE = new ProtocolFactory(false);
    private static final ProtocolFactory ID_INSTANCE = new ProtocolFactory(true);
    private OrganizationFactory organizationFactory;
    private boolean withId;

    public static ProtocolFactory getInstance() {
        return NO_ID_INSTANCE;
    }

    public static ProtocolFactory getInstanceWithId() {
        return ID_INSTANCE;
    }

    private ProtocolFactory(boolean withId) {
        this.withId = withId;
        if (withId) {
            organizationFactory = OrganizationFactory.getInstanceWithId();
        } else {
            organizationFactory = OrganizationFactory.getInstance();
        }
    }

    public Protocol createNoAgents() {
        Protocol protocol = new Protocol();
        protocol.setId(getId());
        populateValues(protocol);
        return protocol;
    }

    public Protocol create() {
        Protocol protocol = createNoAgents();
        protocol.getAgents().add(new ProtocolAgent(getUniqueString()));
        protocol.getAgents().add(new ProtocolAgent(getUniqueString()));
        return protocol;
    }

    private Long getId() {
        if (withId) {
            return ValueGenerator.getUniqueLong();
        }
        return null;
    }

    public Protocol create(Organization sponsor) {
        Protocol protocol = create();
        protocol.setSponsor(sponsor);
        return protocol;
    }

    public Protocol createWithForms() {
        Protocol protocol = create();
        setUpFormOptionalities(protocol);
        return protocol;
    }

    public Protocol createWithFormsDocuments() {
        Protocol protocol = create();
        setUpFormOptionalitiesWithDocuments(protocol);
        setupProtocolDocuments(protocol);
        return protocol;
    }

    private void setUpFormOptionalities(Protocol protocol) {
        FormTypeFactory formTypeFactory;
        if (withId) {
            formTypeFactory = FormTypeFactory.getInstanceWithId();
        } else {
            formTypeFactory = FormTypeFactory.getInstance();
        }
        FormType form1572 = formTypeFactory.create(FormTypeEnum.FORM_1572);
        FormType financialDisclosure = formTypeFactory.create(FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
        FormType cv = formTypeFactory.create(FormTypeEnum.CV);
        FormType humanResearchCertificate = formTypeFactory.create(FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE);
        FormType additionalDocs = formTypeFactory.create(FormTypeEnum.ADDITIONAL_ATTACHMENTS);
        RegistrationFormSetConfiguration investigatorConfiguration =
                protocol.getRegistrationConfiguration().getInvestigatorConfiguration();
        investigatorConfiguration.setOptionality(form1572, FormOptionality.REQUIRED);
        investigatorConfiguration.setOptionality(financialDisclosure, FormOptionality.REQUIRED);
        investigatorConfiguration.setOptionality(cv, FormOptionality.REQUIRED);
        investigatorConfiguration.setOptionality(humanResearchCertificate, FormOptionality.REQUIRED);
        investigatorConfiguration.setOptionality(additionalDocs, FormOptionality.SUPPLEMENTARY);

        RegistrationFormSetConfiguration subinvestigatorConfiguration =
                protocol.getRegistrationConfiguration().getSubinvestigatorConfiguration();
        subinvestigatorConfiguration.setOptionality(form1572, FormOptionality.NONE);
        subinvestigatorConfiguration.setOptionality(financialDisclosure, FormOptionality.REQUIRED);
        subinvestigatorConfiguration.setOptionality(cv, FormOptionality.REQUIRED);
        subinvestigatorConfiguration.setOptionality(humanResearchCertificate, FormOptionality.REQUIRED);
        subinvestigatorConfiguration.setOptionality(additionalDocs, FormOptionality.SUPPLEMENTARY);
    }

    private void setUpFormOptionalitiesWithDocuments(Protocol protocol) {
        setUpFormOptionalities(protocol);

        for (FormType type : protocol.getRegistrationConfiguration().getAssociatedFormTypes()) {
            switch (type.getFormTypeEnum()) {
            case FORM_1572:
                type.setSigningField("signature_field");
                type.setTemplate(FirebirdFileFactory.getInstance().create("/files/form_1572.pdf.gz"));
                break;
            case FINANCIAL_DISCLOSURE_FORM:
                type.setTemplate(FirebirdFileFactory.getInstance().create("/files/dcp_financial_disclosure_form.pdf.gz"));
                break;
            }
        }
    }

    private void setupProtocolDocuments(Protocol protocol) {
        protocol.getDocuments().add(FirebirdFileFactory.getInstance().create());
        protocol.getDocuments().add(FirebirdFileFactory.getInstance().create());
    }

    public void clearForms(Protocol protocol) {
        for (FormType formType : protocol.getRegistrationConfiguration().getAssociatedFormTypes()) {
            protocol.getRegistrationConfiguration().setInvestigatorOptionality(formType,
                    FormOptionality.NONE);
            protocol.getRegistrationConfiguration().setSubinvestigatorOptionality(formType,
                    FormOptionality.NONE);
        }
    }

    public void populateValues(Protocol protocol) {
        protocol.setPhase(ProtocolPhase.NOT_APPLICABLE);
        protocol.setProtocolNumber(getUniqueString());
        protocol.setProtocolTitle(getUniqueString());
        protocol.setSponsor(organizationFactory.create());
        protocol.addLeadOrganization(organizationFactory.create(), PersonFactory.getInstance().create());
    }

    public void compare(Protocol protocol1, Protocol protocol2) {
        assertEquals(protocol1.getId(), protocol2.getId());
        assertEquals(protocol1.getPhase(), protocol2.getPhase());
        assertEquals(protocol1.getProtocolNumber(), protocol2.getProtocolNumber());
        assertEquals(protocol1.getProtocolTitle(), protocol2.getProtocolTitle());
        assertSamePersistentObjects(protocol1.getSponsor(), protocol2.getSponsor());
        checkAgents(protocol1, protocol2);
        checkIterators(protocol1.getCurrentInvestigatorRegistrations().iterator(), protocol2.getCurrentInvestigatorRegistrations()
                .iterator());
        assertEquals(protocol1.getDocuments().size(), protocol2.getDocuments().size());
        checkOptionalities(protocol1.getRegistrationConfiguration(), protocol2.getRegistrationConfiguration());
    }

    public ProtocolRevision addProtocolRevision(Protocol protocol) {
        ProtocolRevision revision = new ProtocolRevision();
        revision.setId(getId());
        revision.setDate(getUniqueDate());
        for (int i = 0; i < 3; i++) {
            ProtocolModification modification = new ProtocolModification(getUniqueString(), getUniqueString());
            revision.addModification(modification);
        }
        revision.setComment(getUniqueString());
        protocol.addRevision(revision);
        return revision;
    }

    private void checkAgents(Protocol protocol1, Protocol protocol2) {
        assertEquals(protocol1.getAgents().size(), protocol2.getAgents().size());
        Iterator<ProtocolAgent> iterator1 = protocol1.getAgents().iterator();
        Iterator<ProtocolAgent> iterator2 = protocol2.getAgents().iterator();
        while (iterator1.hasNext()) {
            ProtocolAgent agent1 = iterator1.next();
            ProtocolAgent agent2 = iterator2.next();
            assertEquals(agent1.getId(), agent2.getId());
            assertEquals(agent1.getName(), agent2.getName());
        }
    }

    private void checkIterators(Iterator<? extends PersistentObject> iterator1,
            Iterator<? extends PersistentObject> iterator2) {
        while (iterator1.hasNext()) {
            PersistentObject o1 = iterator1.next();
            PersistentObject o2 = iterator2.next();
            ComparisonUtil.assertSamePersistentObjects(o1, o2);
        }
        assertFalse(iterator2.hasNext());
    }

    private void checkOptionalities(ProtocolRegistrationConfiguration configuration1,
            ProtocolRegistrationConfiguration configuration2) {
        assertEquals(configuration1.getAssociatedFormTypes().size(), configuration2.getAssociatedFormTypes().size());
        for (FormType type : configuration1.getAssociatedFormTypes()) {
            assertEquals(configuration1.getInvestigatorOptionality(type),
                    configuration2.getInvestigatorOptionality(type));
            assertEquals(configuration1.getSubinvestigatorOptionality(type),
                    configuration2.getSubinvestigatorOptionality(type));
        }
    }

}
