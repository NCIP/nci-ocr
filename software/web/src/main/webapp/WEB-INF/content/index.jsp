<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <head>
        <title>
            <fmt:message key="firebird.welcome.message"/>
        </title>
    </head>
    <body id="sec">
        <!--Content-->
        <h1>
            <fmt:message key="firebird.welcome.message"/>
        </h1>
        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <sj:dialog id="taskDialog"
                        autoOpen="false"
                        modal="true"
                        width="920"
                        position="top"
                        resizable="false"
                        onCloseTopics="dialogClosed"
                        onOpenTopics="dialogOpened" />

                    <div class="clear"><br></div>

                    <s:url var="selectLink" action="enterProtocol" namespace="/sponsor/protocol" />
                    <s:url var="gridtableData" action="taskList" namespace="/" />

                    <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
                        <span class="ui-jqgrid-title"><fmt:message key="tasks.title"/> </span>
                    </div>
                    <table id="taskTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                            summary="This table displays the list of notifications which require attention. It displays
                                    the description of the task which can be clicked to either view more details or take you
                                    to the relevant page, and the date which the notification was created.">
                        <thead>
                            <tr>
                                <th scope="col"><div><fmt:message key="label.description"/></div></th>
                                <th scope="col" width="75px"><div><fmt:message key="label.date"/></div></th>
                            </tr>
                        </thead>
                    </table>
                    <div class="clear"></div>

                    <script>
                    function taskPopUp(actionUrl, actionId) {
                        $('#taskDialog').load('${pageContext.request.contextPath}' + actionUrl).dialog('open');
                    }

                    $(document).ready(function() {
                        var dataRows = ${taskRowsJSON};
                        var isLongData = dataRows.length > ${minPaginationResults};
                        $('#taskTable').dataTable( {
                            "aaData" : dataRows,
                            "bInfo" : isLongData,
                            "bLengthChange": isLongData,
                            "bPaginate": isLongData,
                            "aoColumns": [{"mDataProp": "description"},
                                          {"mDataProp": "dateAdded", "stype" : "date",
                                              "fnRender" : function(obj) {
                                              return formatDate(obj.aData.dateAdded, true);
                                          }}
                                      ],
                          "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                              var td = $("td", nRow).first();
                              var txt = td.text();
                              td.text('');
                              if(aData.modal) {
                                  $('<a>',{
                                      text: txt,
                                      id: aData.taskActionTargetId,
                                      href: '#',
                                      click: function() {
                                          taskPopUp(aData.taskActionUrl, aData.taskActionTargetId);
                                      }
                                  }).appendTo(td);
                              } else {
                                  $('<a>',{
                                      text: txt,
                                      id: aData.taskActionTargetId,
                                      href: "${pageContext.request.contextPath}" + aData.taskActionUrl
                                  }).appendTo(td);
                              }
                              return nRow;
                          }
                        });
                    });

                    </script>
                </div>
            </div>
        </div>
    </body>
</html>
