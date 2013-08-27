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

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.fiveamsolutions.nci.commons.audit.Auditable;
import com.fiveamsolutions.nci.commons.search.Searchable;


/**
 * Country represents <a
 * href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm">
 * ISO 3166-1</a> codes for the names of countries as published by ISO.  This class uses the English names.
 */
@Entity(name =  "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) // Unit tests write, so cannot use read-only
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Country implements Auditable {
    private static final long serialVersionUID = 1L;
    private static final int ALPHA2_LENGTH = 2;
    private static final int ALPHA3_LENGTH = 3;
    private static final int NUMERIC_LENGTH = 3;
    private static final int NAME_LENGTH = 254;

    private Long id;
    private String name;
    private String numeric;
    private String alpha2;
    private String alpha3;
    private SortedSet<State> states = new TreeSet<State>();

    /**
     * For unit tests only.
     *
     * @param name official country name per ISO 3166/MA
     * @param numeric numeric-3 country code, per ISO 3166-1
     * @param alpha2 two-letter country code, per ISO 3166-1
     * @param alpha3 three-letter country code, per ISO 3166-1
     */
    public Country(String name, String numeric, String alpha2, String alpha3) {
        this.name = name;
        this.numeric = numeric;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
    }

    @SuppressWarnings("unused") //Hibernate-only constructor
    private Country() {
        // for hibernate only - do nothing
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Searchable
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id database id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return official country name per ISO 3166/MA
     */
    @Column(updatable = false, unique = true)
    @Length(max = NAME_LENGTH)
    @NotEmpty
    @Searchable(matchMode = Searchable.MATCH_MODE_CONTAINS)
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    /**
     * @return two-letter country code, per ISO 3166-1
     */
    @Column(updatable = false, unique = true)
    @Length(min = NUMERIC_LENGTH, max = NUMERIC_LENGTH)
    @NotEmpty
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT)
    public String getNumeric() {
        return numeric;
    }

    @SuppressWarnings("unused")
    private void setNumeric(String numeric) {
        this.numeric = numeric;
    }

    /**
     * @return two-letter country code, per ISO 3166-1
     */
    @Column(updatable = false, unique = true)
    @Length(min = ALPHA2_LENGTH, max = ALPHA2_LENGTH)
    @NotEmpty
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT)
    public String getAlpha2() {
        return alpha2;
    }

    @SuppressWarnings("unused")
    private void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    /**
     * @return three-letter country code, per ISO 3166-1
     */
    @Column(updatable = false, unique = true)
    @Index(name = "alpha3_idx")
    @Length(min = ALPHA3_LENGTH, max = ALPHA3_LENGTH)
    @NotEmpty
    @Searchable(matchMode = Searchable.MATCH_MODE_EXACT)
    public String getAlpha3() {
        return alpha3;
    }

    @SuppressWarnings("unused")
    private void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    /**
     * @return the states
     */
    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)  // Unit tests write, so cannot use read-only
    @Sort(type = SortType.NATURAL)
    public SortedSet<State> getStates() {
        return states;
    }

    @SuppressWarnings("unused")
    private void setStates(SortedSet<State> states) {
        this.states = states;
    }

    // equals and hashcode intentionally not implemented - lookup class && hibernate can optimize this case
}
