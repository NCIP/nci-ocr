/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This firebird_src Software License (the License) is between NCI and You. You (or
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
 * its rights in the firebird_src Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the firebird_src Software; (ii) distribute and
 * have distributed to and by third parties the firebird_src Software and any
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
package gov.nih.nci.firebird.nes.common;

import static gov.nih.nci.firebird.common.FirebirdConstants.US_COUNTRY_CODE;

import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.CurationStatus;
import gov.nih.nci.iso21090.extensions.Id;
import gov.nih.nci.iso21090.grid.dto.transform.iso.TSTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.iso._21090.AD;
import org.iso._21090.ADXP;
import org.iso._21090.AddressPartType;
import org.iso._21090.CD;
import org.iso._21090.DSETTEL;
import org.iso._21090.II;
import org.iso._21090.NullFlavor;
import org.iso._21090.TEL;
import org.iso._21090.TELEmail;
import org.iso._21090.TELPhone;
import org.iso._21090.TS;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Class that adds a bunch of common conversion methods to be used in the Person
 * and Organization translators.
 */
@SuppressWarnings("PMD.TooManyMethods")
// Utility class exposes many methods plus helpers
public class NesTranslatorHelperUtils {

    static final String CANADA_COUNTRY_CODE = "CAN";

    private static final int AREA_CODE_START_INDEX = 0;
    private static final int AREA_CODE_END_INDEX = 3;
    private static final int PHONE_LOCAL_PART_1_START_INDEX = 3;
    private static final int PHONE_LOCAL_PART_1_END_INDEX = 6;
    private static final int PHONE_LOCAL_PART_2_START_INDEX = 6;
    private static final int PHONE_LOCAL_PART_2_END_INDEX = 10;

    private static final String PREFIX_SEPARATOR = ":";
    private static final String EMAIL_TEL_PREFIX = "mailto";
    private static final String PHONE_TEL_PREFIX = "tel";

    /**
     * Required identifier name for sending Person related II objects to NES.
     */
    public static final String PERSON_IDENTIFIER_NAME = "NCI person entity identifier";

    /**
     * Required identifier name for sending Organization related II objects to NES.
     */
    public static final String ORG_IDENTIFIER_NAME = "NCI organization entity identifier";
    private static final int REQUIRED_PHONE_DIGITS = 10;


    /**
     * convert an iso/xml timestamp into a Date.
     * @param timestamp iso TS type to convert.
     * @return the java Date.
     * @see TSTransformer#FORMAT_STRING
     */
    public static Date getDate(TS timestamp) {
        if (timestamp == null || timestamp.getValue() == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(TSTransformer.FORMAT_STRING, Locale.getDefault());
        try {
            return format.parse(timestamp.getValue());
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * convert a java Date into an iso/xml date.
     * @param date date to convert
     * @return the iso date
     * @see TSTransformer#FORMAT_STRING.
     */
    public static TS createDateTs(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(TSTransformer.FORMAT_STRING, Locale.getDefault());
        TS ts = new TS();
        ts.setValue(format.format(date));
        return ts;
    }

    /**
     * Extracts and returns the first email TEL value, non-prefixed.
     *
     * @param tels the list of TELs
     * @return the non-prefixed value
     */
    public static String getEmailTelValue(DSETTEL tels) {
        return getTelValue(EMAIL_TEL_PREFIX, tels);
    }

    /**
     * Extracts and returns the first phone TEL value, non-prefixed.
     *
     * @param tels the list of TELs
     * @return the non-prefixed value
     */
    public static String getPhoneTelValue(DSETTEL tels) {
        return getTelValue(PHONE_TEL_PREFIX, tels);
    }

    private static String getTelValue(String prefix, DSETTEL tels) {
        if (tels != null) {
            for (TEL tel : tels.getItem()) {
                if (StringUtils.startsWith(tel.getValue(), prefix)) {
                    return tel.getValue().split(PREFIX_SEPARATOR, 2)[1];
                }
            }
        }
        return null;
    }

    /**
     * Creates a new NES DSETTEL for phone and email.
     *
     * @param emailAddress the email address (required)
     * @param phoneNumber the phone number (optional)
     * @param countryCode three-letter country code for the country phone number is in
     * @return the NES DSETTEL
     */
    public static DSETTEL buildNesTelcommInfo(String emailAddress, String phoneNumber, String countryCode) {
        DSETTEL telcomms = new DSETTEL();
        telcomms.getItem().add(createEmailTel(emailAddress));
        if (StringUtils.isNotBlank(phoneNumber)) {
            telcomms.getItem().add(createPhoneTel(phoneNumber, countryCode));
        }
        return telcomms;
    }

    /**
     * Creates an Email TEL object for the provided value.
     *
     * @param value the value of the TEL
     * @return the TEL object
     */
    public static TELEmail createEmailTel(String value) {
        TELEmail email = new TELEmail();
        setTelFields(email, EMAIL_TEL_PREFIX, value);

        return email;
    }

    private static void setTelFields(TEL tel, String prefix, String value) {
        String telValue = value;
        if (StringUtils.isNotBlank(telValue)) {
            String whiteSpace = "\\s";
            telValue = telValue.replaceAll(whiteSpace, "");
        }
        tel.setValue(prefix + PREFIX_SEPARATOR + telValue);
    }

    /**
     * Creates an Phone TEL object for the provided value.
     *
     * @param value the value of the TEL
     * @param countryCode three-letter country code for the country phone number is in
     * @return the TEL object
     */
    public static TELPhone createPhoneTel(String value, String countryCode) {
        TELPhone phone = new TELPhone();
        setTelFields(phone, PHONE_TEL_PREFIX, formatPhoneNumber(value, countryCode));
        return phone;
    }

    private static String formatPhoneNumber(String value, String countryCode) {
        if (value != null && useUsPhoneFormat(value, countryCode)) {
            return formatUsPhoneNumber(value);
        } else {
            return value;
        }
    }

    private static boolean useUsPhoneFormat(String value, String countryCode) {
        return Lists.newArrayList(US_COUNTRY_CODE, CANADA_COUNTRY_CODE).contains(
                StringUtils.upperCase(countryCode))
                && isValidUsPhoneNumberLength(value);
    }

    private static boolean isValidUsPhoneNumberLength(String phoneNumber) {
        return getDigits(phoneNumber).length() >= REQUIRED_PHONE_DIGITS;
    }

    private static String formatUsPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();
        String mainDigits = getDigits(getWithoutExtension(phoneNumber));
        sb.append(mainDigits.substring(AREA_CODE_START_INDEX, AREA_CODE_END_INDEX));
        sb.append('-');
        sb.append(mainDigits.substring(PHONE_LOCAL_PART_1_START_INDEX, PHONE_LOCAL_PART_1_END_INDEX));
        sb.append('-');
        sb.append(mainDigits.substring(PHONE_LOCAL_PART_2_START_INDEX, PHONE_LOCAL_PART_2_END_INDEX));
        String extension = getExtension(phoneNumber);
        if (StringUtils.isNotEmpty(extension)) {
            sb.append('x');
            sb.append(extension);
        }
        return sb.toString();
    }

    private static String getDigits(CharSequence charSequence) {
        return CharMatcher.DIGIT.retainFrom(charSequence);
    }

    private static CharSequence getWithoutExtension(String phoneNumber) {
        return getSplitPhoneNumberAndExtension(phoneNumber).iterator().next();
    }

    private static Iterable<String> getSplitPhoneNumberAndExtension(String phoneNumber) {
        return Splitter.on(CharMatcher.anyOf("Xx")).trimResults().split(phoneNumber);
    }

    private static String getExtension(String phoneNumber) {
        Iterator<String> iterator = getSplitPhoneNumberAndExtension(phoneNumber).iterator();
        iterator.next();
        if (iterator.hasNext()) {
            return getDigits(iterator.next());
        } else {
            return null;
        }
    }

    /**
     * Builds an II iso datatype.
     *
     * @param root the Root to use in the II id
     * @param nesId the value of the id
     * @return the II iso object
     */
    public static II buildIi(String root, String nesId) {
        if (nesId == null) {
            return null;
        }

        II dest = new II();
        dest.setExtension(nesId);
        dest.setRoot(root);
        return dest;
    }

    /**
     * Builds an Id iso datatype.
     *
     * @param root the Root to use in the Id
     * @param nesId the value of the Id
     * @return the Id iso object
     */
    public static Id buildId(String root, String nesId) {
        if (nesId == null) {
            return null;
        }

        Id dest = new Id();
        dest.setExtension(nesId);
        dest.setRoot(root);
        return dest;
    }

    /**
     * Extract the NES Id from the II object.
     *
     * @param source the II object
     * @return the Extracted Nes Id.
     */
    public static String handleIi(II source) {
        String ret = null;
        if (source != null) {
            ret = source.getExtension();
        }
        return ret;
    }

    /**
     * Creates an address for use in NES from a FIREBIRD address.
     *
     * @param sourceAddress the FIREBIRD address
     * @return the NES address.
     */
    public static AD toAd(Address sourceAddress) {

        AD address = new AD();

        if (sourceAddress != null) {
            addAdxp(address, AddressPartType.AL, sourceAddress.getStreetAddress());
            addAdxp(address, AddressPartType.ADL, sourceAddress.getDeliveryAddress());
            addAdxp(address, AddressPartType.CTY, sourceAddress.getCity());
            addAdxp(address, AddressPartType.STA, sourceAddress.getStateOrProvince());
            addAdxp(address, AddressPartType.ZIP, sourceAddress.getPostalCode());
            addAdxp(address, AddressPartType.CNT, sourceAddress.getCountry());
        } else {
            address.setNullFlavor(NullFlavor.NI);
        }
        return address;
    }

    private static void addAdxp(AD address, AddressPartType part, String value) {
        if (StringUtils.isNotBlank(value)) {
            ADXP item = new ADXP();
            if (part == AddressPartType.CNT) {
                setupCountryADXP(item, value);
            } else {
                setupStandardADXP(item, part, value);
            }
            address.getPart().add(item);
        }
    }

    private static void setupStandardADXP(ADXP item, AddressPartType part, String value) {
        item.setType(part);
        item.setValue(value);
    }

    private static void setupCountryADXP(ADXP country, String value) {
        country.setType(AddressPartType.CNT);
        country.setCode(value);
        country.setCodeSystem("ISO 3166");
    }

    /**
     * Translates an NES address to a FIREBIRD address.
     *
     * @param nesAddress the address from NES
     * @return the FIREBIRD address
     */
    public static Address getAddress(AD nesAddress) {
        if (nesAddress == null) {
            return null;
        } else {
            Address firebirdAddress = new Address();
            for (ADXP part : nesAddress.getPart()) {
                handleAddressPart(firebirdAddress, part);
            }
            return firebirdAddress;
        }
    }

    private static void handleAddressPart(Address firebirdAddress, ADXP part) {
        if (AddressPartType.CTY.equals(part.getType())) {
            firebirdAddress.setCity(part.getValue());
        } else if (AddressPartType.STA.equals(part.getType())) {
            firebirdAddress.setStateOrProvince(part.getValue());
        } else if (AddressPartType.ZIP.equals(part.getType())) {
            firebirdAddress.setPostalCode(part.getValue());
        } else if (AddressPartType.CNT.equals(part.getType())) {
            firebirdAddress.setCountry(part.getCode());
        } else {
            handleStreetAddressPart(firebirdAddress, part);
        }
    }

    private static void handleStreetAddressPart(Address firebirdAddress, ADXP part) {
        if (AddressPartType.AL.equals(part.getType())) {
            firebirdAddress.setStreetAddress(part.getValue());
        } else if (AddressPartType.ADL.equals(part.getType())) {
            firebirdAddress.setDeliveryAddress(part.getValue());
        }
    }

    /**
     * Returns the country code from an address.
     *
     * @param address the address (may be null)
     * @return the country code, null if none
     */
    public static String getCountry(Address address) {
        if (address != null) {
            return address.getCountry();
        } else {
            return null;
        }
    }

    /**
     * Returns a CD object with the code field populated with the code value of the enum.
     *
     * @param status the status to be recorded
     * @return the CD object.
     */
    public static CD buildStatus(CurationStatus status) {
        if (status != CurationStatus.PRE_NES_VALIDATION) {
            if (status != null) {
                CD result = new CD();
                result.setCode(status.getCodeValue());
                return result;
            } else {
                throw new IllegalArgumentException("Null Curation Status provided!");
            }
        }
        return null;
    }

    /**
     * Returns a CurationStatus enumeration based on the value in the CD object.
     * @param status the status returned from NES.
     * @return the CurationStatus type.
     */
    public static CurationStatus handleCurationStatus(CD status) {
        if (status != null && StringUtils.isNotEmpty(status.getCode())) {
            return CurationStatus.valueOf(status.getCode().toUpperCase());
        } else {
            throw new IllegalArgumentException("NES Returned a NULL status value. This should not have happened.");
        }
    }

    /**
     * Converts an II to an Id.
     *
     * @param ii the II identifier
     * @return the identifer as an Id
     */
    public static Id toId(II ii) {
        return buildId(ii.getRoot(), ii.getExtension());
    }
}
