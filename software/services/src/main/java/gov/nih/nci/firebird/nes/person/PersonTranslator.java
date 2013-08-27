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
package gov.nih.nci.firebird.nes.person;

import java.util.Date;

import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.firebird.nes.NesIIRoot;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.iso21090.extensions.Id;

import org.apache.commons.lang.StringUtils;
import org.iso._21090.ENPN;
import org.iso._21090.ENXP;
import org.iso._21090.EntityNamePartType;
import org.iso._21090.II;

/**
 * Helper to translate PO persons into Firebird persons.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class PersonTranslator {

    /**
     * Translate an array of people.
     *
     * @param source the source.
     * @return the dest.
     */

    public static gov.nih.nci.firebird.data.Person[] buildFirebirdPeople(Person[] source) {
        if (source == null) {
            return null;
        }

        gov.nih.nci.firebird.data.Person[] dest = new gov.nih.nci.firebird.data.Person[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = buildFirebirdPerson(source[i]);
        }
        return dest;
    }

    /**
     * Build a Firebird person from the NES person.
     *
     * @param sourcePerson the source.
     * @return the FB person.
     */
    public static gov.nih.nci.firebird.data.Person buildFirebirdPerson(Person sourcePerson) {
        if (sourcePerson == null) {
            return null;
        }

        gov.nih.nci.firebird.data.Person destPerson = new gov.nih.nci.firebird.data.Person();
        destPerson.setNesId(NesTranslatorHelperUtils.handleIi(sourcePerson.getIdentifier()));
        handleName(destPerson, sourcePerson);
        destPerson.setPostalAddress(NesTranslatorHelperUtils.getAddress(sourcePerson.getPostalAddress()));
        destPerson.setEmail(NesTranslatorHelperUtils.getEmailTelValue(sourcePerson.getTelecomAddress()));
        destPerson.setPhoneNumber(NesTranslatorHelperUtils.getPhoneTelValue(sourcePerson.getTelecomAddress()));
        destPerson.setNesStatus(NesTranslatorHelperUtils.handleCurationStatus(sourcePerson.getStatusCode()));
        destPerson.setLastNesRefresh(new Date());
        return destPerson;
    }

    private static void handleName(gov.nih.nci.firebird.data.Person dest, Person source) {
        ENPN name = source.getName();
        if (name != null) {
            for (ENXP part : name.getPart()) {
                handlePart(dest, part);
            }
        }
    }

    private static void handlePart(gov.nih.nci.firebird.data.Person dest, ENXP part) {
        if (EntityNamePartType.PFX.equals(part.getType())) {
            dest.setPrefix(part.getValue());
        } else if (EntityNamePartType.FAM.equals(part.getType())) {
            dest.setLastName(part.getValue());
        } else if (EntityNamePartType.GIV.equals(part.getType())) {
            if (StringUtils.isNotBlank(dest.getFirstName())) {
                dest.setMiddleName(part.getValue());
            } else {
                dest.setFirstName(part.getValue());
            }
        } else if (EntityNamePartType.SFX.equals(part.getType())) {
            dest.setSuffix(part.getValue());
        }
    }

    /**
     * Build an NES person.
     *
     * @param source the FB person.
     * @return the NES person.
     */
    public static Person buildNesPerson(gov.nih.nci.firebird.data.Person source) {
        Person p = new Person();
        if (source.getNesId() != null) {
            p.setIdentifier(buildIi(source.getNesId()));
        }
        p.setName(buildNesName(source));
        p.setPostalAddress(NesTranslatorHelperUtils.toAd(source.getPostalAddress()));
        String emailAddress = source.getEmail();
        String phoneNumber = source.getPhoneNumber();
        String country = NesTranslatorHelperUtils.getCountry(source.getPostalAddress());
        p.setTelecomAddress(NesTranslatorHelperUtils.buildNesTelcommInfo(emailAddress, phoneNumber, country));
        p.setStatusCode(NesTranslatorHelperUtils.buildStatus(source.getNesStatus()));
        return p;
    }

    /**
     * Build the person object for nes.
     *
     * @param firstName the first name.
     * @param lastName the last name.
     * @return the person
     */
    public static Person buildNesPerson(String firstName, String lastName) {
        Person p = new Person();
        p.setName(buildNesName(null, firstName, null, lastName, null));
        return p;
    }

    /**
     * Builds an Id from a String.
     *
     * @param nesId String
     * @return valid instance of Id
     */
    public static Id buildId(String nesId) {
        if (nesId == null) {
            return null;
        }

        Id dest = new Id();
        dest.setExtension(nesId);
        dest.setRoot(NesIIRoot.PERSON.getRoot());
        return dest;
    }

    /**
     * Builds an II datatype Id for the Person Object.
     *
     * @param nesId the Nes ID provided by NES
     * @return the completed II object
     */
    public static II buildIi(String nesId) {
        II ii = NesTranslatorHelperUtils.buildIi(NesIIRoot.PERSON.getRoot(), nesId);
        if (ii != null) {
            ii.setIdentifierName(NesTranslatorHelperUtils.PERSON_IDENTIFIER_NAME);
        }
        return ii;
    }

    private static ENPN buildNesName(gov.nih.nci.firebird.data.Person source) {
        return buildNesName(source.getPrefix(), source.getFirstName(), source.getMiddleName(), source.getLastName(),
                source.getSuffix());
    }

    private static ENPN buildNesName(String prefix, String firstName, String middleName,
            String lastName, String suffix) {
        ENPN name = new ENPN();

        addPrefix(prefix, name);
        addGivenName(firstName, middleName, name);
        addLastName(lastName, name);
        addSuffix(suffix, name);

        return name;
    }

    private static void addPrefix(String prefix, ENPN name) {
        if (StringUtils.isNotBlank(prefix)) {
            ENXP currentPart = new ENXP();
            currentPart.setType(EntityNamePartType.PFX);
            currentPart.setValue(prefix);
            name.getPart().add(currentPart);
        }
    }

    private static void addGivenName(String firstName, String middleName, ENPN name) {
        if (StringUtils.isNotBlank(firstName)) {
            ENXP currentPart = new ENXP();
            currentPart.setType(EntityNamePartType.GIV);
            currentPart.setValue(firstName);
            name.getPart().add(currentPart);

            if (StringUtils.isNotBlank(middleName)) {
                currentPart = new ENXP();
                currentPart.setType(EntityNamePartType.GIV);
                currentPart.setValue(middleName);
                name.getPart().add(currentPart);
            }
        }
    }

    private static void addLastName(String lastName, ENPN name) {
        if (StringUtils.isNotBlank(lastName)) {
            ENXP currentPart = new ENXP();
            currentPart.setType(EntityNamePartType.FAM);
            currentPart.setValue(lastName);
            name.getPart().add(currentPart);
        }
    }

    private static void addSuffix(String suffix, ENPN name) {
        if (StringUtils.isNotBlank(suffix)) {
            ENXP currentPart = new ENXP();
            currentPart.setType(EntityNamePartType.SFX);
            currentPart.setValue(suffix);
            name.getPart().add(currentPart);
        }
    }
}
