<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.treeTable.js"/>"></script>
<link href="<c:url value='/styles/jquery.treeTable.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/styles/jquery.treetable.theme.default.css'/>" rel="stylesheet" type="text/css" />

<s:url var="regUrl" action="enterRegistrations" namespace="/investigator/registration">
    <s:param name="profile.id" value="profile.id" />
</s:url>
<h1>
    <fmt:message key="registration.browse.title"/><fmt:message key="label.separator"/> <span class="gray">${profile.person.displayName}</span>
</h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <table id="registrationsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table displays all of the registrations which the investigator has been invited to. It
                            displays the title of the protocol, the protocol ID, the organization sponsoring the protocol,
                            if it is an investigator or subinvestigator registration, and the current status.">
                <thead>
                    <tr>
                        <th scope="col" width="250px">
                            <div>
                                <fmt:message key="registration.browse.protocol.title.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="registration.browse.protocol.id.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="150px">
                            <div>
                                <fmt:message key="registration.browse.protocol.sponsor.column.header" />
                            </div>
                        </th>
                        <th scope="col" width="75px">
                            <div>
                                <fmt:message key="label.type" />
                            </div>
                        </th>
                        <th scope="col" width="75px">
                            <div>
                                <fmt:message key="registration.browse.protocol.status.column.header" />
                            </div>
                        </th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        createRegistrationsTable();
    });

    function createRegistrationsTable() {
        var dataRows = ${registrations};
        $('#registrationsTable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aaSorting: [[3, 'asc'],[0, 'asc']],
            aoColumns: [
                {mDataProp : 'title', fnRender : function(obj) {
                    return buildTitleColumn(obj.aData);
                }},
                {mDataProp: "protocolNumber"},
                {mDataProp: "sponsor"},
                {mDataProp : null, fnRender : function(obj) {
                    if (obj.aData.investigatorRegistration) {
                        return '<fmt:message key="label.investigator"/>';
                    } else {
                        return '<fmt:message key="label.subinvestigator"/>';
                    }
                }},
                {mDataProp: "status"}
            ],
            fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                if (aData.currentRegistrationId != null) {
                   $(nRow).addClass("child-of-" + aData.currentRegistrationId);
                }
                return nRow;
            },
            fnInitComplete: function() {
                indicateLoading(false);
            }
        });

        $('#registrationsTable').treeTable({indent: 15});
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