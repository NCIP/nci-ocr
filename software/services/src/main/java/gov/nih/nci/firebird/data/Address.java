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

import gov.nih.nci.firebird.common.FirebirdConstants;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import com.fiveamsolutions.nci.commons.search.Searchable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Primary address class.
 */
@Embeddable
public class Address implements Serializable, Comparable<Address> {

    private static final long serialVersionUID = 1L;

    private static final int LINE_LENGTH = 254;
    private static final int CITY_LENGTH = 50;
    private static final int STATE_LENGTH = 50;
    private static final int POSTAL_LENGTH = 20;
    private static final int COUNTRY_LENGTH = 3;

    private String streetAddress; // Line 1 Address
    private String deliveryAddress; // Line 2 Address
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String country = FirebirdConstants.US_COUNTRY_CODE;

    /**
     * default constructor.
     */
    public Address() {
        // Empty constructor.
    }

    /**
     * @return line 1
     */
    @NotEmpty
    @Length(max = LINE_LENGTH)
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    @Column(name = "street_address")
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * @param streetAddressLine line 1
     */
    public void setStreetAddress(String streetAddressLine) {
        this.streetAddress = streetAddressLine;
    }

    /**
     * @return line 2
     */
    @Length(max = LINE_LENGTH)
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    @Column(name = "delivery_address")
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * @param deliveryAddressLine line 2
     */
    public void setDeliveryAddress(String deliveryAddressLine) {
        this.deliveryAddress = deliveryAddressLine;
    }

    /**
     * @return city
     */
    @NotEmpty
    @Length(max = CITY_LENGTH)
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    /**
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return postal code
     */
    @NotEmpty
    @Length(max = POSTAL_LENGTH)
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    @Column(name = "postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return stateOrProvince, either the two-letter US state code or non-US state information.
     */
    @Length(max = STATE_LENGTH)
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    @Column(name = "state_or_province", length = STATE_LENGTH)
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * @param stateOrProvince stateOrProvince
     */
    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    /**
     * @return country the 3-letter country code
     */
    @NotNull
    @Searchable(nested = true)
    @Length(min = COUNTRY_LENGTH, max = COUNTRY_LENGTH)
    @Column(length = COUNTRY_LENGTH)
    public String getCountry() {
        return country;
    }

    /**
     * @param country the 3-letter country code.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Copies all fields from another address instance.
     *
     * @param copyFrom copy fields from this address
     */
    public void copyFrom(Address copyFrom) {
        setStreetAddress(copyFrom.getStreetAddress());
        setDeliveryAddress(copyFrom.getDeliveryAddress());
        setCity(copyFrom.getCity());
        setStateOrProvince(copyFrom.getStateOrProvince());
        setPostalCode(copyFrom.getPostalCode());
        setCountry(copyFrom.getCountry());
    }

    @Override
    public int compareTo(Address t) {
        return new CompareToBuilder().append(getCountry(), t.getCountry())
                .append(getStateOrProvince(), t.getStateOrProvince()).append(getCity(), t.getCity())
                .append(getPostalCode(), t.getPostalCode()).append(getStreetAddress(), t.getStreetAddress())
                .append(getDeliveryAddress(), t.getDeliveryAddress()).toComparison();
    }

    @Override
    public String toString() {
        return toString("\n");
    }

    private String toString(String lineDelimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStreetAddress());
        sb.append(lineDelimiter);
        if (StringUtils.isNotEmpty(getDeliveryAddress())) {
            sb.append(getDeliveryAddress());
            sb.append(lineDelimiter);
        }
        sb.append(getCity());
        if (StringUtils.isNotEmpty(getStateOrProvince())) {
            sb.append(", ");
            sb.append(getStateOrProvince());
        }
        if (StringUtils.isNotEmpty(getPostalCode())) {
            sb.append(" ");
            sb.append(getPostalCode());
        }
        if (StringUtils.isNotEmpty(getCountry())) {
            sb.append(lineDelimiter);
            sb.append(getCountry());
        }
        return sb.toString();
    }

    /**
     * @return the address in single line format.
     */
    public String toOneLineString() {
        return toString(", ");
    }

    /**
     * @return true if the stateOrProvince field is valid for the current country.
     */
    @Transient
    public boolean isStateOrProvinceValid() {
        return !isUsAddress() || !StringUtils.isBlank(getStateOrProvince());
    }

    /**
     * @return true if country is USA.
     */
    @Transient
    public boolean isUsAddress() {
        return FirebirdConstants.US_COUNTRY_CODE.equals(getCountry());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }

        Address other = (Address) obj;

        return new EqualsBuilder().append(getStreetAddress(), other.getStreetAddress())
                .append(getDeliveryAddress(), other.getDeliveryAddress()).append(getCity(), other.getCity())
                .append(getStateOrProvince(), other.getStateOrProvince())
                .append(getPostalCode(), other.getPostalCode()).append(getCountry(), other.getCountry()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getStreetAddress()).append(getDeliveryAddress()).append(getCity())
                .append(getStateOrProvince()).append(getPostalCode()).append(getCountry()).toHashCode();
    }
}
