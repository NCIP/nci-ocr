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

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Represents a form that may be included in a registration.
 */
@Entity(name = "form_type")
public class FormType extends AbstractListItem {

    private static final long serialVersionUID = 1L;

    private static final int DESCRIPTION_LENGTH = 1024;

    private String description;
    private FormOptionality investigatorDefault = FormOptionality.NONE;
    private FormOptionality subinvestigatorDefault = FormOptionality.NONE;
    private FirebirdFile template;
    private FirebirdFile sample;
    private String signingField;
    private FormTypeEnum formTypeEnum;
    private Set<FormOptionality> allowableInvestigatorFormOptionalities;
    private Set<FormOptionality> allowableSubInvestigatorFormOptionalities;
    private Set<RegistrationType> registrationTypes;

    /**
     * default constructor.
     */
    public FormType() {
        super();
    }

    /**
     * Creates a new FormType.
     *
     * @param name the name of the form type
     */
    @SuppressWarnings("ucd")
    // convenience constructor used in tests
    public FormType(String name) {
        super(name);
        description = name;
    }

    /**
     * @return the description
     */
    @Column
    @Length(max = DESCRIPTION_LENGTH)
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the standard
     */
    @Transient
    public boolean isStandard() {
        return getFormTypeEnum() != null;
    }

    /**
     * @return the template
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @ForeignKey(name = "form_type_template_fk")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public FirebirdFile getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(FirebirdFile template) {
        this.template = template;
    }

    /**
     * @return the sample file
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    @ForeignKey(name = "form_type_sample_fk")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public FirebirdFile getSample() {
        return sample;
    }

    /**
     * @param sample the sample file to set
     */
    public void setSample(FirebirdFile sample) {
        this.sample = sample;
    }

    /**
     * @param investigatorDefault the investigatorDefault to set
     */
    public void setInvestigatorDefault(FormOptionality investigatorDefault) {
        this.investigatorDefault = investigatorDefault;
    }

    /**
     * @return the investigatorDefault
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "investigator_default", length = FormOptionality.MAX_LENGTH)
    @NotNull
    public FormOptionality getInvestigatorDefault() {
        return investigatorDefault;
    }

    /**
     * @param subinvestigatorDefault the subinvestigatorDefault to set
     */
    public void setSubinvestigatorDefault(FormOptionality subinvestigatorDefault) {
        this.subinvestigatorDefault = subinvestigatorDefault;
    }

    /**
     * @return the subinvestigatorDefault
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "subinvestigator_default", length = FormOptionality.MAX_LENGTH)
    @NotNull
    public FormOptionality getSubinvestigatorDefault() {
        return subinvestigatorDefault;
    }

    /**
     * Read only field - should only be called in tests.
     *
     * @param formTypeEnum the formTypeEnum to set
     */
    public void setFormTypeEnum(FormTypeEnum formTypeEnum) {
        this.formTypeEnum = formTypeEnum;
    }

    /**
     * @return the formTypeEnum if there is one (i.e. if it's a standard form)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "form_type_enum", length = FormTypeEnum.MAXIMUM_NAME_LENGTH, unique = true)
    public FormTypeEnum getFormTypeEnum() {
        return formTypeEnum;
    }

    /**
     * @param signingField the signingField to set
     */
    public void setSigningField(String signingField) {
        this.signingField = signingField;
    }

    /**
     * @return the signingField
     */
    @Column(name = "signing_field")
    public String getSigningField() {
        return signingField;
    }

    /**
     * @return the allowableInvestigatorFormOptionalities
     */
    @CollectionOfElements(targetElement = FormOptionality.class)
    @JoinTable(name = "allowable_investigator_form_optionalities", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @ForeignKey(name = "allowable_investigator_form_optionalities_fkey")
    public Set<FormOptionality> getAllowableInvestigatorFormOptionalities() {
        return allowableInvestigatorFormOptionalities;
    }

    /**
     * @param allowableInvestigatorFormOptionalities the allowableInvestigatorFormOptionalities to set
     */
    @SuppressWarnings("unused")
    // setter required for Hibernate
    private void setAllowableInvestigatorFormOptionalities(
            Set<FormOptionality> allowableInvestigatorFormOptionalities) {
        this.allowableInvestigatorFormOptionalities = allowableInvestigatorFormOptionalities;
    }

    /**
     * @return the allowableSubInvestigatorFormOptionalities
     */
    @CollectionOfElements(targetElement = FormOptionality.class)
    @JoinTable(name = "allowable_sub_investigator_form_optionalities", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @ForeignKey(name = "allowable_sub_investigator_form_optionalities_fkey")
    public Set<FormOptionality> getAllowableSubInvestigatorFormOptionalities() {
        return allowableSubInvestigatorFormOptionalities;
    }

    /**
     * @param allowableSubInvestigatorFormOptionalities the allowableSubInvestigatorFormOptionalities to set
     */
    @SuppressWarnings("unused")
    // setter required for Hibernate
    private void setAllowableSubInvestigatorFormOptionalities(
            Set<FormOptionality> allowableSubInvestigatorFormOptionalities) {
        this.allowableSubInvestigatorFormOptionalities = allowableSubInvestigatorFormOptionalities;
    }

    /**
     * @return the allowableInvestigatorFormOptionalities
     */
    @CollectionOfElements(targetElement = RegistrationType.class)
    @JoinTable(name = "form_type_registration_types", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @ForeignKey(name = "form_type_registration_types_form_type_fkey")
    public Set<RegistrationType> getRegistrationTypes() {
        return registrationTypes;
    }

    /**
     * @param registrationTypes the registrationTypes to set
     */
    public void setRegistrationTypes(Set<RegistrationType> registrationTypes) {
        this.registrationTypes = registrationTypes;
    }

    @Override
    public int compareTo(ListItem item) {
        if (item instanceof FormType) {
            return this.getFormTypeEnum().compareTo(((FormType) item).getFormTypeEnum());
        } else {
            return super.compareTo(item);
        }
    }

}
