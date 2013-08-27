<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h2>
    <fmt:message key="investigator.profile.subinvestigators"/>
</h2>
<s:url var="removeSubInvestigatorConfirmUrl" action="enterDeleteSubinvestigatorConfirm" namespace="/investigator/profile/associations/person/ajax" >
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<s:url var="removeIconUrl" value='/images/ico_delete.gif' />
<s:url namespace="/investigator/profile/associations/subinvestigators/ajax" action="enterSearch" var="addPersonUrl" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<sj:a openDialog="profileDialog" href="%{addPersonUrl}" cssClass="button" id="addPerson"><fmt:message key="button.add"/></sj:a>

<div class="clear"><br></div>

<table id="subInvestigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table shows the people who have been added as subinvestigators to the profile. It displays their
                name, email, mailing address, phone number, and a link to remove them.">
    <thead><tr>
        <th scope="col" width="250px"><div><fmt:message key="label.name" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.email" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.address" /></div></th>
        <th scope="col" width="150px"><div><fmt:message key="label.phone" /></div></th>
        <th scope="col" width="75px"><div><fmt:message key="label.remove" /></div></th>
    </tr></thead>
</table>

<script>
    $(document).ready(function () {
        function buildRemoveSubInvestigatorAssociationColumn(subinvestigator) {
            return ajaxImageFormatter(subinvestigator.id, {
                url: '${removeSubInvestigatorConfirmUrl}',
                imageUrl: '${removeIconUrl}',
                imageTitle: 'Remove SubInvestigator Association',
                paramName: 'personAssociationId',
                paramValue: 'id',
                action: 'removeAssociation',
                target: 'profileDialog'
            }, subinvestigator);
        }

        var dataRows = ${subInvestigatorsJson};
        $('#subInvestigatorsTable').dataTable({
            aaData: dataRows,
            bInfo: false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: true,
            sDom: 'lrtip',
            aoColumns: [
                {mDataProp: "person.sortableName", bUseRendered: false, fnRender: function (obj) {
                    return obj.aData.person.displayNameForList;
                }},
                {mDataProp: "person.email"},
                {mDataProp: "person.postalAddress", fnRender: function (obj) {
                    return __addressFormatter(obj.aData.person.postalAddress);
                }},
                {mDataProp: "person.phoneNumber"},
                {mDataProp: null, fnRender: function (obj) {
                    return buildRemoveSubInvestigatorAssociationColumn(obj.aData);
                }}
            ],
            fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function () {
                indicateLoading(false);
            }
        });
    });
</script>