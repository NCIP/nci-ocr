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
package gov.nih.nci.firebird.nes.common;

import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.StringMapType.Entry;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;

/**
 * A Utility class to translate a StringMap received from NES into a ValidationResult
 * for throwing upwards in FIREBIRD.
 */
public class ValidationErrorTranslator {

    private static final String EMAIL_ADDRESS_FIELD = "email";
    private static final String PHONE_NUMBER_FIELD = "phoneNumber";
    private static final String STATUS_FIELD = "status";
    private static final String PLAYER_FIELD = "player";
    private static final String STATE_FIELD = "postalAddress.null";
    private static final String PHONE_NUMBER_FIELD_PREFIX = "Phone number";
    private static final String MISSING_INFORMATION_ERROR_MESSAGE_KEY = "nes.validation.error.missing.information";
    private static final String MALFORMED_PHONE_NUMBER_MESSAGE_KEY = "nes.validation.error.phone.number.wrong.digits";

    private static final String FIELD_NAME_KEY_SUFFIX = ".fieldName";
    private static final String FIELD_MAPPING_KEY_SUFFIX = ".mapping";
    
    private static final String FIELD_MISSING_ERROR = "must be set";
    
    private static final Map<String, Set<String>> IGNORED_ERRORS = Maps.newHashMap();
    
    static {
        IGNORED_ERRORS.put(STATUS_FIELD, Sets.newHashSet(FIELD_MISSING_ERROR));
        IGNORED_ERRORS.put(PLAYER_FIELD, Sets.newHashSet(FIELD_MISSING_ERROR));
    }

    private final Properties properties;
    private final ResourceBundle resources;

    /**
     * constructor.
     *
     * @param properties the properties that hold conversions from Nes variables.
     * @param resources the resources which hold messages.
     */
    @Inject
    public ValidationErrorTranslator(@Named("nes.mapping.properties") Properties properties,
            ResourceBundle resources) {
        this.properties = properties;
        this.resources = resources;
    }

    /**
     * Translate a StringMap from NES to a ValidationResult.
     *
     * @param nesValidationResults the validation results from NES validation.
     * @return the ValidationResult.
     */
    public ValidationResult translateStringMapValidation(StringMap nesValidationResults) {
        ValidationResult validationResult = new ValidationResult();
        if (!nesValidationResults.getEntry().isEmpty()) {
            handleValidationErrors(validationResult, nesValidationResults);
        }
        return validationResult;
    }

    private void handleValidationErrors(ValidationResult validationResult, StringMap nesValidationResults) {
        for (Entry errorSet : nesValidationResults.getEntry()) {
            convertFieldErrors(validationResult, errorSet);
        }
    }

    private void convertFieldErrors(ValidationResult validationResult, Entry fieldResult) {
        String key = fieldResult.getKey();
        for (String error : fieldResult.getValue()) {
            if (!shouldIgnore(key, error)) {
                ValidationFailure failure = handleError(key, error);
                validationResult.addFailure(failure);
            }
        }
    }

    private boolean shouldIgnore(String key, String error) {
        return IGNORED_ERRORS.containsKey(key) && IGNORED_ERRORS.get(key).contains(error);
    }

    private ValidationFailure handleError(String key, String error) {
        if (key.startsWith(EMAIL_ADDRESS_FIELD)) {
            return handleEmailAddress(key, error);
        } else if (key.startsWith(PHONE_NUMBER_FIELD_PREFIX)) {
            return handlePhoneNumber();
        } else if (key.equals(STATE_FIELD)) {
            return handleState(key);
        } else {
            return createNesFailure(key, error);
        }
    }

    private ValidationFailure handleEmailAddress(String key, String error) {
        if (key.contains("[")) {
            return createNesFailure(EMAIL_ADDRESS_FIELD, error);
        } else {
            return createNesFailure(EMAIL_ADDRESS_FIELD, resources.getString(MISSING_INFORMATION_ERROR_MESSAGE_KEY));
        }
    }

    private ValidationFailure createFailure(String key, String message) {
        String keyMapping = getFieldMapping(key);
        return new ValidationFailure(keyMapping, message);
    }

    private ValidationFailure handlePhoneNumber() {
        return createFailure(PHONE_NUMBER_FIELD, resources.getString(MALFORMED_PHONE_NUMBER_MESSAGE_KEY));
    }

    private ValidationFailure handleState(String key) {
        return createNesFailure(key, resources.getString(MISSING_INFORMATION_ERROR_MESSAGE_KEY));
    }

    private ValidationFailure createNesFailure(String key, String error) {
        String message = getFieldName(key) + " " + error;
        return createFailure(key, message);
    }

    private String getFieldMapping(String key) {
        String keyMapping = properties.getProperty(key + FIELD_MAPPING_KEY_SUFFIX);
        if (StringUtils.isBlank(keyMapping)) {
            return null;
        } else {
            return keyMapping;
        }
    }

    private String getFieldName(String key) {
        return properties.getProperty(key + FIELD_NAME_KEY_SUFFIX);
    }
}
