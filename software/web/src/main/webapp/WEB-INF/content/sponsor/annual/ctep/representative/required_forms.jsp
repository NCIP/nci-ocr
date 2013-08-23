<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<s:url var="downloadLink" action="downloadFile" namespace="/util/ajax" />
<s:url var="downloadIconUrl" value='/images/ico_document.png' />
<s:url var="saveFormConfigurationUrl" action="saveFormConfiguration" />
<s:url var="editAnnualRegistrationUrl" action="edit" namespace="/sponsor/annual/ctep/representative/ajax"/>


<html>
    <body>
        <h1><fmt:message key="sponsor.annual.registrations.required.forms"/></h1>

        <sj:dialog id="editFormDialog" autoOpen="false" modal="true" width="950"
               position="top" onCloseTopics="dialogClosed" resizable="false" onOpenTopics="dialogOpened" />

        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:messages />
                    <s:url var="selectLink" action="enterProtocol" namespace="/sponsor/protocol" />

                    <span class="inline float">
                        <s:select id="formType" list="standardForms" listKey="id" listValue="name" />
                        <s:a id='addFormButton' href="#" cssClass='button' onclick="addForm();"><fmt:message key="button.add.new.form" /></s:a>
                    </span>
                    <span class="float_right marginBottom_5">
                        <s:a id='saveButton' href="#" cssClass='button blueButton float_right' onclick="saveFormConfiguration();" ><fmt:message key="button.save" /></s:a>
                        <s:a id='cancelButton' href="#" cssClass='button float_right' onclick="cancelChanges();" ><fmt:message key="button.cancel" /></s:a>
                    </span>

                    <table id="annualRegistrationsTable" class="ui-jqgrid-htable ui-jqgrid-btable" 
                           summary="This table lays out a display of the forms that are part of annual registrations. You 
                                   are provided with a link to download a sample of that form, the name of the form, you can decide
                                   whether that form should be required or optional, and there are buttons to allow you to either
                                   edit (when available) or remove a form.">
                    </table>

                    <s:hidden id="subInvestigatorText"/>
                    <s:hidden id="protocolText"/>
                </div>
            </div>
        </div>
        <script type="text/javascript">

            var standardForms = {};
            var keys = [], iter = 0;

            var standardFormsJson = ${standardFormsJson};
            for (var i = 0; i < standardFormsJson.length; i++) {
                form = standardFormsJson[i];
                standardForms[form.id] = form;
            }

            for(keys[iter++] in standardForms);

            $(document).ready(function() {
                createAnnualRegistrationsTable();
                buildAddFormsDropdown();
                $("#subInvestigatorText").val("${registrationConfiguration.subInvestigatorText}");
                $("#protocolText").val('${registrationConfiguration.protocolText}');
            });


            function createAnnualRegistrationsTable() {
                var dataRows = ${gridtableData};
                var isLongData = dataRows.length > ${minPaginationResults};
                $('#annualRegistrationsTable').dataTable( {
                    aaData : dataRows,
                    bInfo : isLongData,
                    bLengthChange: isLongData,
                    bPaginate: isLongData,
                    bFilter: isLongData,
                    aoColumns: [{mDataProp: null, fnRender : function(obj) {
                                    return buildDownloadLink(obj.aData);
                                 }},
                              {mDataProp: "description"},
                              {mDataProp: null, fnRender : function(obj) {
                                  return buildRadioButtons(obj.aData);
                              }},
                             {mDataProp:null, fnRender:function (obj) {
                                  return "<div style='width=140px;' class='float_right'>" + buildRemoveButton(obj.aData) + buildEditButton(obj.aData) + "</div>";
                              }}
                              ],
                   fnRowCallback: function( nRow, form, iDisplayIndex, iDisplayIndexFull ) {
                       $(nRow).find('.radioButtons').buttonset();
                       $(nRow).attr("id", form.id);
                       return nRow;
                   }
                });
            }

            function buildEditButton(form) {
                if (form.name === "Form FDA 1572") {
                    return ajaxLinkFormatter('<fmt:message key='button.edit'/>',
                            {
                                url:'${editAnnualRegistrationUrl}',
                                paramName:'registration.id',
                                paramValue:'id',
                                htmlClass:'editButton',
                                action:'editForm',
                                target:'editFormDialog'
                            },
                            form);
                }
                return "";
            }

            function buildDownloadLink(form) {
                var cellvalue = form.id;
                var options = { id :  form.id,
                        action : 'downloadFile',
                        url:'${downloadLink}',
                        imageUrl : '${downloadIconUrl}',
                        imageTitle : 'Download Sample',
                        paramName:'file.id',
                        paramValue: 'sampleId'};
                return linkedImageFormatter(cellvalue, options, form);
            }

            var required = '<s:property value="@gov.nih.nci.firebird.data.FormOptionality@REQUIRED"/>';
            var optional = '<s:property value="@gov.nih.nci.firebird.data.FormOptionality@OPTIONAL"/>';

            function buildRadioButtons(form) {
              var optionalText = "<fmt:message key='button.optional'/>";
              var requiredText = "<fmt:message key='button.required'/>";
              var name = "optionality_" + form.id;
              var requiredId = "required_" + form.id;
              var optionalId = "optional_" + form.id;
              var requiredChecked = form.formOptionality == required;
              var optionalChecked = form.formOptionality == optional;
              return '<div class="radioButtons">' +
                  buildRadioButtonString(name, requiredId, requiredText, required, requiredChecked) +
                  buildRadioButtonString(name, optionalId, optionalText, optional, optionalChecked) + '</div>';
            }

            function buildRadioButtonString(name, id, text, value, checked) {
              var radioButtonString = '<input ';
              if (checked) {
                radioButtonString += 'checked="checked" ';
              }
              radioButtonString += 'type="radio" id="' + id + '" name="' + name + '" value="' + value + '"><label for="' + id + '">' + text + '</label>';
              return radioButtonString;
            }

            function buildRemoveButton(form) {
              var removeText = "<fmt:message key='button.remove'/>";
              return "<a href='#' class='deleteButton' onclick='deleteRow(this)' >" + removeText + "</a>";
            }

            function addForm() {
              var selectedForm = $('#formType option:selected').val();
                if(selectedForm != -1) {
                    $("#annualRegistrationsTable").dataTable().fnAddData(standardForms[selectedForm]);
                    buildAddFormsDropdown();
                }
            }

            function buildAddFormsDropdown() {
                $('#formType option').remove();
                if (_.size(standardForms) == $('#annualRegistrationsTable tbody tr').length) {
                    $('<option></option>').attr("value",-1).text("No Available Forms").appendTo($('#formType'));
                    $("#addFormButton").hide();
                } else {
                    $("#addFormButton").show();
                    _.each(keys, function(item) {
                        if (item !== "length" && $('#annualRegistrationsTable tr[id='+ item +']').length == 0) {
                            $('<option></option>').attr("value",item).text(standardForms[item].name).appendTo($('#formType'));
                        }
                    });
                }
            }

            function deleteRow(button){
                $("#annualRegistrationsTable").dataTable().fnDeleteRow($(button).parents('tr')[0]);
                buildAddFormsDropdown();
            }


            function saveFormConfiguration() {
              var form = $("#editFormsForm");
              var requiredFormIds = [];
              var optionalFormIds = [];

              $('#annualRegistrationsTable tr').each(function (index, row) {
                var optionality = $(row).find("input:checked").attr("value");
                if (optionality == required) {
                    requiredFormIds.push(row.id);
                } else if (optionality == optional) {
                    optionalFormIds.push(row.id);
                }
              });


              var url = '${saveFormConfigurationUrl}'
              var data = { 'requiredFormIds': requiredFormIds, 'optionalFormIds': optionalFormIds,
                  'subInvestigatorText': $("#subInvestigatorText").val(), 'protocolText': $("#protocolText").val() }
              clearMessages();
              $.post(url, data, function(){
                setPageSuccessMessage('<fmt:message key="sponsor.annual.registrations.save.success.message"/>');
              });
            }

            function cancelChanges() {
                if (confirm('<fmt:message key="sponsor.annual.registrations.cancel.changes.message"/>')) {
                    refreshPage(0);
                }
            }

        </script>
    </body>
</html>