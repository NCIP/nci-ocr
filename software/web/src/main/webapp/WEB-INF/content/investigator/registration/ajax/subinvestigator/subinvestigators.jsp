<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h2 class="clear">
    <fmt:message key="registration.subinvestigator.title" />
</h2>
<firebird:messages />

<div class="clear">
    <br />
</div>
<s:url var="listFromProfileUrl" action="listFromProfile" namespace="/investigator/registration/ajax/subinvestigator">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url namespace="/investigator/registration/subinvestigator/associations/person/ajax"
    action="enterSearch" var="addNewUrl" escapeAmp="false">
    <s:param name="profile.id" value="profile.id" />
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url namespace="/investigator/registration/ajax/subinvestigator" action="enterInvite" var="enterInviteUrl">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<div>
    <s:if test="%{!readOnly}">
        <sj:a openDialog="registrationDialog" href="%{listFromProfileUrl}" cssClass="button"
            id="listSubinvestigatorFromProfile"><fmt:message key="button.add.from.profile"/></sj:a>
        <sj:a openDialog="registrationDialog" href="%{addNewUrl}" cssClass="button"
            id="addNewSubinvestigator"><fmt:message key="button.add.new"/></sj:a>
        <sj:a openDialog="registrationDialog" href="%{enterInviteUrl}" cssClass="button"
            id="enterInviteSubinvestigator"><fmt:message key="button.invite"/></sj:a>
    </s:if>
</div>

<div class="clear">
    <br />
</div>
<s:url var="deleteIconUrl" value='/images/ico_delete.gif' />
<s:url var="deleteUrl" action="confirmDelete" namespace="/investigator/registration/ajax/subinvestigator">
    <s:param name="registration.id" value="%{registration.id}" />
</s:url>

<table id="subinvestigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table will display all of the subinvestigators that you have added to your registration. It displays
                an indicator if this subinvestigator has validation errors, the name of the subinvestigator, their email
                address, their phone number, what their invitation status is, what their registration status is, and a link
                which can be used to remove the subinvestigator from your registration packet.">
    <thead>
        <tr>
            <th scope="col" width="5px">&nbsp;</th>
            <th scope="col" width="175px"><div><fmt:message key="label.name"/></div></th>
            <th scope="col" width="175px"><div><fmt:message key="label.email"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.phone"/></div></th>
            <th scope="col" width="125px"><div><fmt:message key="label.invitation.status"/></div></th>
            <th scope="col" width="125px"><div><fmt:message key="label.registration.status"/></div></th>
            <th scope="col" width="30px">&nbsp;</th>
        </tr>
    </thead>
</table>

<firebird:nextTabButton />

<script type='text/javascript'>
    $(document).ready(function() {
        var registrationsAsJson = ${registrationsAsJson};
        $('#subinvestigatorsTable').dataTable( {
            aaData : registrationsAsJson,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                        {mDataProp: null, fnRender : function(obj) {
                            return createSubInvestigatorValidationErrorColumn(obj.aData);
                        }},
                        {mDataProp: "investigator.sortableName", bUseRendered: false, fnRender : function(obj) {
                             return personFormatter(obj.aData.investigator);
                        }},
                        {mDataProp: "investigator.email"},
                        {mDataProp: "investigator.phoneNumber"},
                        {mDataProp: "invitationStatusText"},
                        {mDataProp: "statusText"},
                        {mDataProp: null, fnRender : function(obj) {
                            return buildRemoveSubInvestigatorColumn(obj.aData);
                        }}
                      ],
           fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        });
    });

    function createSubInvestigatorValidationErrorColumn(registration) {
      var invalidSubinvestigatorIds = ${invalidSubinvestigatorIds};
      if (_.contains(invalidSubinvestigatorIds, registration.investigator.id)) {
        return "<span class='validationErrorAsterisk'>*</span>";
      }
        return "";
    }

    function buildRemoveSubInvestigatorColumn(registration) {
        if (!${registration.lockedForInvestigator}) {
            return ajaxImageFormatter(registration.id, {
                url:'${deleteUrl}',
                imageUrl : '${deleteIconUrl}',
                imageTitle : 'Delete',
                paramName:'subinvestigatorRegistration.id',
                paramValue: 'id',
                action: 'delete',
                target: 'registrationDialog'
                }, registration);
        } else {
            return "";
        }
    }
</script>
