<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.treeTable.js"/>"></script>
<link href="<c:url value='/styles/jquery.treeTable.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/styles/jquery.treetable.theme.default.css'/>" rel="stylesheet" type="text/css" />

<s:set name="isSponsorRepresentative" value="%{sponsorRepresentative}"/>

<h2><fmt:message key="sponsor.protocol.investigators.title"/></h2>

<firebird:messages/>

<s:url namespace="/sponsor/protocol/ajax" action="enterComments" var="commentsUrl">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url namespace="/sponsor/representative/protocol/ajax" action="enterRemovePacket" var="removePacketUrl" />
<s:if test="#isSponsorRepresentative">
    <div>
        <s:url namespace="/sponsor/representative/protocol/ajax" action="enterAddInvestigator" var="addInvestigatorUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <sj:a openDialog="popUpDialog" href="%{addInvestigatorUrl}" cssClass="button" id="addInvestigator"><fmt:message key="button.add.investigator"/></sj:a>
        <s:url namespace="/sponsor/representative/protocol/ajax" action="enterInvite" var="inviteUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>
        <sj:a openDialog="popUpDialog" href="%{inviteUrl}" cssClass="button" id="invite"><fmt:message key="button.invite"/></sj:a>
    </div>
    <div class="clear"><br></div>
</s:if>
<table id="gridtable_inv" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table displays all of the investigators who have been invited to participate in this protocol and any
                revisions they have made. It shows the investigator's name which can be clicked to view their registration,
                their invitation status, their registrations status, a button to activate or deactivate the investigator,
                and a link which can be used to remove the investigator from this protocol.">
    <thead>
        <tr>
            <th scope="col" width="150px"><div><fmt:message key="label.investigator"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.invitation.status"/></div></th>
            <th scope="col" width="150px"><div><fmt:message key="label.registration.status"/></div></th>
            <th scope="col" width="120px"><%-- Activate / Deactivate Investigator --%></th>
            <th scope="col" width="70px"><div><fmt:message key="label.remove"/></div></th>
        </tr>
    </thead>
</table>

<div class="clear"><br></div>

<script type="text/javascript">

    var registrations = ${registrationsAsJson};
    var inactiveText = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INACTIVE.display"/>';
    $(document).ready(function() {
        $('#gridtable_inv').dataTable( {
            aaData : registrations,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            bSort: false,
            aoColumns: [{mDataProp: "investigator", fnRender : function(obj) {
                             var content = personFormatter(obj.aData.investigator);
                             content = linkFormatter(content,{
                                 paramName:'registration.id',
                                 paramValue: 'id',
                                 action : 'viewReg',
                                 url : '<s:url namespace="/sponsor/protocol/review" action="enter"/>'
                             }, obj.aData);

                             return content;
                          }},
                          {mDataProp: "invitationStatusText"},
                          {mDataProp: "statusText"},
                          {mDataProp: null, fnRender : function(obj) {
                            return buildToggleRegistrationAccessColumn(obj.aData);
                           }},
                          {mDataProp: null, fnRender : function(obj) {
                            return buildRemoveColumn(obj.aData);
                           }}
                      ],
           "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               if (aData.currentRegistrationId != null) {
                 $(nRow).addClass("child-of-" + aData.currentRegistrationId);
               }
               return nRow;
           }
        } );

        $('#gridtable_inv').treeTable({indent: 15});

    });

    function buildToggleRegistrationAccessColumn(investigatorRegistration) {
      if (investigatorRegistration.ableToBeReactivated) {
            var button = ajaxLinkFormatter("",{
                paramName:'registration.id',
                paramValue: 'id',
                action : 'activateBtn',
                url : '<s:url namespace="/sponsor/representative/protocol/ajax" action="enterReactivatePacket"/>',
                htmlClass : "button",
                text: "<fmt:message key='button.activate'/>",
                target : "popUpDialog"
            }, investigatorRegistration);
            return "<div class='marginTop_5 marginBottom_5'>" + button + "</div>"
      } else if (investigatorRegistration.ableToBeDeactivated) {
            var button = ajaxLinkFormatter("",{
                paramName:'registration.id',
                paramValue: 'id',
                action : 'deactivateBtn',
                url : '<s:url namespace="/sponsor/representative/protocol/ajax" action="enterDeactivatePacket"/>',
                htmlClass : "button",
                text: "<fmt:message key='button.deactivate'/>",
                target : "popUpDialog"
            }, investigatorRegistration);
        return "<div class='marginTop_5 marginBottom_5'>" + button + "</div>"
      }
      return "";
    }

    function buildRemoveColumn(investigatorRegistration) {
      if (investigatorRegistration.removable) {
        return ajaxLinkFormatter(createImage("ico_delete.gif", "Remove"),
                    {url: '<s:property value="removePacketUrl"/>',
                    paramName:'registration.id',
                    paramValue: 'id',
                    action: 'removePacket',
                    target: 'popUpDialog'},
                    investigatorRegistration);
      }
      return "";
    }
</script>