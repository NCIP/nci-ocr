<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <head>
        <title>
            <fmt:message key="sponsor.protocol.browse"/>
        </title>
    </head>
    <body>
        <h1><fmt:message key="sponsor.protocol.browse" /></h1>
        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:instructionBubble messageKey="sponsor.protocol.tab.select.filter.instructions" />
                    <s:url var="selectLink" action="enterProtocol" namespace="/sponsor/protocol" />

                    <table id="protocolsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                            summary="This table lists all of the currently active protocols for different sponsor organizations
                                    which you have privileges. It displays the protocol ID which can be clicked to view
                                    further details, the title of the protocol, the name of the sponsoring organization,
                                    the list of the Lead Organizations and Principal Investigators, and the list of agents.">
                        <thead>
                            <tr>
                                <th scope="col" width="170px"><div><fmt:message key="textfield.protocol.number"/></div></th>
                                <th scope="col" width="225px"><div><fmt:message key="textfield.title"/></div></th>
                                <th scope="col" width="200px"><div><fmt:message key="dropdown.sponsor"/></div></th>
                                <th scope="col" width="200px"><div><fmt:message key="label.lead.organizations"/></div></th>
                                <th scope="col" width="250px"><div><fmt:message key="autocomplete.agents"/></div></th>
                            </tr>
                        </thead>
                    </table>

                    <script type="text/javascript">
                        $(document).ready(function() {
                            var dataRows = ${gridtableData};
                            var isLongData = dataRows.length > ${minPaginationResults};
                            $('#protocolsTable').dataTable( {
                                aaData : dataRows,
                                bInfo : isLongData,
                                bLengthChange: isLongData,
                                bPaginate: isLongData,
                                aoColumns: [{mDataProp: "protocolNumber", fnRender : function(obj) {
                                                return linkFormatter(obj.aData.protocolNumber,
                                                        {url : '<s:property value="selectLink"/>',
                                                         paramName : "protocol.id",
                                                         paramValue : "id"},
                                                         obj.aData);
                                            }},
                                            {mDataProp: "protocolTitle"},
                                            {mDataProp: "sponsor.name"},
                                            {mDataProp: null, fnRender : function(obj) {
                                                var leadOrganizationDisplay = "";
                                                if (obj.aData.leadOrganizations) {
                                                    _.each(obj.aData.leadOrganizations, function (item) {
                                                        var principalInvestigator = (item.principalInvestigator)
                                                                ? item.principalInvestigator.displayName
                                                                : "none";
                                                        if (leadOrganizationDisplay) {
                                                            leadOrganizationDisplay += "<br>"
                                                        }
                                                        leadOrganizationDisplay += "<b>" + item.organization.name + "</b>";
                                                        leadOrganizationDisplay += " (" + principalInvestigator + ")";
                                                    });
                                                }
                                                return leadOrganizationDisplay;
                                            }},
                                            {mDataProp : "agentListForDisplay"}
                                          ],
                               fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                                   $(nRow).attr("id", aData.id);
                                   return nRow;
                               }
                            } );
                        });
                    </script>
                </div>
            </div>
        </div>
    </body>

</html>