<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="sponsor.browse.sponsor.delegates.title"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <sj:dialog id="delegateDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClose" resizable="false" onOpenTopics="dialogOpened"/>
            <table id="delegatesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table displays the all of the sponsor delegates for the organizations which you are a part of.
                            It displays the name of the person, the name of the sponsor organization, and a link which can be
                            used to remove that person's sponsor delegate privileges for that organization.">
                <thead><tr>
                    <th scope="col" width="250px"><div><fmt:message key="label.name" /></div></th>
                    <th scope="col" width="250px"><div><fmt:message key="label.sponsor.organization" /></div></th>
                    <th scope="col" width="75px"><div><fmt:message key="label.remove" /></div></th>
                </tr></thead>
            </table>
        </div>
    </div>
</div>
<s:url var="removeDelegateConfirmUrl" action="enterRemoveDelegateConfirm" namespace="/sponsor/representative/ajax" />
<s:url var="removeIconUrl" value='/images/ico_delete.gif' />
<script type="text/javascript">
    $(document).ready(function() {
      createDelegatesTable();

        $('#delegateDialog').bind("dialogclose", function(event, ui) {
            refreshPage(0, false);
        });
    });

    function createDelegatesTable() {
        var dataRows = ${sponsorDelegatesJson};
        $('#delegatesTable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [
                {mDataProp: "sortableName", bUseRendered: false, fnRender: function (obj) {
                    return obj.aData.displayName;
                }},
                {mDataProp: "sponsor"},
                {mDataProp : null, fnRender : function(obj) {
                    return buildRemoveDelegateColumn(obj.aData);
                }}
            ],
            fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function() {
                indicateLoading(false);
            }
        });
    }

    function buildRemoveDelegateColumn(delegate) {
      return ajaxImageFormatter(delegate.id, {
        url:'${removeDelegateConfirmUrl}',
            imageUrl : '${removeIconUrl}',
            imageTitle : 'Remove Sponsor Delegate',
            paramName:'sponsorDelegateRoleId',
            paramValue: 'id',
            action: 'removeDelegate',
            target: 'delegateDialog'
            }, delegate);
    }
</script>