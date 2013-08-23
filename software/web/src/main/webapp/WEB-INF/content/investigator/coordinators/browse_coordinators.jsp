<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="regUrl" action="enterRegistrations" namespace="/investigator/registration">
    <s:param name="profile.id" value="profile.id" />
</s:url>
<h1><fmt:message key="investigator.browse.coordinators.title"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <sj:dialog id="coordinatorDialog" autoOpen="false" modal="true" width="950" position="top" resizable="false" onOpenTopics="dialogOpened" />
            <table id="coordinatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table displays all of the people who are currently acting as registration coordinators
                            for you. It displays the coordinator's name, a button to enable or disable the coordinator's
                            access to your profile, a button to enable or disable the coordinators access to your
                            registrations, and a button to allow you to remove their access entirely.">
                <thead>
                    <tr>
                        <th scope="col" width="250px">
                            <div>
                                <fmt:message key="investigator.browse.coordinators.coordinator.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="investigator.browse.coordinators.suspend.profile.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="investigator.browse.coordinators.suspend.registrations.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="75px">
                            <div>
                                <fmt:message key="investigator.browse.coordinators.remove.column.header" />
                            </div>
                        </th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<s:url var="removeCoordinatorConfirmUrl" action="removeCoordinatorConfirmEnter" namespace="/investigator/coordinators/ajax" />
<s:url var="unsuspendProfileConfirmUrl" action="unsuspendCoordinatorFromProfileConfirmEnter" namespace="/investigator/coordinators/ajax" />
<s:url var="suspendProfileConfirmUrl" action="suspendCoordinatorFromProfileConfirmEnter" namespace="/investigator/coordinators/ajax" />
<s:url var="unsuspendRegistrationsConfirmUrl" action="unsuspendCoordinatorFromRegistrationsConfirmEnter" namespace="/investigator/coordinators/ajax" />
<s:url var="suspendRegistrationsConfirmUrl" action="suspendCoordinatorFromRegistrationsConfirmEnter" namespace="/investigator/coordinators/ajax" />
<script type="text/javascript">
    $(document).ready(function() {
        createCoordinatorsTable();
    });

    function createCoordinatorsTable() {
      var dataRows = ${registrationCoordinatorsJson};
        $('#coordinatorsTable').dataTable( {
          aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "sortableName", bUseRendered: false, fnRender: function (obj) {
                    return obj.aData.displayName;
                }},
                {mDataProp : null, fnRender : function(obj) {
                  return buildSuspendProfileColumn(obj.aData);
              }},
                {mDataProp : null, fnRender : function(obj) {
                  return buildSuspendRegistrationsColumn(obj.aData);
              }},
                {mDataProp : null, fnRender : function(obj) {
                  return buildRemoveCoordinatorColumn(obj.aData);
              }}
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

    function buildSuspendProfileColumn(coordinator) {
      if (coordinator.suspendedProfileAccess) {
            var id = "activateProfileButton." + coordinator.id;
            var href = "${unsuspendProfileConfirmUrl}";
            var buttonText = "<fmt:message key='button.activate'/>";
            var cssClass = "activateButton";
        }
        else {
            var id = "suspendProfileButton." + coordinator.id;
            var href = "${suspendProfileConfirmUrl}";
            var buttonText = "<fmt:message key='button.suspend'/>";
            var cssClass = "suspendButton";
         }
         return buildButton(id, href, buttonText, coordinator.id, cssClass);
    }

    function buildSuspendRegistrationsColumn(coordinator) {
        if (coordinator.suspendedRegistrationAccess) {
            var id = "activateRegistrationsButton." + coordinator.id;
            var href = "${unsuspendRegistrationsConfirmUrl}";
            var buttonText = "<fmt:message key='button.activate'/>";
            var cssClass = "activateButton";
         }
        else {
           var id = "suspendRegistrationsButton." + coordinator.id;
           var href = "${suspendRegistrationsConfirmUrl}";
           var buttonText = "<fmt:message key='button.suspend'/>";
           var cssClass = "suspendButton";
         }
         return buildButton(id, href, buttonText, coordinator.id, cssClass);
    }

    function buildButton(id, href, buttonText, managedInvestigatorId, cssClass) {
         href += "?managedInvestigatorId=" + managedInvestigatorId;
         var content = '<a id="' + id + '" href="javascript:void(0)" class="button ' + cssClass + '" onclick="openDialog(\'' + href + '\');">';
         content += buttonText + "</a>"
         return content;
    }

    function openDialog(url) {
        var target = '#coordinatorDialog'
        $(target).dialog('open');
        _fbUtil.performAjaxPost(url, target, null);
    }

    function buildRemoveCoordinatorColumn(coordinator) {
        var imageName='ico_delete.gif';
        return ajaxLinkFormatter(createImage(imageName, 'Remove Registration Coordinator'),
                {url: '${removeCoordinatorConfirmUrl}',
                    paramName:'managedInvestigatorId',
                    paramValue: 'id',
                    action: 'removeCoordinator',
                    target: 'coordinatorDialog'},
                coordinator);
    }

    function buildTitleColumn(registration) {
      return linkFormatter(registration.title,{
            paramName:'id',
            paramValue: 'id',
            action : 'enterRegistrations',
            url : '${regUrl}'
        }, registration);
    }
</script>
