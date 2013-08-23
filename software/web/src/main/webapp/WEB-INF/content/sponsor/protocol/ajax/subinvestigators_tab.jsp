<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.treeTable.js"/>"></script>
<link href="<c:url value='/styles/jquery.treeTable.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/styles/jquery.treetable.theme.default.css'/>" rel="stylesheet" type="text/css" />

<h2><fmt:message key="sponsor.protocol.subinvestigators.title"/></h2>

<firebird:messages/>

<table id="gridtable_subinv" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table shows all of the subinvestigators which have been added by investigators to participate in this
                protocol. It shows the subinvestigator's name which can be clicked to view their registration, the
                name of the investigator who invited them which can be clicked to view their registration, and the
                subinvestigators registration status.">
    <thead>
        <tr>
            <th scope="col" width="275px"><div><fmt:message key="label.subinvestigator"/></div></th>
            <th scope="col" width="275px"><div><fmt:message key="label.investigator"/></div></th>
            <th scope="col" width="275px"><div><fmt:message key="label.registration.status"/></div></th>
        </tr>
    </thead>
</table>


<div class="clear"><br></div>

<script type="text/javascript">
    var registrations = ${registrationsAsJson};
    var submittedText = '<s:property value="%{getText(@gov.nih.nci.firebird.data.RegistrationStatus@SUBMITTED.key)}"/>';
    var completedText = '<s:property value="%{getText(@gov.nih.nci.firebird.data.RegistrationStatus@COMPLETED.key)}"/>';
    var inReviewText = '<s:property value="%{getText(@gov.nih.nci.firebird.data.RegistrationStatus@IN_REVIEW.key)}"/>';
    $(document).ready(function() {
        $('#gridtable_subinv').dataTable( {
            aaData : registrations,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aaSorting: [[1,'asc']],
            aoColumns: [{mDataProp: "investigator.sortableName", bUseRendered: false, fnRender : function(obj) {
                             var content = personFormatter(obj.aData.investigator);
                             content = linkFormatter(content,{
                                 paramName:'registration.id',
                                 paramValue: 'id',
                                 action : 'viewReg',
                                 url : '<s:url namespace="/sponsor/protocol/review" action="enter"/>'
                             }, obj.aData);

                             return content;
                          }},
                          {mDataProp: "primaryInvestigator.sortableName", bUseRendered: false, fnRender : function(obj) {
                             var content = personFormatter(obj.aData.primaryInvestigator);
                             content = linkFormatter(content,{
                                 paramName:'registration.id',
                                 paramValue: 'primaryRegistrationId',
                                 action : 'viewReg',
                                 url : '<s:url namespace="/sponsor/protocol/review" action="enter"/>'
                             }, obj.aData);

                             return content;
                          }},
                          {mDataProp: "statusText"}
                      ],
           fnSort: [ [1,'asc'], [0,'asc'] ],
           fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               if (aData.currentRegistrationId != null) {
                   $(nRow).addClass("child-of-" + aData.currentRegistrationId);
                 }
               return nRow;
           }
        } );

        $('#gridtable_subinv').treeTable({indent: 15});

    });
</script>