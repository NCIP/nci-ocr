<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:if test="!sponsorDelegateForRegistration">
    <div class="formcol">
        <s:if test="registration.approvable">
            <s:a id="approvePacketButton" href="#" cssClass="button"
                onclick="indicateLoading();approveRegistration();" ><fmt:message key="button.approve.registration"/></s:a>
        </s:if>
        <s:elseif test="%{registration.status != @gov.nih.nci.firebird.data.RegistrationStatus@APPROVED}">
            <firebird:instructionBubble messageKey="approve.registration.packet.disabled" />
            <s:a id="approvePacketButton" href="#" cssClass="disabledButton"
                onclick="return false;"><fmt:message key="button.approve.registration"/></s:a>
        </s:elseif>
    </div>
    <div class="clear"></div>
</s:if>
<h2>Registrations</h2>
<table id="registrationOverviewTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays an overview of the current state of this registration packet, displaying the investigator
                and all subinvestigator registrations. The table shows the Name of the investigator which can be clicked
                to view the registration details, whether it is an investigator or subinvestigator registration, the current
                status, and the status date.">
    <thead>
        <tr>
            <th scope="col" width="200px"><div>Investigator</div></th>
            <th scope="col" width="200px"><div>Type</div></th>
            <th scope="col" width="200px"><div>Status</div></th>
            <th scope="col" width="200px"><div>Status Date</div></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${registrationOverviewJson};
        $('#registrationOverviewTable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aaSorting : [[1, "asc"],[0, "asc"]],
            aoColumns: [{mDataProp: "investigatorSortableName", bUseRendered: false, fnRender : function(obj) {
                            var id = 'reg_' + obj.aData.id;
                            var onclick = "selectTab('tab_" + obj.aData.id + "')";
                            var content = '<a href="#" id="' + id + '" onclick="' + onclick + '">' + obj.aData.investigator + '</a>';
                            return content;
                          }},
                          {mDataProp: "type"},
                          {mDataProp: "status.display"},
                          {mDataProp: "statusDateDisplay", sType: "date" }
                      ],
           fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        } );
    });
    function approveRegistration() {
        var url = '<s:url namespace="/sponsor/protocol/review/ajax" action="approvePacket"/>';

        $.post(url, {'registration.id': ${registration.id}}, function(data) {
            reloadCurrentTab();
        });
        indicateLoading(false);
    }
</script>

