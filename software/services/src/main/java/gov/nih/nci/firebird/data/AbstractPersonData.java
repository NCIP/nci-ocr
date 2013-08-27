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

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.fiveamsolutions.nci.commons.search.Searchable;

/**
 * Base class that contains person field data.
 */
@MappedSuperclass
public abstract class AbstractPersonData implements PersistentObject {

    private static final long serialVersionUID = 1L;
    private static final int SHORT_COL_LENGTH = 10;
    private static final int LONG_COL_LENGTH = 50;

    private Long id;
    private String nesId;
    private String ctepId;
    private CurationStatus nesStatus = CurationStatus.PRE_NES_VALIDATION;
    private Date updateRequested;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String prefix;
    private String email;
    private String phoneNumber;
    private Address postalAddress = new Address();
    private Date lastNesRefresh;
    private String providerNumber;

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
     * Get the display name.
     * @return the display name.
     */
    @Transient
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        appendField(getPrefix(), sb);
        appendField(getFirstName(), sb);
        appendField(getMiddleName(), sb);
        appendField(getLastName(), sb);
        appendField(getSuffix(), sb);
        return sb.toString();
    }

    /**
     * Get the display name with last name first, etc. for use in sortable lists.
     * @return the display name.
     */
    @Transient
    public String getDisplayNameForList() {
        return getNameInLastNameFirstFormat(true);
    }

    private String getNameInLastNameFirstFormat(boolean forDisplay) {
        StringBuilder sb = new StringBuilder();
        appendField(getLastName(), sb);
        sb.append(",");
        if (forDisplay) {
            appendField(getPrefix(), sb);
        }
        appendField(getFirstName(), sb);
        appendField(getMiddleName(), sb);
        appendField(getSuffix(), sb, ", ");
        return sb.toString();
    }

    /**
     * Get the name to use for sorting by last name, etc. leaving out the prefix.
     * @return the name to use for sorts.
     */
    @Transient
    public String getSortableName() {
        return getNameInLastNameFirstFormat(false);
    }

    private void appendField(String field, StringBuilder sb) {
        appendField(field, sb, " ");
    }

    private void appendField(String field, StringBuilder sb, String prepend) {
        if (StringUtils.isNotBlank(field)) {
            if (sb.length() > 0) {
                sb.append(prepend);
            }
            sb.append(field);
        }
    }

    /**
     * @return the nesId
     */
    @NotEmpty
    @Column(name = "nes_id", unique = true)
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT, caseSensitive = true)
    @Index(name = "person_nes_id_index")
    public String getNesId() {
        return nesId;
    }

    /**
     * @param nesId the nesId to set
     */
    public void setNesId(String nesId) {
        this.nesId = nesId;
    }

    /**
     * @return the nesStatus
     */
    @Column(name = "curation_status", length = CurationStatus.MAXIMUM_NAME_LENGTH)
    @Enumerated(EnumType.STRING)
    @NotNull
    public CurationStatus getNesStatus() {
        return nesStatus;
    }

    /**
     * @param nesStatus the nesStatus to set
     */
    public void setNesStatus(CurationStatus nesStatus) {
        this.nesStatus = nesStatus;
    }

    /**
     * @return whether or not this person is pending curation updates
     */
    @Transient
    public boolean isUpdatePending() {
        return getUpdateRequested() != null;
    }

    /**
     * @return the updateRequested
     */
    @Column(name = "update_requested")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateRequested() {
        return updateRequested;
    }

    private void setUpdateRequested(Date updateRequested) {
        this.updateRequested = updateRequested;
    }

    /**
     * Updates the updateRequested Flag date to a new date, keeping it up to date with
     * requests.
     */
    public void requestUpdate() {
        setUpdateRequested(new Date());
    }

    /**
     * Updates the updateRequested Flag date to to null.
     */
    public void clearPendingUpdate() {
        setUpdateRequested(null);
    }

    /**
     * @return the firstName
     */
    @NotEmpty
    @Length(max = LONG_COL_LENGTH)
    @Column(name = "first_name")
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the middleName
     */
    @Length(max = LONG_COL_LENGTH)
    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    @NotEmpty
    @Length(max = LONG_COL_LENGTH)
    @Column(name = "last_name")
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the suffix
     */
    @Length(max = SHORT_COL_LENGTH)
    @Column(name = "suffix")
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the prefix
     */
    @Length(max = SHORT_COL_LENGTH)
    @Column(name = "prefix")
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the email address
     */
    @Email
    @NotEmpty
    @Length(max = LONG_COL_LENGTH)
    @Column(name = "email")
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT)
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param postalAddress the postalAddress to set
     */
    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }

    /**
     * @return the postalAddress
     */
    @Valid
    @Embedded
    public Address getPostalAddress() {
        return postalAddress;
    }

    /**
     * @return the lastNesRefresh
     */
    @Column(name = "last_nes_refresh")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastNesRefresh() {
        return lastNesRefresh;
    }

    /**
     * @param lastNesRefresh the lastNesRefresh to set
     */
    public void setLastNesRefresh(Date lastNesRefresh) {
        this.lastNesRefresh = lastNesRefresh;
    }

    /**
     * @return the ctepId
     */
    @Column(name = "ctep_id", unique = true)
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT, caseSensitive = true)
    @Index(name = "person_ctep_id_index")
    public String getCtepId() {
        return ctepId;
    }

    /**
     * @param ctepId the ctepId to set
     */
    public void setCtepId(String ctepId) {
        this.ctepId = ctepId;
    }

    /**
     * @return Provider Number
     */
    @Column(name = "provider_number")
    @Length(max = LONG_COL_LENGTH)
    public String getProviderNumber() {
        return providerNumber;
    }

    /**
     * @param providerNumber the Provider Number to set
     */
    public void setProviderNumber(String providerNumber) {
        this.providerNumber = providerNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * @return whether the person has a record in NES.
     */
    public boolean hasNesRecord() {
        return StringUtils.isNotEmpty(nesId);
    }

}
