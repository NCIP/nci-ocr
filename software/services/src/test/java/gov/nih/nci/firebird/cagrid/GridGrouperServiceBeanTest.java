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
package gov.nih.nci.firebird.cagrid;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipType;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.common.GridGrouperI;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GroupAddFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GroupNotFoundFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.MemberDeleteFault;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.axis.types.URI.MalformedURIException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class GridGrouperServiceBeanTest {
    
    private static final String TEST_FULLY_QUALIFIED_USERNAME = "/fully/qualified/username";
    private static final String TEST_GRID_GROUPER_URL = "http://url";
    private static final String TEST_GRID_GROUPER_STEM = "Organization:CBIIT:FIREBIRD";
    private static final String TEST_USER_IS_MEMBER_OF_GROUP = "investigator";
    private static final String TEST_USER_IS_NOT_MEMBER_OF_GROUP = "sponsor";

    private GridGrouperI mockGridGrouper = mock(GridGrouperI.class);
    private GridGrouperServiceBean bean = new GridGrouperServiceBean(mockGridGrouper, TEST_GRID_GROUPER_STEM, TEST_GRID_GROUPER_URL);
    
    @Before
    public void setUp() throws ResourcePropertyRetrievalException, MalformedURIException, RemoteException {
        setUpGroupMemberships();
    }

    private void setUpGroupMemberships() throws GridGrouperRuntimeFault, InsufficientPrivilegeFault, RemoteException {
        GroupDescriptor investigatorGroup = new GroupDescriptor();
        investigatorGroup.setName(TEST_GRID_GROUPER_STEM + ":" +TEST_USER_IS_MEMBER_OF_GROUP);
        when(mockGridGrouper.getMembersGroups(eq(TEST_FULLY_QUALIFIED_USERNAME), any(MembershipType.class))).thenReturn(new GroupDescriptor[] {investigatorGroup});
    }

    @Test
    public void testGetGroupNames() throws GridInvocationException {
        Set<String> groupNames = bean.getGroupNames(TEST_FULLY_QUALIFIED_USERNAME);
        assertEquals(1, groupNames.size());
        assertTrue(groupNames.contains(TEST_USER_IS_MEMBER_OF_GROUP));
    }

    @Test
    public void testGetGroupNamesNoMemberships() throws GridGrouperRuntimeFault, InsufficientPrivilegeFault, RemoteException, GridInvocationException {
        when(mockGridGrouper.getMembersGroups(TEST_FULLY_QUALIFIED_USERNAME, MembershipType.Any)).thenReturn(null);
        Set<String> groupNames = bean.getGroupNames(TEST_FULLY_QUALIFIED_USERNAME);
        assertNotNull(groupNames);
        assertTrue(groupNames.isEmpty());
    }

    @Test(expected = GridInvocationException.class)
    public void testGetGroupNamesRuntimeFault() throws GridGrouperRuntimeFault, InsufficientPrivilegeFault, RemoteException, GridInvocationException  {
        when(mockGridGrouper.getMembersGroups(TEST_FULLY_QUALIFIED_USERNAME, MembershipType.Any)).thenThrow(new GridGrouperRuntimeFault());
        bean.getGroupNames(TEST_FULLY_QUALIFIED_USERNAME);
    }

    @Test(expected = GridInvocationException.class)
    public void testGetGroupNamesInsufficientPrivelegFault() throws GridGrouperRuntimeFault, InsufficientPrivilegeFault, RemoteException, GridInvocationException  {
        when(mockGridGrouper.getMembersGroups(TEST_FULLY_QUALIFIED_USERNAME, MembershipType.Any)).thenThrow(new InsufficientPrivilegeFault());
        bean.getGroupNames(TEST_FULLY_QUALIFIED_USERNAME);
    }

    @Test(expected = GridInvocationException.class)
    public void testGetGroupNamesRemoteException() throws GridGrouperRuntimeFault, InsufficientPrivilegeFault, RemoteException, GridInvocationException {
        when(mockGridGrouper.getMembersGroups(TEST_FULLY_QUALIFIED_USERNAME, MembershipType.Any)).thenThrow(new RemoteException());
        bean.getGroupNames(TEST_FULLY_QUALIFIED_USERNAME);
    }

    @Test
    public void testAddGridUserToGroup() throws GridInvocationException, GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
        bean.addGridUserToGroup(TEST_FULLY_QUALIFIED_USERNAME, "group");
        ArgumentCaptor<GroupIdentifier> groupIdentifierCaptor = ArgumentCaptor.forClass(GroupIdentifier.class);
        verify(mockGridGrouper).isMemberOf(groupIdentifierCaptor.capture(), eq(TEST_FULLY_QUALIFIED_USERNAME), eq(MemberFilter.All));
        GroupIdentifier groupIdentifier = groupIdentifierCaptor.getValue();
        assertEquals(TEST_GRID_GROUPER_STEM + ":group", groupIdentifier.getGroupName());
        assertEquals(TEST_GRID_GROUPER_URL, groupIdentifier.getGridGrouperURL());
        verify(mockGridGrouper).addMember(groupIdentifier, TEST_FULLY_QUALIFIED_USERNAME);
    }

    @Test
    public void testAddGridUserToGroupExistingMember() throws GridInvocationException, GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
        when(mockGridGrouper.isMemberOf(any(GroupIdentifier.class), anyString(), any(MemberFilter.class))).thenReturn(true);
        bean.addGridUserToGroup(TEST_FULLY_QUALIFIED_USERNAME, "group");
        verify(mockGridGrouper, never()).addMember(any(GroupIdentifier.class), eq(TEST_FULLY_QUALIFIED_USERNAME));
    }

    @Test(expected = GridInvocationException.class)
    public void testAddGridUserToGroupRemoteException() throws GridInvocationException, GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
        when(mockGridGrouper.isMemberOf(any(GroupIdentifier.class), anyString(), any(MemberFilter.class))).thenThrow(new RemoteException());
        bean.addGridUserToGroup(TEST_FULLY_QUALIFIED_USERNAME, "group");
    }

    @Test
    public void testRemoveGridUserFromGroup() throws GridInvocationException, GridGrouperRuntimeFault, InsufficientPrivilegeFault, GroupNotFoundFault, MemberDeleteFault, RemoteException {
        when(mockGridGrouper.isMemberOf(any(GroupIdentifier.class), eq(TEST_FULLY_QUALIFIED_USERNAME), eq(MemberFilter.ImmediateMembers))).thenReturn(true);
        bean.removeGridUserFromGroup(TEST_FULLY_QUALIFIED_USERNAME, TEST_USER_IS_MEMBER_OF_GROUP);
        ArgumentCaptor<GroupIdentifier> identifierCaptor = ArgumentCaptor.forClass(GroupIdentifier.class);
        verify(mockGridGrouper).deleteMember(identifierCaptor.capture(), eq(TEST_FULLY_QUALIFIED_USERNAME));
        assertEquals(TEST_GRID_GROUPER_STEM + ":" + TEST_USER_IS_MEMBER_OF_GROUP, identifierCaptor.getValue().getGroupName());
    }

    @Test
    public void testRemoveGridUserFromGroupNotMember() throws GridInvocationException, GridGrouperRuntimeFault, InsufficientPrivilegeFault, GroupNotFoundFault, MemberDeleteFault, RemoteException {
        bean.removeGridUserFromGroup(TEST_FULLY_QUALIFIED_USERNAME, TEST_USER_IS_NOT_MEMBER_OF_GROUP);
        verify(mockGridGrouper, never()).deleteMember(any(GroupIdentifier.class), eq(TEST_FULLY_QUALIFIED_USERNAME));
    }
    
    @Test
    public void testDoesGroupExist() throws GridInvocationException, GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
        String existingGroupName = "group";
        String absentGroupName = "no_such_group";
        GroupDescriptor groupDescriptor = new GroupDescriptor();
        groupDescriptor.setExtension(existingGroupName);
        GroupDescriptor[] groupDescriptors = new GroupDescriptor[] {groupDescriptor};
        when(mockGridGrouper.getChildGroups(any(StemIdentifier.class))).thenReturn(groupDescriptors);
        assertTrue(bean.doesGroupExist(existingGroupName));    
        assertFalse(bean.doesGroupExist(absentGroupName));    
    }


    @Test(expected = GridInvocationException.class)
    public void testDoesGroupExist_RemoteException() throws GridInvocationException, GridGrouperRuntimeFault, GroupNotFoundFault, RemoteException {
        String baseGroupName = "group";
        when(mockGridGrouper.getChildGroups(any(StemIdentifier.class))).thenThrow(new RemoteException());
        bean.doesGroupExist(baseGroupName);
    }
    
    @Test
    public void testCreateGroup() throws GridInvocationException, GridGrouperRuntimeFault, GroupAddFault, InsufficientPrivilegeFault, RemoteException {
        String groupName = "group";
        String displayName = "FIREBIRD Group";
        bean.createGroup(groupName, displayName);
        ArgumentCaptor<StemIdentifier> stemCaptor = ArgumentCaptor.forClass(StemIdentifier.class);
        verify(mockGridGrouper).addChildGroup(stemCaptor.capture(), eq(groupName), eq(displayName));
        assertEquals(TEST_GRID_GROUPER_STEM, stemCaptor.getValue().getStemName());
        assertEquals(TEST_GRID_GROUPER_URL, stemCaptor.getValue().getGridGrouperURL());
    }
    
    @Test
    public void testAddGridGroupToGroup() throws RemoteException, GridInvocationException  {
        String groupName = "group";
        String groupUuid = "uuid";
        String parentGroupName = "parent";
        String expectedFullyQualifiedParentGroupName = TEST_GRID_GROUPER_STEM + ":" + parentGroupName;
        GroupDescriptor groupDescriptor = new GroupDescriptor();
        groupDescriptor.setUUID(groupUuid);
        when(mockGridGrouper.getGroup(any(GroupIdentifier.class))).thenReturn(groupDescriptor);
        bean.addGridGroupToGroup(groupName, parentGroupName);
        ArgumentCaptor<GroupIdentifier> groupIdentifierCaptor = ArgumentCaptor.forClass(GroupIdentifier.class);
        verify(mockGridGrouper).addMember(groupIdentifierCaptor.capture(), eq(groupUuid));
        assertEquals(expectedFullyQualifiedParentGroupName, groupIdentifierCaptor.getValue().getGroupName());
    }

    @Test(expected = GridInvocationException.class)
    public void testAddGridGroupToGroup_RemoteException() throws RemoteException, GridInvocationException {
        when(mockGridGrouper.getGroup(any(GroupIdentifier.class))).thenThrow(new RemoteException());
        String groupName = "group";
        String parentGroupName = "parent";
        bean.addGridGroupToGroup(groupName, parentGroupName);
    }

}
