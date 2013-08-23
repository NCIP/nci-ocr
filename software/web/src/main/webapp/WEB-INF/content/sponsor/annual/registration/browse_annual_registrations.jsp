<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="viewRegistrationUrl" action="enterReviewRegistration" namespace="/sponsor/annual/registration/review" />

<h1><fmt:message key="browse.annual.registration.submissions"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:instructionBubble messageKey="browse.annual.registrations.filter.instructions" />
            <firebird:label forId="filter" messageKey="browse.annual.registrations.filter.text"/>
            <select id="filter" onchange="updateFilter()"></select>
            <table id="registrationsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table provides a list of the annual submissions for an investigator. It displays the 
                            registration name which can be click to see more information, the registration type, the
                            registration status, the effective date, and the renewal date.">
                <thead>
                    <tr>
                        <th scope="col"><div><fmt:message key="label.name" /></div></th>
                        <th scope="col"><div><fmt:message key="label.type" /></div></th>
                        <th scope="col"><div><fmt:message key="label.status" /></div></th>
                        <th scope="col"><div><fmt:message key="label.effective.date" /></div></th>
                        <th scope="col"><div><fmt:message key="label.renewal.date" /></div></th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">

    var showAll = '<fmt:message key="browse.annual.registrations.filter.header"/>';

    $(document).ready(function() {
        createRegistrationsTable();
        populateStatusFilter();
    });

    function createRegistrationsTable() {
        var dataRows = ${registrationsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#registrationsTable').dataTable( {
            aaData : dataRows,
            bInfo : isLongData,
            bLengthChange: isLongData,
            bPaginate: isLongData,
            bFilter: true,
            aaSorting: [[0, 'asc']],
            aoColumns: [
                {mDataProp: 'sortableName', fnRender : function(obj) {
                    return createTitleColumn(obj.aData);
                }},
                {mDataProp: "type", bSearchable: false},
                {mDataProp: "status", bSearchable: false,
                  fnRender : function(obj) {
                    return "<span class='statusBullet statusBullet_sponsor_" + obj.aData.status._name + "'>&bull;</span>" +
                        "<span class='status'>" + obj.aData.status.display + "</span>";
                }},
                {mDataProp: "effectiveDate", "stype" : "date", bSearchable: false,
                    fnRender : function(obj) {
                    return formatDate(obj.aData.effectiveDate);
                }},
                {mDataProp: "renewalDate", "stype" : "date", bSearchable: false,
                    fnRender : function(obj) {
                    return formatDate(obj.aData.renewalDate);
                }},
               {mDataProp: 'ctepId', bVisible: false},
               {mDataProp: 'emailAddress', bVisible: false}
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

    function createTitleColumn(registration) {
      return linkFormatter(registration.name,
                {url : '<s:property value="viewRegistrationUrl"/>',
                 paramName : "registration.id",
                 paramValue : "id"},
                 registration);
    }

    function populateStatusFilter() {
      var filter = $("#filter");
      filter.append($("<option />").text(showAll));
      var statuses = [];
      var table = $('#registrationsTable').dataTable();
      $(".status", table.fnGetNodes()).each(function() {
        var status = $(this).text();
        if (_.indexOf(statuses, status) === -1) {
          statuses.push(status);
          filter.append($("<option />").text(status));
        }
      });
    }

    function updateFilter() {
        var selectedStatus = $("#filter option:selected").text();
        if (selectedStatus === showAll) {
          selectedStatus = "";
        }
        $("#registrationsTable").dataTable().fnFilter(selectedStatus, 2);
    }
</script>