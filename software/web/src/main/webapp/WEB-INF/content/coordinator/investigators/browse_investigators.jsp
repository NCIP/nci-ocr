<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h1><fmt:message key="registration.coordinator.investigators.browse.title" /></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <sj:dialog id="investigatorsDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />
            <s:url namespace="/coordinator/investigators/ajax" action="enterAddInvestigators" var="addInvestigatorUrl" />

            <firebird:instructionBubble messageKey="registration.coordinator.investigators.browse.profile.instructions"/>

            <sj:a openDialog="investigatorsDialog" href="%{#addInvestigatorUrl}" cssClass="button" id="addInvestigator"><fmt:message key="button.add.investigator"/></sj:a>
            <table id="investigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table shows all of the investigators which you coordinate for. It shows the investigator's
                            name, link(s) to their registrations, and your current status coordinating for them.">
                <thead>
                    <tr>
                        <th scope="col">
                            <div>
                                <fmt:message key="registration.coordinator.investigators.browse.table.column.investigator.header" />
                            </div>
                        </th>
                        <s:if test="%{registrationCoordinator}">
                            <th id="protocolRegistrationsHeader" scope="col">
                                <div>
                                    <fmt:message key="registration.coordinator.investigators.browse.table.column.registrations.header" />
                                </div>
                            </th>
                        </s:if>
                        <s:if test="%{ctepRegistrationCoordinator}">
                            <th id="annualRegistrationsHeader" scope="col">
                                <div>
                                    <fmt:message key="registration.coordinator.investigators.browse.table.column.annual.registrations.header"/>
                                </div>
                            </th>
                        </s:if>
                        <th scope="col">
                            <div>
                                <fmt:message key="registration.coordinator.investigators.browse.table.column.status.header" />
                            </div>
                        </th>
                    </tr>
               </thead>
            </table>
        </div>
    </div>
</div>

<s:url var="profileUrl" action="home" namespace="/investigator/profile" />
<s:url var="registrationsUrl" action="enterRegistrations" namespace="/investigator/registration" />
<s:url var="annualRegistrationsUrl" action="enterBrowseAnnualRegistrations" namespace="/investigator/annual/registration" />
<script>
    $(document).ready(function() {
        var dataRows = ${managedInvestigatorsJson};
        var isLongData = dataRows.length > ${minPaginationResults};

        $('#investigatorsTable').dataTable( {
            "aaData" : dataRows,
            "bInfo" : isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "aoColumns": [
                {mDataProp : "investigatorSortableName", bUseRendered: false, fnRender : function(obj) {
                    return buildInvestigatorNameColumn(obj.aData);
                }},
                <s:if test="%{registrationCoordinator}">
                {mDataProp : null, fnRender : function(obj) {
                    return buildRegistrationsColumn(obj.aData);
                }},
                </s:if>
                <s:if test="%{ctepRegistrationCoordinator}">
                {mDataProp:null, fnRender:function (obj) {
                    return buildAnnualRegistrationsColumn(obj.aData);
                }},
                </s:if>
                {"mDataProp": "status.display"}],
            "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });

    function buildInvestigatorNameColumn(investigatorListing) {
        if (!investigatorListing.approved || investigatorListing.suspendedFromProfile) {
            return investigatorListing.investigatorName;
        }
        else {
            return linkFormatter(investigatorListing.investigatorName,
                    {"url" : '<s:property value="profileUrl"/>',
                "paramName" : "profile.id",
                "paramValue" : "id",
                "action" : "profile"
                },
                investigatorListing);
        }
    }

    function buildRegistrationsColumn(investigator) {
        if (!investigator.approved || investigator.suspendedFromRegistrations) {
            return '<fmt:message key="registration.coordinator.investigators.browse.table.column.registrations.text" />';
        }
        else {
            return linkFormatter('<fmt:message key="registration.coordinator.investigators.browse.table.column.registrations.text" />',
                    {"url" : '<s:property value="registrationsUrl"/>',
                     "paramName" : "profile.id",
                     "paramValue" : "id",
                     "action" : "enterRegistrations"},
                     investigator);
        }
    }

    function buildAnnualRegistrationsColumn(investigator) {
        if (!investigator.approved || investigator.suspendedFromRegistrations) {
            return '<fmt:message key="registration.coordinator.investigators.browse.table.column.annual.registrations.text" />';
        }
        else {
            return linkFormatter('<fmt:message key="registration.coordinator.investigators.browse.table.column.annual.registrations.text" />',
                    {"url":'<s:property value="annualRegistrationsUrl"/>',
                        "paramName":"profile.id",
                        "paramValue":"id",
                        "action":"enterBrowseAnnualRegistrations"},
                    investigator);
        }
    }

</script>