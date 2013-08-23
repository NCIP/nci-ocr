<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeSubinvestigatorUrl" namespace="/investigator/profile/associations/person/ajax" action="removeSubinvestigator" escapeAmp="false" >
    <s:param name="profile.id" value="%{profile.id}" />
    <s:param name="personAssociationId" value="%{personAssociationId}" />
</s:url>

<firebird:confirmDialog titleKey="remove.person.association.confirm.title" confirmUrl="${removeSubinvestigatorUrl}" dialogId="profileDialog">
    <h3>
        <fmt:message key="remove.subinvestigator.confirm.message.header">
            <fmt:param>${personAssociation.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
    <table id="registrationsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table shows the protocols which the requested subinvestigator to be removed is a part of. It 
                    displays the protocol title, the protocol ID, the registration packet status, and if the person 
                    association will be removed from the registration.">
        <thead><tr>
            <th scope="col" width="75px"><div><fmt:message key="remove.person.association.sponsor.protocol.title.header" /></div></th>
            <th scope="col" width="75px"><div><fmt:message key="remove.person.association.sponsor.protocol.id.header" /></div></th>
            <th scope="col" width="75px"><div><fmt:message key="remove.person.association.registration.packet.status.header" /></div></th>
            <th scope="col" width="75px"><div><fmt:message key="remove.person.association.will.be.removed.header" /></div></th>
        </tr></thead>
    </table>
    <h3>
        <fmt:message key="remove.subinvestigator.confirm.message.footer">
            <fmt:param>${personAssociation.person.displayName}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>
<script>
    $(document).ready(function() {
      createRegistrationsTable();
    });

    function createRegistrationsTable() {
        var dataRows = ${subinvestigatorRegistrationsJson};
        $('#registrationsTable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            oLanguage: { sEmptyTable: '<fmt:message key="remove.subinvestigator.confirm.empty.table.message"/>' },
            aoColumns: [
                {mDataProp: "protocolTitle"},
                {mDataProp: "protocolNumber"},
                {mDataProp: "status"},
                {mDataProp: "willBeRemoved"}
            ],
            fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function() {
                indicateLoading(false);
            }
        });
    }
</script>