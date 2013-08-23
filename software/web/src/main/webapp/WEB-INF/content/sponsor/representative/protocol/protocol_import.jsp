<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<sj:dialog id="messagesDialog" autoOpen="false" modal="true" width="950" position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened"/>

<h1>
    <s:if test="%{protocolImportJob == null}" >
        <fmt:message key="import.protocol.upload.title" />
    </s:if>
    <s:else>
        <fmt:message key="import.protocol.in.progress.title" >
            <fmt:param value="${protocolImportJob.sponsor.name}"/>
        </fmt:message>
    </s:else>
</h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:if test="%{protocolImportJob == null}" >
                <firebird:instructionBubble messageKey="import.protocol.upload.instructions"/>
                <s:form id="uploadFileForm" namespace="/sponsor/representative/protocol" action="uploadImportFile" enctype="multipart/form-data" method="POST">
                    <div class="formcol_double">
                        <firebird:sponsorSelection />
                    </div>
                    <div class="formcol clear">
                        <s:file name="importFile" id="importFile" requiredPosition="right" size="30" requiredLabel="true"
                            label="%{getText('import.protocol.export.file')}" labelposition="top" onKeyDown="this.blur()" onContextMenu="return false;"/>
                    </div>
                    <div class="btn_bar clear">
                        <sj:a id="continueButton" cssClass="button continueButton" formIds="uploadFileForm" href="#"><fmt:message key="button.continue"/></sj:a>
                        <s:a id="cancelButton" namespace="/sponsor/protocol" action="enterBrowseProtocols" cssClass="button"><fmt:message key="button.cancel"/></s:a>
                    </div>
                </s:form>
            </s:if>
            <s:else>
                <firebird:instructionBubble messageKey="import.protocol.import.successful.instructions"/>
                <div class="blank_space"></div>
                <div class="formcol_row clear">
                    <s:a id="importButton_top" cssClass="button importButton" href="#"><fmt:message key="button.import"/></s:a>
                    <s:a id="cancelButton_top" action="cancelImport" cssClass="button cancelButton"><fmt:message key="button.cancel"/></s:a>
                    <s:a id="completeButton_top" action="cancelImport" cssClass="button completeButton hide"><fmt:message key="button.complete"/></s:a>
                </div>
                <br>
                <h3><span id="jobStatus"><fmt:message key="label.status"/>: ${protocolImportJob.status.display}</span></h3>
                <div class="float table_stats"></div>
                <div class="float_right"><a href="javascript:void(0)" onclick="_importPage.retrieveCurrentJobState();">Refresh</a></div>
                <s:form id="importRecordsForm">
                    <table id="importRecordsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                            summary="This table presents a summary of the different protocols that were included in the selected import
                                    file. It provides a checkbox to select if the protocol should be imported following validation,
                                    the protocol number which can be clicked to view further information about the protocol,
                                    the title of the protocol, the phase of the protocol, the current import status,
                                    and a link if there are any messages regarding validation or import.">
                        <thead>
                            <tr>
                                <th scope="col" width="20px"><div>&nbsp;</div></th>
                                <th scope="col" width="125px"><div><fmt:message key="textfield.protocol.number"/></div></th>
                                <th scope="col" width="250px"><div><fmt:message key="textfield.title"/></div></th>
                                <th scope="col" width="100px"><div><fmt:message key="label.protocol.phase"/></div></th>
                                <th scope="col" width="150px"><div><fmt:message key="label.validation.status"/></div></th>
                                <th scope="col" width="30px"><div>&nbsp;</div></th>
                            </tr>
                        </thead>
                    </table>
                    <div class="float table_stats"></div>
                    <s:hidden name="sponsorExternalId" value="%{protocolImportJob.sponsor.externalId}"/>
                    <div class="btn_bar clear">
                        <s:a id="importButton_bottom" cssClass="button importButton" href="#"><fmt:message key="button.import"/></s:a>
                        <s:a id="cancelButton_bottom" action="cancelImport" cssClass="button cancelButton"><fmt:message key="button.cancel"/></s:a>
                        <s:a id="completeButton_bottom" action="cancelImport" cssClass="button completeButton hide"><fmt:message key="button.complete"/></s:a>
                    </div>
                </s:form>
                <script type="text/javascript">
                    var _importPage = (function(existingPage) {
                        if (!existingPage) {
                            var VALIDATING_IMPORT_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportJobStatus@VALIDATING.display"/>';
                            var READY_FOR_IMPORT_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportJobStatus@VALIDATION_COMPLETE.display"/>';
                            var IMPORTING_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportJobStatus@IMPORTING.display"/>';
                            var IMPORT_COMPLETE_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportJobStatus@IMPORT_COMPLETE.display"/>';

                            var VALID_IMPORT_DETAIL_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportDetailStatus@VALID.display"/>';
                            var INVALID_IMPORT_DETAIL_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportDetailStatus@INVALID.display"/>';
                            var IMPORTING_DETAIL_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportDetailStatus@IMPORTING.display"/>';
                            var IMPORTED_DETAIL_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportDetailStatus@IMPORT_COMPLETE.display"/>';
                            var IMPORT_ERROR_DETAIL_STATE = '<s:property value="@gov.nih.nci.firebird.service.protocol.ProtocolImportDetailStatus@IMPORT_ERROR.display"/>';

                            var DISABLED_IMPORT_BUTTON_CLASS = "disabledButton";

                            var STATUS_MESSAGES = [];
                            STATUS_MESSAGES[VALIDATING_IMPORT_STATE] = "<fmt:message key='import.protocol.statistics.validating'/>";
                            STATUS_MESSAGES[READY_FOR_IMPORT_STATE] = "<fmt:message key='import.protocol.statistics.validated'/>";
                            STATUS_MESSAGES[IMPORTING_STATE] = "<fmt:message key='import.protocol.statistics.importing'/>";
                            STATUS_MESSAGES[IMPORT_COMPLETE_STATE] = "<fmt:message key='import.protocol.statistics.imported'/>";

                            var page = {};
                            var currentJobState = undefined;
                            var refreshTimer = undefined;

                            page.retrieveCurrentJobState = function() {
                                indicateLoading();
                                $.ajax({url: '<s:url action="retrieveImportJobState"/>', success:function(jobState) {
                                    currentJobState = jobState;
                                    page.refreshTable(jobState.details);
                                    page.refreshPageState();
                                    indicateLoading(false);
                                }, dataType: 'json'});
                            };

                            page.refreshTable = function(importDetails) {
                                var table = $('#importRecordsTable').dataTable();
                                table.fnClearTable();
                                table.fnAddData(importDetails);
                            };

                            page.refreshPageState = function(jobState) {
                                var status = currentJobState.status.display;
                                if (!page.isReadyForImport() && !isImportComplete()) {
                                    status += " " + createImage("loading.gif", "Working");
                                }
                                $("#jobStatus").html(status);
                                updateImportButtonAppearance();
                                updateCancelButtonAppearance();
                                updateStatistics();
                            };

                            function updateImportButtonAppearance() {
                                var $importBtn = $(".importButton");
                                if (_.contains([IMPORT_COMPLETE_STATE, IMPORTING_STATE], currentJobState.status.display)) {
                                    $importBtn.hide();
                                } else {
                                    $importBtn.show();
                                    if (page.isReadyForImport()) {
                                        $importBtn.removeClass(DISABLED_IMPORT_BUTTON_CLASS);
                                     } else {
                                        $importBtn.addClass(DISABLED_IMPORT_BUTTON_CLASS);
                                     }
                                }
                            }

                            function updateCancelButtonAppearance() {
                                var $cancelBtn = $(".cancelButton");
                                var $completeBtn = $(".completeButton");
                                if (currentJobState.status.display === IMPORTING_STATE) {
                                    $cancelBtn.hide();
                                    $completeBtn.hide();
                                 } else {
                                     $cancelBtn.show();
                                     if (currentJobState.status.display === IMPORT_COMPLETE_STATE) {
                                         $cancelBtn.hide();
                                         $completeBtn.show();
                                     }
                                 }
                            }

                            function updateStatistics() {
                                var details = currentJobState.details;
                                var $stats = $(".table_stats");
                                $stats.html(STATUS_MESSAGES[currentJobState.status.display]);
                                $(".totalRecords", $stats).text(details.length);
                                $(".validationCompleteCount", $stats).text(getStatusCount(details, [VALID_IMPORT_DETAIL_STATE, INVALID_IMPORT_DETAIL_STATE]));
                                $(".validCount", $stats).text(getStatusCount(details, VALID_IMPORT_DETAIL_STATE));
                                $(".invalidCount", $stats).text(getStatusCount(details, INVALID_IMPORT_DETAIL_STATE));
                                page.updateCheckedCount();
                                $(".importCount", $stats).text(getStatusCount(details, [IMPORTING_DETAIL_STATE, IMPORTED_DETAIL_STATE, IMPORT_ERROR_DETAIL_STATE]));
                                $(".completeImportCount", $stats).text(getStatusCount(details, IMPORTED_DETAIL_STATE));
                                $(".importFailCount", $stats).text(getStatusCount(details, IMPORT_ERROR_DETAIL_STATE));
                                $(".notImportedCount", $stats).text(_.select(details, function(detail) {
                                    return !(getCheckDetailStatusFunc(IMPORTED_DETAIL_STATE))(detail);
                                }).length);
                            }

                            function getStatusCount(details, states) {
                                return _.select(details, getCheckDetailStatusFunc(states)).length;
                            }

                            function getCheckDetailStatusFunc(expectedStatus) {
                                if (!_.isArray(expectedStatus)) {
                                    expectedStatus = [expectedStatus];
                                }
                                return function(detail) {
                                    return _.contains(expectedStatus, detail.status.display);
                                };
                            }

                            page.updateCheckedCount = function() {
                                $(".selectedCount", ".table_stats").text($(':checkbox:checked:enabled').length);
                            }

                            page.isReadyForImport = function() {
                                return currentJobState.status.display === READY_FOR_IMPORT_STATE;
                            };

                            page.createCheckBox = function(rowObj) {
                                var importDetail = rowObj.aData;
                                var checkboxHtml = "<input type='checkbox' name='selectedIndexForImport' ";
                                checkboxHtml += "id='"+ getCheckBoxId(importDetail) + "' ";
                                checkboxHtml += "value='" + importDetail.index + "' ";
                                if (importDetail.markedForImport) {
                                    checkboxHtml += "checked='checked' ";
                                }
                                if (!page.isReadyForImport() || importDetail.status.display === INVALID_IMPORT_DETAIL_STATE) {
                                    checkboxHtml += "disabled";
                                }
                                checkboxHtml += " />";
                                return createHiddenCheckboxLabel(getCheckBoxId(importDetail)) + checkboxHtml;
                            };

                            function getCheckBoxId(importDetail) {
                                return "import_" + importDetail.index;
                            }

                            function getDuplicateProtocolDetectionFunc($checkbox, protocolNumber) {
                                return function(importDetail) {
                                    var itemCheckBoxId = getCheckBoxId(importDetail);
                                    if (itemCheckBoxId !== $checkbox.attr("id") &&
                                            importDetail.protocol.protocolNumber === protocolNumber) {
                                        var disable = ($checkbox.attr("checked")) ? "disabled" : "";
                                        $('#' + itemCheckBoxId).attr("disabled", disable);
                                    }
                                }
                            }

                            page.buildValidationProblemLink = function(rowObj) {
                                var importDetail = rowObj.aData;
                                if(!importDetail.validationResult.valid && (page.isReadyForImport() || isImportComplete())) {
                                    var messagesUrl = "<s:url namespace='./ajax' action='viewProtocolImportMessages'/>";
                                    importDetail.id = importDetail.index;
                                    return ajaxLinkFormatter(createImage('ico_comments_present.gif', 'Messages'),
                                            {url: messagesUrl,
                                                paramName:'selectedIndexForImport',
                                                paramValue: 'index',
                                                action: 'messages',
                                                target: 'messagesDialog'},
                                            importDetail);
                                }
                                return "";
                            };

                            page.clickImportButton = function(event) {
                                event.preventDefault();
                                if (page.isReadyForImport()) {
                                    var selectedRecords = $("#importRecordsForm").serialize();
                                    var url = "<s:url action='importRecords'/>";
                                    indicateLoading();
                                    $.post(url, selectedRecords, function(data) {
                                        if (!isBlank(data)) {
                                           setPageErrorMessages(data);
                                        } else {
                                            clearMessages();
                                        }
                                        page.startTimer();
                                        page.retrieveCurrentJobState();
                                        indicateLoading(false);
                                    }, "json");
                                }
                            };

                            page.startTimer = function() {
                                if (!refreshTimer) {
                                    refreshTimer = setTimeout(refreshPageInformation, <fmt:message key="import.protocol.refresh.timer"/>)
                                }
                            }

                            function refreshPageInformation() {
                                refreshTimer = undefined;
                                if ($('#importRecordsTable').length > 0 && !page.isReadyForImport() && !isImportComplete()) {
                                    page.retrieveCurrentJobState();
                                    page.startTimer();
                                }
                            }

                            function isImportComplete() {
                                return currentJobState.status.display === IMPORT_COMPLETE_STATE;
                            }

                            return page;
                        } else {
                            return existingPage;
                        }
                    })(_importPage);

                    $(".completeButton").hide();

                    $('#importRecordsTable').dataTable({
                        aaData: [ ],
                        bInfo : false,
                        bLengthChange: false,
                        bPaginate: false,
                        bFilter: false,
                        aoColumns: [{mDataProp: null, fnRender : _importPage.createCheckBox},
                                    {mDataProp: null, fnRender : function(obj) {
                                        var protocolNumber = obj.aData.protocol.protocolNumber;
                                        if(isBlank(protocolNumber)) {
                                            protocolNumber = "<fmt:message key='label.invalid' />";
                                        }

                                        return ajaxLinkFormatter(protocolNumber,
                                            {url: "<s:url namespace='./ajax' action='viewProtocolDetails'/>",
                                            paramName:'selectedIndexForImport',
                                            paramValue: 'index',
                                            action: 'details',
                                            target: 'messagesDialog'},
                                            obj.aData);
                                        }},
                                      {mDataProp: "protocol.protocolTitle"},
                                      {mDataProp: null, fnRender : function (obj) {
                                          var phase = obj.aData.protocol.phase;
                                          return phase ? phase.display : "<fmt:message key='label.invalid'/>";
                                      }},
                                      {mDataProp : "status.display"},
                                      {mDataProp: null, fnRender : _importPage.buildValidationProblemLink}
                                  ],
                       fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                           $(nRow).attr("id", aData.index);
                           if(_importPage.isReadyForImport()) {
                               $(":checkbox", nRow).click(_importPage.updateCheckedCount);
                               $(":checkbox", nRow).blur(_importPage.updateCheckedCount);
                           }
                           return nRow;
                       }
                    });
                    _importPage.retrieveCurrentJobState();
                    _importPage.startTimer();
                    $(".importButton").click(_importPage.clickImportButton);
                </script>
            </s:else>
        </div>
    </div>
</div>
