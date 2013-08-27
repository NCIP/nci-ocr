<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/registration/ajax" action="enterComments" var="commentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url var="initiateRegistrationRevisionUrl" action="initiateRegistrationRevision" escapeAmp="false">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<s:url var="cancelRegistrationRevisionUrl" action="cancelRegistrationRevision" escapeAmp="false">
    <s:param name="registration.id" value="registration.id" />
</s:url>
<div>
    <h3 id="registrationStatus" class="clear">
        <fmt:message key="registration.status.label"/><fmt:message key="label.separator"/> <s:property value="%{registration.status.display}" />
    </h3>

    <div class="dotted_line"></div>

    <h2>
        <fmt:message key="registration.overview.forms.header"/>
    </h2>
    <firebird:messages/>

    <div>
        <s:if test="%{registration.submittable}">
            <s:if test="%{userAnApprovedCoordinatorForRegistrationsProfile}">
                <s:if test="%{!registration.lockedForInvestigator}">
                    <s:url var="submitToInvestigatorUrl" action="confirmSubmitToInvestigatorEnter" escapeAmp="false">
                        <s:param name="registration.id" value="registration.id" />
                        <s:param name="originalUpdateDate" value="registration.protocol.lastUpdate.time" />
                    </s:url>
                        <sj:a id="processRegistrationBtn" href="%{#submitToInvestigatorUrl}" cssClass="button blueButton"
                              openDialog="registrationDialog"><fmt:message key="button.submit.to.investigator"/></sj:a>
                </s:if>
            </s:if>
            <s:else>
                <s:url var="completeUrl" action="submitRegistration" escapeAmp="false">
                    <s:param name="registration.id" value="registration.id" />
                    <s:param name="originalUpdateDate" value="registration.protocol.lastUpdate.time" />
                </s:url>
                    <sj:a id="processRegistrationBtn" openDialog="registrationDialog" href="%{#completeUrl}"
                        cssClass="button blueButton"><fmt:message key="button.submit.for.review"/></sj:a>
            </s:else>
        </s:if><s:elseif test="%{registration.revisable}">
                <s:a id="initiateRegistrationRevisionButton" href="javascript:void(0)" onclick="initiateRegistrationRevision();" cssClass="button blueButton">
                    <fmt:message key="button.revise"/>
                </s:a>
        </s:elseif>
        <s:if test="%{registration.cancelable}">
                <s:a id="cancelRegistrationRevisionButton" href="javascript:void(0)" onclick="cancelRegistrationRevision();" cssClass="button blueButton">
                    <fmt:message key="button.cancel.revision"/>
                </s:a>
        </s:if>
    </div>
    <firebird:registrationComments registration="${registration}" retainState="true"/>

    <div class="line"></div>

    <table id="registrationFormsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table shows the forms that are part of this registration. It displays the name of the form which
                    can be clicked to go to the corresponding form tab, if the form is required or optional, when the form's
                    data was last updated, what the form's status is, and a link will display if any comments exist on the form.">
        <thead>
            <tr>
                <th scope="col" width="340px"><div><fmt:message key="registration.review.forms.form.name.column.header"/></div></th>
                <th scope="col" width="90px"><div><fmt:message key="registration.review.forms.optionality.column.header"/></div></th>
                <th scope="col" width="135px"><div><fmt:message key="registration.review.forms.last.updated.column.header"/></div></th>
                <th scope="col" width="100px"><div><fmt:message key="registration.review.forms.status.column.header"/></div></th>
                <th scope="col" width="20px"><%-- Comments column --%></th>
            </tr>
        </thead>
    </table>

    <script type="text/javascript">
    var incompleteText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@INCOMPLETE.display"/>';
    var completeText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@COMPLETED.display"/>';
    var inReviewText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@IN_REVIEW.display"/>';
    var acceptedText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED.display"/>';
        $(document).ready(function() {
          buildRegistrationFormsTable();
          $('#registrationDialog').bind("dialogclose", function(event, ui) {
            _viewReg.clearTimers(); //defined on view_registration.jsp
          });
        });

        function buildRegistrationFormsTable() {
          var dataRows = ${registrationFormsJson};
            var isLongData = dataRows.length > ${minPaginationResults};
            $('#registrationFormsTable').dataTable( {
                aaData : dataRows,
                bInfo : isLongData,
                bLengthChange: isLongData,
                bPaginate: isLongData,
                bFilter: isLongData,
                aaSorting: [[1,'asc'], [0,'asc']],
                aoColumns: [  {mDataProp: "description", fnRender : function(obj) {
                               return buildFormTitleColumn(obj.aData);
                              }},
                              {mDataProp: "optionality"},
                              {mDataProp: "modificationDate", sDefaultContent: ""},
                              {mDataProp: "status", fnRender : function(obj) {
                               return buildFormStatusColumn(obj.aData);
                              }},
                              {mDataProp: "comments", fnRender : function(obj) {
                               return buildFormCommentsColumn(obj.aData);
                              }}
                          ],
               fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                   $(nRow).attr("id", aData.id);
                   return nRow;
               }
            } );
        }

        function buildFormTitleColumn(form) {
          var options = {tab : 'name'};
          return tabLinkFormatter(form.description, options, form);
        }

        function buildFormStatusColumn(form) {
          if (form.status == incompleteText) {
                var cssClass = 'errorMessage';
          } else if (form.status == completeText) {
                var cssClass = 'validMessage';
          } else {
            return form.status
          }
          return "<div class='" + cssClass + "'>" + form.status + "</div>";
        }

        function buildFormCommentsColumn(form) {
          if (form.status == inReviewText || form.status == acceptedText) {
            return "";
          }
          var options = {url:'${commentsUrl}', dialog: 'registrationDialog'};
          return commentDisplayFormatter(form.comments, options, form);
        }

        function initiateRegistrationRevision() {
          var url = "${initiateRegistrationRevisionUrl}";
          $.post(url, null, function(data) {
             window.location.reload();
          });
        }

        function cancelRegistrationRevision() {
          var url = "${cancelRegistrationRevisionUrl}";
          $.post(url, null, function(data) {
             window.location.reload();
          });
        }

    </script>
</div>