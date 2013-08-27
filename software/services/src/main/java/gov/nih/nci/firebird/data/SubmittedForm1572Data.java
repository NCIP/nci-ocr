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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;
import com.google.common.collect.Sets;

/**
 * Contains all the snapshot data for a Form 1572 at submission time.
 */
@Entity(name = "submitted_form_1572_data")
public class SubmittedForm1572Data implements PersistentObject {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private PersonSnapshot personSnapshot;
    private OrganizationSnapshot primaryOrganizationSnapshot;
    private Set<PracticeSiteSnapshot> practiceSiteSnapshots = Sets.newHashSet();
    private Set<ClinicalLaboratorySnapshot> labSnapshots = Sets.newHashSet();
    private Set<InstitutionalReviewBoardSnapshot> irbSnapshots = Sets.newHashSet();

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
     * @return the personSnapshot
     */
    @OneToOne
    @JoinColumn(name = "person_snapshot_id")
    @ForeignKey(name = "submitted_form_1572_person_snapshot_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public PersonSnapshot getPersonSnapshot() {
        return personSnapshot;
    }
    
    /**
     * @param personSnapshot the personSnapshot to set
     */
    public void setPersonSnapshot(PersonSnapshot personSnapshot) {
        this.personSnapshot = personSnapshot;
    }
    
    /**
     * @return the primaryOrganizationSnapshot
     */
    @OneToOne
    @JoinColumn(name = "primary_organization_snapshot_id")
    @ForeignKey(name = "submitted_form_1572_primary_organization_snapshot_fkey")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public OrganizationSnapshot getPrimaryOrganizationSnapshot() {
        return primaryOrganizationSnapshot;
    }
    
    /**
     * @param primaryOrganizationSnapshot the primaryOrganizationSnapshot to set
     */
    public void setPrimaryOrganizationSnapshot(OrganizationSnapshot primaryOrganizationSnapshot) {
        this.primaryOrganizationSnapshot = primaryOrganizationSnapshot;
    }
    
    /**
     * @return the practiceSiteSnapshots
     */
    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinTable(name = "submitted_form_1572_practice_site_snapshots",
        joinColumns = @JoinColumn(name = "submitted_form_1572_data_id"),
        inverseJoinColumns = @JoinColumn(name = "practice_site_snapshot_id"))
    @ForeignKey(name = "submitted_form_1572_practice_site_snapshot_form_data_fkey", 
        inverseName = "submitted_form_1572_practice_site_snapshot_snapshot_fkey")
    public Set<PracticeSiteSnapshot> getPracticeSiteSnapshots() {
        return practiceSiteSnapshots;
    }
    
    @SuppressWarnings("unused") // required for Hibernate
    private void setPracticeSiteSnapshots(Set<PracticeSiteSnapshot> practiceSiteSnapshots) {
        this.practiceSiteSnapshots = practiceSiteSnapshots;
    }
    
    /**
     * @return the labSnapshots
     */
    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinTable(name = "submitted_form_1572_lab_snapshots",
        joinColumns = @JoinColumn(name = "submitted_form_1572_data_id"),
        inverseJoinColumns = @JoinColumn(name = "lab_snapshot_id"))
    @ForeignKey(name = "submitted_form_1572_lab_snapshot_form_data_fkey", 
        inverseName = "submitted_form_1572_lab_snapshot_snapshot_fkey")
    public Set<ClinicalLaboratorySnapshot> getLabSnapshots() {
        return labSnapshots;
    }
    
    @SuppressWarnings("unused") // required for Hibernate
    private void setLabSnapshots(Set<ClinicalLaboratorySnapshot> labSnapshots) {
        this.labSnapshots = labSnapshots;
    }
    
    /**
     * @return the irbSnapshots
     */
    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinTable(name = "submitted_form_1572_irb_snapshots",
        joinColumns = @JoinColumn(name = "submitted_form_1572_data_id"),
        inverseJoinColumns = @JoinColumn(name = "irb_snapshot_id"))
    @ForeignKey(name = "submitted_form_1572_irb_snapshot_form_data_fkey", 
        inverseName = "submitted_form_1572_irb_snapshot_snapshot_fkey")
    public Set<InstitutionalReviewBoardSnapshot> getIrbSnapshots() {
        return irbSnapshots;
    }
    
    @SuppressWarnings("unused") // required for Hibernate
    private void setIrbSnapshots(Set<InstitutionalReviewBoardSnapshot> irbSnapshots) {
        this.irbSnapshots = irbSnapshots;
    }

}
