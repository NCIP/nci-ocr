/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR_src
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
package gov.nih.nci.firebird.test.util;

import static org.junit.Assert.*;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;

import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.ClinicalResearchExperience;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import java.util.Iterator;

/**
 * Compares various domain objects for deep field level equality. Also exposes comparison utility objects.
 */
public class ComparisonUtil {

    public static final ComparisonBuilder<InvestigatorProfile> PROFILE_COMPARISON_BUILDER = new ComparisonBuilder<InvestigatorProfile>();
    public static final ComparisonBuilder<Person> PERSON_COMPARISON_BUILDER = new ComparisonBuilder<Person>();
    public static final ComparisonBuilder<Organization> ORGANIZATION_COMPARISON_BUILDER = new ComparisonBuilder<Organization>();
    public static final ComparisonBuilder<Address> ADDRESS_COMPARISON_BUILDER = new ComparisonBuilder<Address>();
    public static final ComparisonBuilder<ClinicalResearchExperience> CLINCIAL_RESEARCH_EXPERIENCE_COMPARISON_BUILDER = new ComparisonBuilder<ClinicalResearchExperience>();

    static {
        PROFILE_COMPARISON_BUILDER.addExcludedFields("id");
        PROFILE_COMPARISON_BUILDER.addFieldComparisonBuilder("person", PERSON_COMPARISON_BUILDER);
        PROFILE_COMPARISON_BUILDER.addFieldComparisonBuilder("organization", ORGANIZATION_COMPARISON_BUILDER);
        PROFILE_COMPARISON_BUILDER.addFieldComparisonBuilder("clinicalResearchExperience", CLINCIAL_RESEARCH_EXPERIENCE_COMPARISON_BUILDER);
        PERSON_COMPARISON_BUILDER.addExcludedFields("id", "externalData");
        PERSON_COMPARISON_BUILDER.addFieldComparisonBuilder("postalAddress", ADDRESS_COMPARISON_BUILDER);
        ORGANIZATION_COMPARISON_BUILDER.addExcludedFields("id");
        ORGANIZATION_COMPARISON_BUILDER.addExcludedFields("externalData");
        ORGANIZATION_COMPARISON_BUILDER.addExcludedFields("roles");
        ORGANIZATION_COMPARISON_BUILDER.addFieldComparisonBuilder("postalAddress", ADDRESS_COMPARISON_BUILDER);
    }

    /**
     * Compares the content of the Organization and all objects in the Organization asserting equality or equivalence
     * for every field.
     *
     * @param o1 the first Organization to compare
     * @param o2 the second Organization to compare
     */
    public static void assertEquivalent(Organization o1, Organization o2) {
        ORGANIZATION_COMPARISON_BUILDER.assertEquivalent(o1, o2);
    }

    /**
     * Compares the content of the InvestigatorProfile and all objects in the InvestigatorProfile asserting equality or equivalence
     * for every field.
     *
     * @param p1 the first profile to compare
     * @param p2 the second profile to compare
     */
    public static void assertEquivalent(InvestigatorProfile p1, InvestigatorProfile p2) {
        PROFILE_COMPARISON_BUILDER.assertEquivalent(p1, p2);
    }

    /**
     * Compares the content of the address and all objects in the address asserting equality or equivalence
     * for every field.
     *
     * @param a1 the first address to compare
     * @param a2 the second address to compare
     */
    public static void assertEquivalent(Address a1, Address a2) {
        ADDRESS_COMPARISON_BUILDER.assertEquivalent(a1, a2);
    }

    /**
     * Compares the content of the Organization and all objects in the Organization checking equality or equivalence
     * for every field.
     *
     * @param o1 the first Organization to compare
     * @param o2 the second Organization to compare
     * @return true if equivalent, false if a field was found to differ.
     */
    public static boolean areEquivalent(Organization o1, Organization o2) {
        try {
            assertEquivalent(o1, o2);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Compares the content of the InvestigatorProfile and all objects in the InvestigatorProfile checking equality or equivalence
     * for every field.
     *
     * @param p1 the first profile to compare
     * @param p2 the second profile to compare
     * @return true if equivalent, false if a field was found to differ.
     */
    public static boolean areEquivalent(InvestigatorProfile p1, InvestigatorProfile p2) {
        try {
            assertEquivalent(p1, p2);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Compares the content of the address and all objects in the address checking equality or equivalence
     * for every field.
     *
     * @param p1 the first address to compare
     * @param p2 the second address to compare
     * @return true if equivalent, false if a field was found to differ.
     */
    public static boolean areEquivalent(Address a1, Address a2) {
        try {
            assertEquivalent(a1, a2);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Compares two <code>PersistentObjects</code>.
     *
     * @param object1 first object to compare
     * @param object2 second object to compare.
     */
    public static void assertSamePersistentObjects(PersistentObject object1, PersistentObject object2) {
        if (object1 == null) {
            assertNull(object2);
        } else if (object1.getId() == null) {
            assertSame(object1, object2);
        } else {
            assertNotNull(object2);
            assertTrue(object1.getClass().isAssignableFrom(object2.getClass()));
            assertEquals(object1.getId(), object2.getId());
        }
    }

    public static <PO extends PersistentObject> void assertSamePersistentObjects(Iterator<PO> i1, Iterator<PO> i2) {
        while (i1.hasNext()) {
            assertSamePersistentObjects(i1.next(), i2.next());
        }
        assertFalse(i2.hasNext());
    }
}
