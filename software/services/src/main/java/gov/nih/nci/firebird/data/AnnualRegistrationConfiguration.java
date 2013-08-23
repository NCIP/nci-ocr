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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.audit.Auditable;

/**
 * Configuration for new investigator annual registrations.
 */
@Entity
@Table(name = "annual_registration_configuration")
public class AnnualRegistrationConfiguration implements Auditable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private RegistrationFormSetConfiguration formSetConfiguration;
    private Date timestamp;
    private Organization sponsor;
    private String protocolText;
    private String subInvestigatorText;

    /**
     * Default Maximum Length of Section 7 text in the 1572 PDF form.
     */
    public static final int MAX_PROTOCOL_TEXT_FIELD_LENGTH = 450;

    /**
     * Default Maximum Length of Section 6 text in the 1572 PDF form.
     */
    public static final int MAX_SUB_INVESTIGATOR_TEXT_FIELD_LENGTH = 500;

    /**
     * Default Constructor.
     */
    public AnnualRegistrationConfiguration() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param configuration AnnualRegistrationConfiguration to copy from
     */
    public AnnualRegistrationConfiguration(AnnualRegistrationConfiguration configuration) {
        this.formSetConfiguration = new RegistrationFormSetConfiguration(configuration.getFormSetConfiguration());
        this.timestamp = new Date();
        this.sponsor = configuration.sponsor;
        this.subInvestigatorText = configuration.subInvestigatorText;
        this.protocolText = configuration.protocolText;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the investigatorConfiguration
     */
    @OneToOne
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name = "form_set_configuration_id")
    @ForeignKey(name = "annual_registration_configuration_form_set_configuration_fkey")
    public RegistrationFormSetConfiguration getFormSetConfiguration() {
        return formSetConfiguration;
    }

    /**
     * @param formSetConfiguration form set configuration
     */
    public void setFormSetConfiguration(RegistrationFormSetConfiguration formSetConfiguration) {
        this.formSetConfiguration = formSetConfiguration;
    }

    /**
     * @return the timestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the sponsor organization
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "sponsor_id")
    @ForeignKey(name = "annual_registration_sponsor_fkey")
    @NotNull
    public Organization getSponsor() {
        return sponsor;
    }

    /**
     * @param sponsor the sponsor to set
     */
    public void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    /**
      * @return Protocol Text field (Section 7)
      */
     @Column(name = "protocol_text")
     @Length(max = MAX_PROTOCOL_TEXT_FIELD_LENGTH)
     public String getProtocolText() {
         return protocolText;
     }

     /**
      * Represents Field 7 of the Form 1572.
      * @param protocolText text for Field 5 of the Form 1572
      */

     public void setProtocolText(String protocolText) {
         this.protocolText = protocolText;
     }

     /**
      * @return Sub-Investigator Text field (Section 6)
      */
     @Column(name = "sub_investigator_text")
     @Length(max = MAX_SUB_INVESTIGATOR_TEXT_FIELD_LENGTH)
     public String getSubInvestigatorText() {
         return subInvestigatorText;
     }

     /**
      * Represents Field 6 of the Form 1572.
      * @param subInvestigatorText text for Field 6 of the Form 1572
      */
     public void setSubInvestigatorText(String subInvestigatorText) {
         this.subInvestigatorText = subInvestigatorText;
     }
}
