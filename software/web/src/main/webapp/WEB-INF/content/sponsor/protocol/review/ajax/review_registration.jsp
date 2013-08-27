<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/sponsor/protocol" action="enterProtocol" var="protocolUrl">
    <s:param name="protocol.id">${registration.protocol.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="downloadForm" var="viewFormUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="commentsEnter" var="commentsUrl">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<s:url namespace="/sponsor/protocol/review/ajax" action="additionalDocumentsEnter" var="additionalDocumentsEnter">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>

<div>
    <h2>${headerTitle}</h2>
    <firebird:messages />
    <h3>Status: ${registration.status.display}</h3>
    <div class="dotted_line"></div>

    <div class="formcol_narrow">
        <s:if test="registration.completeable">
            <s:a id="completeReviewButton" href="#" cssClass="button" onclick="indicateLoading();_reviewPage.submitReview()"><fmt:message key="button.complete.review"/></s:a>
        </s:if>
        <s:else>
            <firebird:instructionBubble messageKey="complete.review.button.instructions.${registration.status}" />
            <s:a id="completeReviewButton" href="#" cssClass="disabledButton" onclick="return false;"><fmt:message key="button.complete.review"/></s:a>
        </s:else>
    </div>
    <firebird:registrationComments registration="${registration}" retainState="true"/>
    <div class="clear"><br/></div>
    <h2><fmt:message key="registration.review.forms.header" /></h2>
    <table id="registrationFormsTable_${registration.id}"
            class="ui-jqgrid-htable ui-jqgrid-btable registrationFormsTable"
            summary="This table displays the forms which are part of the investigator's registration. It displays the name
                    which can be clicked to review the PDF, the status of the form, the date the status was last updated, radio buttons
                    to accept or reject the form if it is in review, a link if there are comments on the form, and a link which can
                    be used to view additional documents which may be included with the form.">
        <thead>
            <tr>
                <th scope="col" width="250px">
                    <div>
                        <fmt:message key="registration.review.forms.form.name.column.header" />
                    </div>
                </th>
                <th scope="col" width="150px">
                    <div>
                        <fmt:message key="registration.review.forms.status.column.header" />
                    </div>
                </th>
                <th scope="col" width="150px">
                    <div>
                        <fmt:message key="registration.review.forms.last.updated.column.header" />
                    </div>
                </th>
                <th scope="col" width="150px"></th>
                <th scope="col" width="75px">
                    <div>
                        <fmt:message key="registration.review.forms.comments.column.header" />
                    </div>
                </th>
                <th class="ui-th-column" scope="col" width="75px">
                    <div>
                        <fmt:message key="registration.review.forms.additional.documents.column.header" />
                    </div>
                </th>
            </tr>
        </thead>
    </table>


    <script type="text/javascript">
        var _reviewPage = (function() {
            var reviewPage = {};
            var ACCEPTED = "accepted", REJECTED = "rejected", NONE = "none";

            reviewPage.acceptedState = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED" />';
            reviewPage.rejectedState = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@REJECTED" />';
            reviewPage.inReviewState = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@IN_REVIEW" />';

            reviewPage.submitFormReview = function(radioOption, currentFormValue) {
                var selectedFormValue = $(radioOption).val();
                var radioState = getRadioState(currentFormValue, selectedFormValue);
                var formId = getRadioRowId(radioOption);
                var url = getFormSubmissionUrl(radioState, formId);
                var onSuccess = getSuccessFunction(radioState, formId);
                $.post(url, {'registration.id': ${registration.id}, 'registrationForm.id' : formId}, onSuccess);
            }

            function getRadioRowId(radioOption) {
                return $(radioOption).parents("tr").first().attr("id");
            }

            function getRadioState(currentValue, selectedValue) {
               if(currentValue === selectedValue)
                    return NONE;
               else if(selectedValue == "true")
                    return ACCEPTED;
               else
                    return REJECTED;
            }

            function getFormSubmissionUrl(radioState, formId) {
                if (radioState == NONE) {
                    return '<s:url namespace="/sponsor/protocol/review/ajax" action="clearForm"/>';
                } else if(radioState == ACCEPTED) {
                    return '<s:url namespace="/sponsor/protocol/review/ajax" action="acceptForm"/>';
                } else {
                    return '<s:url namespace="/sponsor/protocol/review/ajax" action="commentsEnter"/>';
                }
            }

            function getSuccessFunction(radioState, formId) {
                if(radioState == ACCEPTED || radioState == NONE) {
                  return function(data, textStatus, jqXHR) {refreshPage(0, true); };
                } else {
                  return function(data, textStatus, jqXHR) {
                      indicateLoading(false);
                      $('#registrationDialog').html(data).dialog("open");
                  };
                }
            }

            reviewPage.submitReview = function() {
                if(!checkAllFormsCompleted()) {
                    return false;
                }

                var isAccepted = checkAllFormsAccepted()
                handleRegistrationCompletion(isAccepted);
                indicateLoading(false);
            }

            function checkAllFormsCompleted() {
                return $("#registrationFormsTable_${registration.id} input:checked").length === $("#registrationFormsTable_${registration.id} input:not(:checked)").length;
            }

            function checkAllFormsAccepted() {
                var isAccepted = true;

                $("#registrationFormsTable_${registration.id} input:checked").each(function () {
                    isAccepted = isAccepted && ($(this).val() == "true");
                });
                return isAccepted;
            }

            function handleRegistrationCompletion(isAccepted) {
                if (isAccepted) {
                    acceptRegistration();
                } else {
                    rejectRegistration();
                }
            }

            function acceptRegistration() {
                var url = '<s:url namespace="/sponsor/protocol/review/ajax" action="acceptRegistration"/>';
                $.post(url, {'registration.id': ${registration.id}}, function(data) {
                    if (isBlank(data)) {
                        reloadCurrentTab();
                        setTimeout('setPageSuccessMessage(\'<fmt:message key="registration.review.complete.success"/>\');', 800);
                    } else {
                        $('#registrationDialog').html(data).dialog("open");
                    }
                }, 'html');
            }

            function rejectRegistration(url) {
                var url = '<s:url namespace="/sponsor/protocol/review/ajax" action="reviewCommentsEnter"/>';
                $('#registrationDialog').load(url,{"registration.id" : ${registration.id}}).dialog("open");
            }

            reviewPage.buildDescriptionColumn = function(form) {
              if (form.linkToAddionalDocuments) {
                    return createLinkToFormDocuments(form, form.formTypeDescription);
                } else {
                    return createLinkToFormDownload(form);
                }
            }

            function createLinkToFormDocuments(form, cellContents) {
              return ajaxLinkFormatter(cellContents,
                        {url: '<s:property value="additionalDocumentsEnter"/>',
                    paramName:'registrationForm.id',
                    paramValue: 'id',
                    action: 'additionalDocumentsEnter',
                    target: 'registrationDialog'},
                    form);
            }

            function createLinkToFormDownload(form) {
              return linkFormatter(form.formTypeDescription,{
                    paramName:'registrationForm.id',
                    paramValue: 'id',
                    action : 'viewForm',
                    onclick : 'refreshPage(1000, true)',
                    url : '<s:property value="viewFormUrl"/>'
                }, form);
            }

            reviewPage.buildReviewRadioButtonColumn = function(form) {
                if (form.reviewRequired) {
                    var disabled = true;
                    var onclick = "";
                    var currentFormValue = getRadioButtonStatus(form);
                    if (isRegistrationReviewable() && (form.currentlyReviewable || currentFormValue != null)) {
                        disabled = false;
                        onclick = "indicateLoading();_reviewPage.submitFormReview(this,'" + currentFormValue + "');";
                    }
                    return createRadioButtonsString({Accept:"true",Reject:"false"}, "review_" + form.id, currentFormValue, disabled, onclick);
                } else {
                    return "";
                }
            }

            function createRadioButtonsString(values, name, selected, disabled, onclick) {
                var radioButtonString = "";
                for(valueIndex in values) {
                    var labelString = '';
                    var id = name + "_" + values[valueIndex];
                    var inputString = "<input type='radio' name='" + name + "' "
                    inputString += ((disabled) ? "disabled " : "");
                    if (values[valueIndex] == selected) {
                        inputString += "checked ";
                    }

                    inputString += (!isBlank(onclick)) ? "onclick=" + onclick + " " : "";
                    inputString += "id='" + id + "' value='" + values[valueIndex] +"'/>";

                    if(!_.isNumber(valueIndex)) {
                        //Map Label
                        labelString += "<label for='" + id + "' ";
                        labelString += disabled ? "class='disabledText'" : "";
                        labelString += ">" + valueIndex + "</label>"
                    }
                    radioButtonString += inputString + labelString;

                }
                return radioButtonString;
            }

            function getRadioButtonStatus(form) {
                if (form.formStatus._name === _reviewPage.acceptedState) {
                    return "true";
                } else if (form.formStatus._name === _reviewPage.rejectedState) {
                    return "false";
                }
                return null;
            }

            function isRegistrationReviewable() {
                return ${registration.reviewable};
            }

            reviewPage.buildCommentColumn = function(form) {
                if (checkShowCommentsLink(form)) {
                        return createCommentLink(form);
                }
                return "";
            }

            function checkShowCommentsLink(form) {
                return form.formStatus._name !== _reviewPage.acceptedState && form.formStatus._name !== _reviewPage.inReviewState && !isBlank(form.comments);
            }

            function createCommentLink(form) {
                return ajaxLinkFormatter(createImage('ico_comments_present.gif', 'View / Edit Comments'),
                        {url: '<s:property value="commentsUrl"/>',
                            paramName:'registrationForm.id',
                            paramValue: 'id',
                            action: 'comments',
                            target: 'registrationDialog'},
                        form);
            }

            reviewPage.buildAdditionalDocumentationColumn = function(form) {
                if (form.additionalDocumentsUploaded && !form.linkToAddionalDocuments) {
                    var imageName = 'ico_page_white_stack.png';
                    return createLinkToFormDocuments(form, createImage(imageName, 'Additional Documents'));
                }
                return "";
            }

            return reviewPage;
        })();


        $(document).ready(function() {
            var dataRows = ${registrationFormsJson};
            $('#registrationFormsTable_${registration.id}').dataTable( {
                aaData : dataRows,
                bInfo : false,
                bLengthChange: false,
                bPaginate: false,
                bFilter: false,
                aaSorting: [[3,'desc'], [0,'asc']],
                aoColumns: [{mDataProp: "formTypeDescription", sClass: "wrappedCell", fnRender : function(obj) {
                            return _reviewPage.buildDescriptionColumn(obj.aData);
                    }},
                    {mDataProp: "formStatus.display"},
                    {mDataProp: "formStatusDateDisplay", sType: "date" },
                    {mDataProp : "reviewRequired", fnRender : function(obj) {
                        return _reviewPage.buildReviewRadioButtonColumn(obj.aData);
                    }},
                    {mDataProp : null, fnRender : function(obj) {
                        return _reviewPage.buildCommentColumn(obj.aData);
                    }},
                    {mDataProp : null, fnRender : function(obj) {
                        return _reviewPage.buildAdditionalDocumentationColumn(obj.aData);
                    }}
                ],
                fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                    $(nRow).attr("id", aData.id);
                    return nRow;
                },
                fnInitComplete: function() {
                    indicateLoading(false);
                }
            });

        });
    </script>
</div>
