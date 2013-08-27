<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script type="text/javascript" src="<s:url value="/scripts/search_support.js"/>"></script>
<s:url var="searchUrl" action="ctep-investigator-search" namespace="/search"/>

<h1><fmt:message key="sponsor.browse.ctep.investigators.header"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:instructionBubble messageKey="sponsor.browse.ctep.investigators.instructions" />

            <firebird:label forId="investigatorSearchInput" messageKey="sponsor.browse.ctep.investigators.search.label"/>
            <input id="investigatorSearchInput" class="autosearch" onkeyup="investigatorSearch.searchKeyEvent();"/>

            <table id="investigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                    summary="This table will display the results of searches using the input above.
                            It displays the investigator name which can be clicked to view their profile, the user's email
                            address, the user's CTEP ID, a link to view their annual registrations, and what their current
                            NCI Investigator status is.">
                <thead>
                    <tr>
                        <th scope="col"><div><fmt:message key="label.investigator" /></div></th>
                        <th scope="col"><div><fmt:message key="label.email" /></div></th>
                        <th scope="col"><div><fmt:message key="label.nci.investigator.number" /></div></th>
                        <th scope="col"><div><fmt:message key="label.registrations" /></div></th>
                        <th scope="col"><div><fmt:message key="label.nci.investigator.status" /></div></th>
                    </tr>
                </thead>
            </table>
            <div class="clear"></div>
        </div>
    </div>
</div>

<s:url var="investigatorProfileUrl" action="home" namespace="/investigator/profile" />
<s:url var="investigatorRegistrationsUrl" action="enterBrowseAnnualRegistrations" namespace="/investigator/annual/registration" />
<script type="text/javascript">

var investigatorSearch = new SearchSupport(
        'investigatorSearchInput',
        'investigatorsTable',
        '<s:property value="#searchUrl"/>');

var page = {
    createInvestigatorColumn: function(investigator) {
      return linkFormatter(investigator.name,
            {
                "url" : '<s:property value="investigatorProfileUrl"/>',
                "paramName" : "profile.id",
                "paramValue" : "id",
                "action" : "profile"
            },
            investigator);
    },

    createRegistrationsColumn: function(investigator) {
      return linkFormatter('<fmt:message key="registration.coordinator.investigators.browse.table.column.annual.registrations.text" />',
                {"url":'<s:property value="investigatorRegistrationsUrl"/>',
                    "paramName":"profile.id",
                    "paramValue":"id",
                    "action":"enterBrowseAnnualRegistrations"},
                investigator);
    }
};

$(document).ready(function() {
    $('#investigatorsTable').dataTable( {
        aaData : [],
        bLengthChange: false,
        iDisplayLength : 5,
        bFilter: false,
        oLanguage: {
            sInfoEmpty : '<fmt:message key="no.matching.search.results"/>',
            sEmptyTable : ''
        },
        aoColumns:
        [
            {mDataProp: 'sortableName', bUseRendered: false, sWidth : 200, fnRender : function(obj) {
              return page.createInvestigatorColumn(obj.aData);
            }},
            {mDataProp: 'email'},
            {mDataProp: 'ctepId'},
            {mDataProp: null, bSortable: false, fnRender : function(obj) {
              return page.createRegistrationsColumn(obj.aData);
            }},
            {mDataProp: 'status'}
        ],
        fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            $(nRow).attr("id", aData.id);
            return nRow;
        },
       fnDrawCallback: function() {
           investigatorSearch.finishTable();
       }
    } );
});

</script>