/*
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
package gov.nih.nci.firebird.data;

import gov.nih.nci.firebird.common.ValidationResult;

import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * FDA for 1572.
 */
@SuppressWarnings({ "unused", "PMD.AvoidDuplicateLiterals" })
// private setters form hibernate
// column name form1572_id used to many times.
@Entity
@Table(name = "form_1572")
public class ProtocolForm1572 extends AbstractProtocolRegistrationForm implements Form1572 {

    private static final long serialVersionUID = 1L;

    private SortedSet<Organization> practiceSites = new TreeSet<Organization>();
    private SortedSet<Organization> labs = new TreeSet<Organization>();
    private SortedSet<Organization> irbs = new TreeSet<Organization>();
    private SubmittedForm1572Data submittedData;

    private ProtocolForm1572Validator validator;

    /**
     * No-argument constructor for Hibernate / Struts.
     */
    public ProtocolForm1572() {
        super();
        this.validator = new ProtocolForm1572Validator(this);
    }

    ProtocolForm1572(AbstractProtocolRegistration registration, FormType formType) {
        super(registration, formType);
        this.validator = new ProtocolForm1572Validator(this);
    }

    /**
     * Section 7.
     *
     * @return the registration to the protocol
     */
    @Override
    @OneToOne(mappedBy = "form1572", optional = false)
    public AbstractProtocolRegistration getRegistration() {
        return super.getRegistration();
    }

    /**
     * Section 5.
     *
     * @return Institutional Review Boards
     */
    @Override
    @ManyToMany
    @JoinTable(name = "form1572_irbs", joinColumns = @JoinColumn(name = "form1572_id"),
            inverseJoinColumns = @JoinColumn(name = "irb_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "form1572_id", "irb_id" }))
    @ForeignKey(name = "form1572_fkey", inverseName = "form1572_irb_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @Sort(type = SortType.NATURAL)
    public SortedSet<Organization> getIrbs() {
        return irbs;
    }

    private void setIrbs(SortedSet<Organization> irbs) {
        this.irbs = irbs;
    }

    @Override
    @Transient
    public boolean isAdditionalDocumentsUploaded() {
        return !getClinicalLabCertificates().isEmpty();
    }

    @Override
    @Transient
    public int getNumberOfAdditionalDocuments() {
        return getClinicalLabCertificates().size();
    }

    @Override
    @Transient
    public List<LaboratoryCertificate> getClinicalLabCertificates() {
        List<LaboratoryCertificate> clinicalLabCertificates = Lists.newArrayList();

        SortedSet<Organization> organizations = getLabs();
        for (Organization organization : organizations) {
            ClinicalLaboratory clinicalLab = (ClinicalLaboratory) organization
                    .getRole(OrganizationRoleType.CLINICAL_LABORATORY);
            clinicalLabCertificates.addAll(clinicalLab.getCertificates().values());
        }
        return clinicalLabCertificates;
    }

    /**
     * Section 4.
     *
     * @return Labs.
     */
    @Override
    @ManyToMany
    @JoinTable(name = "form1572_labs", joinColumns = @JoinColumn(name = "form1572_id"),
            inverseJoinColumns = @JoinColumn(name = "lab_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "form1572_id", "lab_id" }))
    @ForeignKey(name = "form1572_fkey", inverseName = "form1572_lab_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @Sort(type = SortType.NATURAL)
    public SortedSet<Organization> getLabs() {
        return labs;
    }

    private void setLabs(SortedSet<Organization> labs) {
        this.labs = labs;
    }

    /**
     * Section 3.
     *
     * @return practice site
     */
    @Override
    @ManyToMany
    @JoinTable(name = "form1572_practicesites", joinColumns = @JoinColumn(name = "form1572_id"),
            inverseJoinColumns = @JoinColumn(name = "practice_site_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {
                    "form1572_id", "practice_site_id" }))
    @ForeignKey(name = "form1572_fkey", inverseName = "form1572_practicesite_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @Sort(type = SortType.NATURAL)
    public SortedSet<Organization> getPracticeSites() {
        return practiceSites;
    }

    private void setPracticeSites(SortedSet<Organization> practiceSites) {
        this.practiceSites = practiceSites;
    }

    /**
     * @return the submittedData
     */
    @OneToOne
    @JoinColumn(name = "submitted_form_1572_data_id")
    @ForeignKey(name = "form_1572_submitted_data_fkey")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public SubmittedForm1572Data getSubmittedData() {
        return submittedData;
    }

    private void setSubmittedData(SubmittedForm1572Data submittedData) {
        this.submittedData = submittedData;
    }

    @Override
    @Transient
    public Set<Organization> getOrganizations() {
        Set<Organization> organizations = super.getOrganizations();
        organizations.addAll(getAssociatedOrganizations());
        return organizations;
    }

    @Transient
    private Set<Organization> getAssociatedOrganizations() {
        Set<Organization> organizations = Sets.newHashSet();
        organizations.addAll(getPracticeSites());
        organizations.addAll(getLabs());
        organizations.addAll(getIrbs());
        return organizations;
    }

    @Override
    @Transient
    public Set<Person> getPersons() {
        Set<Person> persons = new HashSet<Person>();
        persons.add(getInvestigator());
        if (getRegistration() instanceof InvestigatorRegistration) {
            InvestigatorRegistration registration = (InvestigatorRegistration) getRegistration();
            for (SubInvestigatorRegistration subRegistration : registration.getSubinvestigatorRegistrations()) {
                persons.add(subRegistration.getProfile().getPerson());
            }
        }
        return persons;
    }

    @Override
    public void validate(ValidationResult result, ResourceBundle resources) {
        super.validate(result, resources);
        validator.validate(result, resources);
    }

    /**
     * @param validator the validator to set
     */
    @SuppressWarnings("ucd")
    // used to inject mock validator from tests
    void setValidator(ProtocolForm1572Validator validator) {
        this.validator = validator;
    }

    /**
     * Returns whether or not the given organization is selected as a practice site, clinical laboratory, or IRB.
     *
     * @param organization organization
     * @return true if the organization is selected, otherwise false.
     */
    @Transient
    public boolean isOrganizationSelected(Organization organization) {
        return getAssociatedOrganizations().contains(organization);
    }

    @Override
    @Transient
    public Set<Organization> getAssociatedOrganizations(OrganizationRoleType type) {
        switch (type) {
            case CLINICAL_LABORATORY:
                return getLabs();
            case IRB:
                return getIrbs();
            case PRACTICE_SITE:
                return getPracticeSites();
            default:
                return Sets.newHashSet();
        }
    }

    @Override
    void copyTo(AbstractProtocolRegistration registration) {
        ProtocolForm1572 copy = new ProtocolForm1572(registration, getFormType());
        registration.setForm1572(copy);
        copyCommonData(copy);
        copy.getIrbs().addAll(getIrbs());
        copy.getLabs().addAll(getLabs());
        copy.getPracticeSites().addAll(getPracticeSites());
    }

    @Override
    public void submitForm() {
        super.submitForm();
        setSubmittedData(new SubmittedForm1572Data());
        InvestigatorProfile profile = getRegistration().getProfile();
        submittedData.setPersonSnapshot(profile.getPerson().createSnapshot());
        submittedData.setPrimaryOrganizationSnapshot(
                profile.getPrimaryOrganization().getOrganization().createSnapshot());
        addSubmittedPracticeSiteSnapshots();
        addSubmittedLabSnapshots();
        addSubmittedIrbSnapshots();
    }

    private void addSubmittedPracticeSiteSnapshots() {
        for (Organization siteOrganization : getPracticeSites()) {
            PracticeSite site = (PracticeSite) siteOrganization.getRole(OrganizationRoleType.PRACTICE_SITE);
            submittedData.getPracticeSiteSnapshots().add(site.createSnapshot());
        }
    }

    private void addSubmittedLabSnapshots() {
        for (Organization labOrganization : getLabs()) {
            ClinicalLaboratory lab =
                    (ClinicalLaboratory) labOrganization.getRole(OrganizationRoleType.CLINICAL_LABORATORY);
            submittedData.getLabSnapshots().add(lab.createSnapshot());
        }
    }

    private void addSubmittedIrbSnapshots() {
        for (Organization irbOrganization : getIrbs()) {
            InstitutionalReviewBoard irb = (InstitutionalReviewBoard) irbOrganization.getRole(OrganizationRoleType.IRB);
            submittedData.getIrbSnapshots().add(irb.createSnapshot());
        }
    }

    @Override
    Set<PersistentObject> returnForm() {
        Set<PersistentObject> objectsToDelete = super.returnForm();
        if (getSubmittedData() != null) {
            objectsToDelete.add(getSubmittedData());
        }
        setSubmittedData(null);
        new Form1572AssociationCleaner(this).cleanAssociations();
        return objectsToDelete;
    }

}
