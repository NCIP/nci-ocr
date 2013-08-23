<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="selectLink" action="enterProtocol" namespace="/sponsor/protocol" />
<s:url var="exportProtocolsUrl" action="exportProtocols" />
<h1><fmt:message key="sponsor.protocol.export" /></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
        <firebird:messages/>
            <firebird:instructionBubble messageKey="sponsor.protocol.tab.select.filter.instructions" />
            <table id="exportProtocolsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                   summary="This table provides a list of current protocols in the system which can be selected to be exported.
                   Each row is shown with a checkbox to select that protocol, the protocol title, the protocol ID, the sponsor
                   organization name, the list of lead organizations and their principal investigators, and the protocol agents.">
                <thead>
                    <tr>
                        <th scope="col" width="10px"><div><input id="checkAll" type="checkbox" onclick="checkOrDeselectAllProtocols()" /></div></th>
                        <th scope="col" width="225px"><div><fmt:message key="textfield.title"/></div></th>
                        <th scope="col" width="170px"><div><fmt:message key="textfield.protocol.number"/></div></th>
                        <th scope="col" width="200px"><div><fmt:message key="protocol.sponsor"/></div></th>
                        <th scope="col" width="200px"><div><fmt:message key="label.lead.organizations"/></div></th>
                        <th scope="col" width="250px"><div><fmt:message key="protocol.agents"/></div></th>
                    </tr>
                </thead>
            </table>
            <s:form id="exportForm" action="exportProtocols" target="_new" />
            <s:a id="exportButton" cssClass="button" href="#"><fmt:message key="button.export.csv"/></s:a>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        completeExportProtocolsTable();
        addExportButtonClickHandler();
    });

    function completeExportProtocolsTable() {
        var dataRows = ${protocolsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#exportProtocolsTable').dataTable( {
            aaData : dataRows,
            bInfo : isLongData,
            bLengthChange: isLongData,
            bPaginate: isLongData,
            aoColumns: [
                          {mDataProp: null, bSortable: false, fnRender : function(obj) {
                              return buildCheckBoxColumn(obj.aData);
                          }},
                          {mDataProp: "title"},
                          {mDataProp: "protocolId", fnRender : function(obj) {
                            return linkFormatter(obj.aData.protocolId,
                                    {url : '<s:property value="selectLink"/>',
                                     paramName : "protocol.id",
                                     paramValue : "id"},
                                     obj.aData);
                          }},
                          {mDataProp: "sponsor"},
                          {mDataProp: "leadOrganizations"},
                          {mDataProp : "agents"}
                      ],
           fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        } );
    }

    function buildCheckBoxColumn(protocol) {
        var checkboxId = "selectProtocol_" + protocol.id;
        return createHiddenCheckboxLabel(checkboxId) + "<input type='checkbox' id='" + checkboxId + "' name='selectedProtocolIds' value='" + protocol.id + "' onclick='uncheckSelectAllCheckBox()'/>";
    }

    function addExportButtonClickHandler() {
        $("#exportButton").click( function(){
            clearMessages();
            if (!areProtocolsSelected()) {
              setPageErrorMessages('<fmt:message key="sponsor.protocol.export.empty"/>');
              return;
            }
            addSelectedProtocolsToForm();
            $("#exportForm").submit();
            $('#exportForm').empty();
            indicateLoading(false);
        });
    }

    function areProtocolsSelected() {
        return getAllCheckboxes().is(':checked');
    }

    function getAllCheckboxes() {
        var table = $('#exportProtocolsTable').dataTable();
        return $(":checkbox", table.fnGetNodes());
    }

    function addSelectedProtocolsToForm() {
        var $exportForm = $('#exportForm');
        getAllCheckboxes().filter(':checked').each(function(index) {
            var id = $(this).attr('value');
            var input = $("<input>").attr("type", "hidden").attr("name", "selectedProtocolIds").val(id);
            $exportForm.append(input);
        });
    }

    function checkOrDeselectAllProtocols() {
        if ($("#checkAll").is(':checked')) {
          getAllCheckboxes().attr('checked','checked');
        } else {
          getAllCheckboxes().removeAttr('checked');
        }
    }

    function uncheckSelectAllCheckBox() {
        $("#checkAll").removeAttr('checked');
    }
</script>