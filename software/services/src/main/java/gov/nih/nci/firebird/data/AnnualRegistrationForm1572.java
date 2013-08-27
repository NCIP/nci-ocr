/*
 * *
 *  * The software subject to this notice and license includes both human readable
 *  * source code form and machine readable, binary, object code form. The NCI OCR
 *  * Software was developed in conjunction with the National Cancer Institute
 *  * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 *  * government employees are authors, any rights in such works shall be subject
 *  * to Title 17 of the United States Code, section 105.
 *  *
 *  * This NCI OCR Software License (the License) is between NCI and You. You (or
 *  * Your) shall mean a person or an entity, and all other entities that control,
 *  * are controlled by, or are under common control with the entity. Control for
 *  * purposes of this definition means (i) the direct or indirect power to cause
 *  * the direction or management of such entity, whether by contract or otherwise,
 *  * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 *  * or (iii) beneficial ownership of such entity.
 *  *
 *  * This License is granted provided that You agree to the conditions described
 *  * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 *  * no-charge, irrevocable, transferable and royalty-free right and license in
 *  * its rights in the NCI OCR Software to (i) use, install, access, operate,
 *  * execute, copy, modify, translate, market, publicly display, publicly perform,
 *  * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 *  * have distributed to and by third parties the NCI OCR Software and any
 *  * modifications and derivative works thereof; and (iii) sublicense the
 *  * foregoing rights set out in (i) and (ii) to third parties, including the
 *  * right to license such rights to further third parties. For sake of clarity,
 *  * and not by way of limitation, NCI shall have no right of accounting or right
 *  * of payment from You or Your sub-licensees for the rights granted under this
 *  * License. This License is granted at no charge to You.
 *  *
 *  * Your redistributions of the source code for the Software must retain the
 *  * above copyright notice, this list of conditions and the disclaimer and
 *  * limitation of liability of Article 6, below. Your redistributions in object
 *  * code form must reproduce the above copyright notice, this list of conditions
 *  * and the disclaimer of Article 6 in the documentation and/or other materials
 *  * provided with the distribution, if any.
 *  *
 *  * Your end-user documentation included with the redistribution, if any, must
 *  * include the following acknowledgment: This product includes software
 *  * developed by 5AM and the National Cancer Institute. If You do not include
 *  * such end-user documentation, You shall include this acknowledgment in the
 *  * Software itself, wherever such third-party acknowledgments normally appear.
 *  *
 *  * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 *  * to endorse or promote products derived from this Software. This License does
 *  * not authorize You to use any trademarks, service marks, trade names, logos or
 *  * product names of either NCI or 5AM, except as required to comply with the
 *  * terms of this License.
 *  *
 *  * For sake of clarity, and not by way of limitation, You may incorporate this
 *  * Software into Your proprietary programs and into any third party proprietary
 *  * programs. However, if You incorporate the Software into third party
 *  * proprietary programs, You agree that You are solely responsible for obtaining
 *  * any permission from such third parties required to incorporate the Software
 *  * into such third party proprietary programs and for informing Your
 *  * sub-licensees, including without limitation Your end-users, of their
 *  * obligation to secure any required permissions from such third parties before
 *  * incorporating the Software into such third party proprietary software
 *  * programs. In the event that You fail to obtain such permissions, You agree
 *  * to indemnify NCI for any claims against NCI by such third parties, except to
 *  * the extent prohibited by law, resulting from Your failure to obtain such
 *  * permissions.
 *  *
 *  * For sake of clarity, and not by way of limitation, You may add Your own
 *  * copyright statement to Your modifications and to the derivative works, and
 *  * You may provide additional or different license terms and conditions in Your
 *  * sublicenses of modifications of the Software, or any derivative works of the
 *  * Software as a whole, provided Your use, reproduction, and distribution of the
 *  * Work otherwise complies with the conditions stated in this License.
 *  *
 *  * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 *  * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 *  * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 *  * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 *  * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 *  * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 *  * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.nih.nci.firebird.data;

import static com.google.common.base.Preconditions.checkArgument;
import gov.nih.nci.firebird.common.ValidationResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
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
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * CTEP's annual registration Form 1572.
 */
@SuppressWarnings({ "unused", "PMD.AvoidDuplicateLiterals" })
// private setters form hibernate
// column name form1572_id used to many times.
@Entity
@Table(name = "ctep_form_1572")
public class AnnualRegistrationForm1572 extends AbstractAnnualRegistrationForm implements Form1572 {

    private static final long serialVersionUID = 1L;
    private SortedSet<Organization> practiceSites = new TreeSet<Organization>();
    private SortedSet<Organization> labs = new TreeSet<Organization>();
    private SortedSet<Organization> irbs = new TreeSet<Organization>();
    private boolean phaseOne = true;
    private boolean phaseTwoOrThree = true;
    private AnnualForm1572Validator validator;

    /**
     * No-argument constructor for Hibernate / Struts.
     */
    public AnnualRegistrationForm1572() {
        super();
        this.validator = new AnnualForm1572Validator(this);
    }

    AnnualRegistrationForm1572(AnnualRegistration registration, FormType formType) {
        super(registration, formType);
        this.validator = new AnnualForm1572Validator(this);
    }

    @Override
    @ManyToMany
    @JoinTable(name = "ctepform1572_irbs", joinColumns = @JoinColumn(name = "ctepform1572_id"),
            inverseJoinColumns = @JoinColumn(name = "irb_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "ctepform1572_id", "irb_id" }))
    @ForeignKey(name = "ctepform1572_fkey", inverseName = "ctepform1572_irb_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @Sort(type = SortType.NATURAL)
    public SortedSet<Organization> getIrbs() {
        return irbs;
    }

    private void setIrbs(SortedSet<Organization> irbs) {
        this.irbs = irbs;
    }

    /**
     * Section 4.
     *
     * @return Labs.
     */
    @Override
    @ManyToMany
    @JoinTable(name = "ctepform1572_labs", joinColumns = @JoinColumn(name = "ctepform1572_id"),
            inverseJoinColumns = @JoinColumn(name = "lab_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "ctepform1572_id", "lab_id" }))
    @ForeignKey(name = "ctepform1572_fkey", inverseName = "ctepform1572_lab_fkey")
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
    @JoinTable(name = "ctepform1572_practicesites", joinColumns = @JoinColumn(name = "ctepform1572_id"),
            inverseJoinColumns = @JoinColumn(name = "practice_site_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {
                    "ctepform1572_id", "practice_site_id" }))
    @ForeignKey(name = "ctepform1572_fkey", inverseName = "ctepform1572_practicesite_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH })
    @Sort(type = SortType.NATURAL)
    public SortedSet<Organization> getPracticeSites() {
        return practiceSites;
    }

    private void setPracticeSites(SortedSet<Organization> practiceSites) {
        this.practiceSites = practiceSites;
    }

    @Override
    @Transient
    public Set<Person> getPersons() {
        return Collections.singleton(getInvestigator());
    }

    @Override
    @OneToOne(mappedBy = "form1572", optional = false)
    public AnnualRegistration getRegistration() {
        return super.getRegistration();
    }

    @Override
    protected void validate(ValidationResult result, ResourceBundle resources) {
        super.validate(result, resources);
        validator.validate(result, resources);
        checkPhaseSelection(result, resources);
    }

    private void checkPhaseSelection(ValidationResult result, ResourceBundle resources) {
        if (!isPhaseOne() && !isPhaseTwoOrThree()) {
            result.addFailure(createValidationFailure(resources, "validation.failure.missing.phase"));
        }
    }

    /**
     * @param validator the validator to set
     */
    void setValidator(AnnualForm1572Validator validator) {
        this.validator = validator;
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

    /**
     * @return the phaseOne
     */
    @Column(name = "phase_one", nullable = false)
    public boolean isPhaseOne() {
        return phaseOne;
    }

    /**
     * @param phaseOne the phaseOne to set
     */
    public void setPhaseOne(boolean phaseOne) {
        this.phaseOne = phaseOne;
    }

    /**
     * @return the phaseTwoOrThree
     */
    @Column(name = "phase_two_or_three", nullable = false)
    public boolean isPhaseTwoOrThree() {
        return phaseTwoOrThree;
    }

    /**
     * @param phaseTwoOrThree the phaseTwoOrThree to set
     */
    public void setPhaseTwoOrThree(boolean phaseTwoOrThree) {
        this.phaseTwoOrThree = phaseTwoOrThree;
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

    @Override
    @Transient
    public int getNumberOfAdditionalDocuments() {
        return getClinicalLabCertificates().size();
    }

    @Override
    @Transient
    public boolean isAdditionalDocumentsUploaded() {
        return !getClinicalLabCertificates().isEmpty();
    }

    @Override
    public void copyForm(AbstractRegistrationForm form) {
        checkArgument(form instanceof AnnualRegistrationForm1572,
                "Passed in form to copy wasn't an instance of AnnualRegistrationForm1572");
        AnnualRegistrationForm1572 formToCopy = (AnnualRegistrationForm1572) form;
        getIrbs().addAll(geteAssociatedOrganizationsThatAreStillInProfile(formToCopy, OrganizationRoleType.IRB));
        getLabs().addAll(
                geteAssociatedOrganizationsThatAreStillInProfile(formToCopy, OrganizationRoleType.CLINICAL_LABORATORY));
        getPracticeSites().addAll(
                geteAssociatedOrganizationsThatAreStillInProfile(formToCopy, OrganizationRoleType.PRACTICE_SITE));
        setPhaseOne(formToCopy.isPhaseOne());
        setPhaseTwoOrThree(formToCopy.isPhaseTwoOrThree());
    }

    @Transient
    private Collection<Organization> geteAssociatedOrganizationsThatAreStillInProfile(
            AnnualRegistrationForm1572 form1572, OrganizationRoleType organizationRoleType) {
        Set<Organization> profileAssociatedOrganizations = getProfileAssociatedOrganizations(organizationRoleType);
        Set<Organization> formAssociatedOrganizations = form1572.getAssociatedOrganizations(organizationRoleType);
        return Sets.intersection(formAssociatedOrganizations, profileAssociatedOrganizations);
    }

    @Transient
    private Set<Organization> getProfileAssociatedOrganizations(OrganizationRoleType organizationRoleType) {
        Set<OrganizationAssociation> profileOrganizationAssociations = getRegistration().getProfile()
                .getOrganizationAssociations(organizationRoleType);
        return Sets.newHashSet(Iterables.transform(profileOrganizationAssociations,
                new Function<OrganizationAssociation, Organization>() {
                    @Override
                    public Organization apply(OrganizationAssociation association) {
                        return association.getOrganizationRole().getOrganization();
                    }
                }));
    }
    
    @Override
    Set<PersistentObject> returnForm() {
        new Form1572AssociationCleaner(this).cleanAssociations();
        return super.returnForm();
    }

}
