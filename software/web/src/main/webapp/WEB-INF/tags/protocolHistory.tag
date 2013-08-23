<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="protocol" type="gov.nih.nci.firebird.data.Protocol" rtexprvalue="true" required="true" %>
<%@ attribute name="isSponsor" type="java.lang.Boolean" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div class="clear"></div>
<div class="text_column"><firebird:label forId="revisionDateSelect" messageKey="label.protocol.change.log" bold="true"/></div>
<div class="clear"></div>

<div id="revisionDiv">
    <select id="revisionDateSelect"> </select>

    <div id="revisionComments">
        <firebird:comments/>
    </div>

    <br>

    <table id="revisionTable" width="80%" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table shows the changes that took place in the selected revision.">
        <thead>
            <tr>
                <th scope="col"><div><fmt:message key="label.changes"/></div></th>
            </tr>
        </thead>
    </table>
</div>

<script>
    var protocolRevisions = ${revisionHistoryJson};

    $(document).ready(function () {
        $('#revisionTable').dataTable({
            bInfo: false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false
        });

        if (protocolRevisions && protocolRevisions.length > 0) {
            $('#revisionDateSelect').change(function () {
                selectDate();
            });

            for (var revisionIndex in protocolRevisions) {
                var revision = protocolRevisions[revisionIndex];
                $('<option></option>').val(revision.id).text(formatDate(revision.date, true)).appendTo('#revisionDateSelect');
            }
            selectDate();
        } else {

            $('#revisionDateSelect, #revisionComments, #revisionTable').hide();
            $('#revisionDiv').append('<fmt:message key="registration.protocol.information.no.revisions"/>');
        }

    });

    function selectDate() {
        var revision = getRevisionById($('#revisionDateSelect').val());
        var dataTable = $('#revisionTable').dataTable();
        $('#revisionComments .richText').html(revision.comment);
        dataTable.fnClearTable();
        var isSponsor = '${isSponsor}';
        if (isSponsor) {
            showSponsorModificationDescriptions(dataTable, revision);
        } else {
            showInvestigatorModificationDescriptions(dataTable, revision);
        }
    }

    function getRevisionById(id) {
        for (revisionIndex in protocolRevisions) {
            var revision = protocolRevisions[revisionIndex];
            if (revision.id == id) {
                return revision;
            }
        }
    }

    function showSponsorModificationDescriptions(dataTable, revision) {
        for (var descriptionIndex in revision.sponsorModificationDescriptions) {
            var description = revision.sponsorModificationDescriptions[descriptionIndex];
            dataTable.fnAddData([description]); //expects an array of data
        }
    }

    function showInvestigatorModificationDescriptions(dataTable, revision) {
        for (var descriptionIndex in revision.investigatorModificationDescriptions) {
            var description = revision.investigatorModificationDescriptions[descriptionIndex];
            dataTable.fnAddData([description]); //expects an array of data
        }
    }

</script>
