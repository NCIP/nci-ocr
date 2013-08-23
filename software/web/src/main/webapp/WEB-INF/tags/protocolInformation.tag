<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="protocol" type="gov.nih.nci.firebird.data.Protocol" rtexprvalue="true" required="true" %>

<div class="text_column"><firebird:label messageKey="protocol.protocolTitle" bold="true"/></div>
<br>
<div id="protocolTitle" class="text_value_column">${protocol.protocolTitle}</div>
<div class="clear"></div>

<div class="text_column"><firebird:label messageKey="protocol.protocolNumber" bold="true"/></div>
<br>
<div id="protocolNumber" class="text_value_column">${protocol.protocolNumber}</div>
<div class="clear"></div>

<div class="text_column"><firebird:label messageKey="protocol.sponsor" bold="true"/></div>
<br>
<div id="protocolSponsor" class="text_value_column">${protocol.sponsor.name}</div>
<div class="clear"></div>

<div class="text_column"><firebird:label messageKey="protocol.phase" bold="true"/></div>
<br>
<div id="protocolPhase" class="text_value_column">${protocol.phase.display}</div>
<div class="clear"></div>

<table id="protocolLeadOrganizations" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays the Lead Organizations and their corresponding Principal Investigators of the Protocol.">
    <thead>
    <tr>
        <th scope="col" width="250px"><div><fmt:message key="label.lead.organization"/></div></th>
        <th scope="col" width="250px"><div><fmt:message key="label.principal.investigator"/></div></th>
    </tr>
    </thead>
</table>
<div class="clear"></div>


<div class="text_column"><firebird:label messageKey="protocol.agents" bold="true"/></div>
<br>
<div id="protocolAgents" class="text_value_column">${protocol.agentListForDisplay}</div>
<div class="clear"></div>
<script>
    $(document).ready(function () {
        var dataRows = <s:property value="%{@gov.nih.nci.firebird.web.common.ProtocolUiUtils@getProtocolLeadOrganizationsJson(#attr.protocol)}" escapeHtml="false"/>;
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#protocolLeadOrganizations').dataTable({
            "aaData": dataRows,
            "bInfo": isLongData,
            "bLengthChange": isLongData,
            "bPaginate": isLongData,
            "bFilter": isLongData,
            "aoColumns": [
                {mDataProp: "organization.name"},
                {mDataProp: null, fnRender : function(obj) {
                    return  (obj.aData.principalInvestigator)
                            ? obj.aData.principalInvestigator.displayName
                            : "None";
                }}
            ],
            "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            }
        });
    });
</script>
