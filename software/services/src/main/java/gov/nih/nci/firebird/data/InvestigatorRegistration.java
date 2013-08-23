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
package gov.nih.nci.firebird.data;

import static gov.nih.nci.firebird.data.RegistrationStatus.*;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.exception.ValidationException;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Investigator registration to a Protocol.
 */
@Entity
@DiscriminatorValue("PRIMARY")
@SuppressWarnings("PMD.TooManyMethods")
// Methods broken down for clarity
public class InvestigatorRegistration extends AbstractProtocolRegistration {

    private static final long serialVersionUID = 1L;
    static final String SUBINVESTIGATOR_NOT_INVITED_MESSAGE = "validation.failures.subinvestigator.not.invited";

    private Set<SubInvestigatorRegistration> subinvestigatorRegistrationsOriginal =
            new HashSet<SubInvestigatorRegistration>();
    private SortedSet<RevisedInvestigatorRegistration> revisedRegistrations =
            new TreeSet<RevisedInvestigatorRegistration>();

    @Override
    @Transient
    public Map<FormType, FormOptionality> getFormConfiguration() {
        return getProtocol().getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities();
    }

    @OneToMany(mappedBy = "primaryRegistration", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @SuppressWarnings("unused") // Required by hibernate
    private Set<SubInvestigatorRegistration> getSubinvestigatorRegistrationsOriginal() {
        return subinvestigatorRegistrationsOriginal;
    }

    @SuppressWarnings("unused") // Required by hibernate
    private void setSubinvestigatorRegistrationsOriginal(
            Set<SubInvestigatorRegistration> subinvestigatorRegistrationsOriginal) {
        this.subinvestigatorRegistrationsOriginal = subinvestigatorRegistrationsOriginal;
    }

    /**
     * @return the subinvestigator registrations
     */
    @Transient
    public Set<SubInvestigatorRegistration> getSubinvestigatorRegistrations() {
        return getSubinvestigatorRegistrationsOriginal();
    }

    /**
     * @return the subinvestigator Registrations
     */
    @Transient
    public Set<SubInvestigatorRegistration> getActiveSubinvestigatorRegistrations() {
        return Sets.filter(getSubinvestigatorRegistrations(), AbstractProtocolRegistration.IS_INVITED_PREDICATE);
    }

    /**
     * @return the subinvestigators
     */
    @Transient
    public Set<Person> getSubinvestigators() {
        return Sets.newHashSet(Iterables.transform(getSubinvestigatorRegistrations(),
                new Function<SubInvestigatorRegistration, Person>() {
                    @Override
                    public Person apply(SubInvestigatorRegistration registration) {
                        return registration.getProfile().getPerson();
                    }
                }));
    }

    /**
     * Adds a subinvestigator registration to this registration and sets its primary registration.
     *
     * @param registration the registration to add.
     */
    public void addSubinvestigatorRegistration(SubInvestigatorRegistration registration) {
        registration.setPrimaryRegistration(this);
        getSubinvestigatorRegistrations().add(registration);
    }

    @Override
    @Transient
    public InvestigatorRegistration getPrimaryRegistration() {
        return this;
    }

    /**
     * Indicates whether this registration and all related subinvestigator registrations have been accepted and the full
     * packet may now be approved / finalized.
     *
     * @return true if approvable.
     */
    @Transient
    public boolean isApprovable() {
        boolean approvable = ACCEPTED.equals(getStatus());
        for (SubInvestigatorRegistration subinvestigatorRegistration : getActiveSubinvestigatorRegistrations()) {
            approvable = approvable
                    && (ACCEPTED.equals(subinvestigatorRegistration.getStatus()) || APPROVED
                            .equals(subinvestigatorRegistration.getStatus()));
        }
        return approvable;
    }

    @Override
    public void validate(ResourceBundle resources) throws ValidationException {
        ValidationResult result = new ValidationResult();
        try {
            super.validate(resources);
        } catch (ValidationException e) {
            result = e.getResult();
        }
        checkForUninvitedSubInvestigators(resources, result);
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
    }

    /**
     * @param resources resource bundle with validation messages included
     * @param result ValidationResult object to store failures in
     * @return Validation results
     */
    public ValidationResult checkForUninvitedSubInvestigators(ResourceBundle resources, ValidationResult result) {
        for (Person subInvestigator : getUninvitedSubInvestigators()) {
            String messageTemplate = resources.getString(SUBINVESTIGATOR_NOT_INVITED_MESSAGE);
            ValidationFailure validationFailure = new ValidationFailure(MessageFormat.format(messageTemplate,
                    subInvestigator.getDisplayName()));
            result.addFailure(validationFailure);
        }
        return result;
    }

    /**
     * @return List of all uninvited sub investigators
     */
    @Transient
    public Set<Person> getUninvitedSubInvestigators() {
        Set<Person> unInvitedSubInvestigators = Sets.newHashSet();
        for (SubInvestigatorRegistration registration : getSubinvestigatorRegistrations()) {
            if (!registration.getInvitation().isInvited()) {
                unInvitedSubInvestigators.add(registration.getProfile().getPerson());
            }
        }
        return unInvitedSubInvestigators;
    }

    /**
     * @return the history of revised registrations
     */
    @OneToMany(mappedBy = "currentRegistrationRevision", fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Sort(type = SortType.NATURAL)
    public SortedSet<RevisedInvestigatorRegistration> getRevisedRegistrations() {
        return revisedRegistrations;
    }

    @SuppressWarnings("unused") // setter required by Hibernate
    private void setRevisedRegistrations(SortedSet<RevisedInvestigatorRegistration> revisedRegistrations) {
        this.revisedRegistrations = revisedRegistrations;
    }

    /**
     * Creates a new RevisedRegistrationRegistration based on this registration allowing for revision
     * of an approved registration.
     *
     * @return the new revised registration.
     */
    public RevisedInvestigatorRegistration createRevisedRegistration() {
        Preconditions.checkArgument(RegistrationStatus.APPROVED.equals(getStatus()),
                "Can only create revised registration for an APPROVED registration, status was " + getStatus());
        RevisedInvestigatorRegistration revisedRegistration = new RevisedInvestigatorRegistration();
        revisedRegistration.copyFrom(this);
        revisedRegistration.setCurrentRegistration(this);
        revisedRegistrations.add(revisedRegistration);
        getProfile().addRegistration(revisedRegistration);
        return revisedRegistration;
    }

    boolean isReferencedInRevision(SubInvestigatorRegistration subinvestigatorRegistration) {
        for (RevisedInvestigatorRegistration revisedRegistration : getRevisedRegistrations()) {
            if (revisedRegistration.getSubinvestigatorRegistrations().contains(subinvestigatorRegistration)) {
                return true;
            }
        }
        return false;
    }

}
