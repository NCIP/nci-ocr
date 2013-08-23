<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="sponsor.protocol.forms.edit.title"/></firebird:dialogHeader>
            <firebird:messages/>
            <s:form action="saveForms" id="editFormsForm" namespace="/sponsor/representative/protocol/ajax" method="post">
                <s:hidden name="protocol.id" value="%{protocol.id}"/>
                <table id="editGridtable" class="ui-jqgrid-htable ui-jqgrid-btable"
                       summary="This table displays the Registration Forms which have been added to this protocol registration for
                                filling out by investigators and / or subinvestigators. Each row provides the name of the form,
                                a select box to select the optionality of the form for investigators, a select box to select the
                                optionality of the form for subinvestigators, and a link to remove this form from the protocol.">
                    <thead>
                        <tr>
                            <th scope="col" width="225px"><div><fmt:message key="sponsor.protocol.forms.name.column"/></div></th>
                            <th scope="col" width="225px"><div><fmt:message key="sponsor.protocol.forms.investigators.column"/></div></th>
                            <th scope="col" width="225px"><div><fmt:message key="sponsor.protocol.forms.subinvestigators.column"/></div></th>
                            <th scope="col" width="34px"></th>
                        </tr>
                    </thead>
                </table>

                <s:select id="formType" list="standardForms" listKey="id" listValue="name" label="Form Type" onchange="showDescription()" />
                <div class="side_buttons">
                    <a id="addButton" href="#" class="button"><fmt:message key="button.add"/></a>
                </div>
                <div class="clear blank_space"></div>
                <div id="descriptionDiv" class="descriptiondiv">
                    <div class="commentHeader">
                        <h4>
                            <firebird:label messageKey="label.description" bold="true"/>
                        </h4>
                    </div>
                    <div class="content">
                        <p id="description" />
                    </div>
                </div>
                <div class="clear"></div>
                <br/>
                <firebird:instructionBubble messageKey="sponsor.protocol.edit.instructions"/>

                <firebird:label forId="commentEditor" messageKey="label.comments"/>
                <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.data.ProtocolRevision@MAX_COMMENT_LENGTH" />
                <firebird:richTextArea id="commentEditor" name="comment" required="true" maxCharacters="${maxCommentCharacters}" />

                <div class="btn_bar">
                    <sj:a id="confirmFormsSaveButton" cssClass="button" formIds="editFormsForm" targets="popUpDialog"
                        onClickTopics="submitFormHandlerTopic" href="#"><fmt:message key="button.save"/></sj:a>
                    <s:a id="confirmFormsCancelButton" href="#" cssClass="button"><fmt:message key="button.cancel"/></s:a>
                </div>
            </s:form>
        </div>
    </div>
</div>
<script type="text/javascript">

    var standardForms = {};
    var keys = [], iter = 0;

    var standardFormsJson = ${standardFormsJson};
    for (var i = 0; i < standardFormsJson.length; i++) {
        var form = standardFormsJson[i];
        standardForms[form.id] = form;
    }

    for(keys[iter++] in standardForms);

    function selectOptionalityFormatter(cellvalue, formType, investigator) {
        var div = $("<div></div>");
        var optionalityType = getOptionalityType(investigator);
        var control = createNamedElement("select", optionalityType);
        var selectBoxId = optionalityType + "_" + formType.id;
        control.attr("id", selectBoxId);
        control.appendTo(div);

        var formOptionalities =  getFormOptionalities(formType, investigator);
        _.each(formOptionalities, function(formOptionality) {
            var selectOption = "<option/>";
            if (cellvalue === formOptionality._name) {
                selectOption = "<option selected/>";
            }
            $(selectOption).val(formOptionality._name).text(formOptionality.display).appendTo(control);
        });

        return createHiddenCheckboxLabel(selectBoxId) + $(div).html();
    }

    function getOptionalityType(investigator) {
        if (investigator) {
            return 'investigatorOptionalities';
        } else {
            return 'subinvestigatorOptionalities';
        }
    }

    function getFormOptionalities(formType, investigator) {
        if (investigator) {
            return formType.allowableInvestigatorFormOptionalities;
        } else {
            return formType.allowableSubInvestigatorFormOptionalities;
        }
    }

    function showDescription() {
        if ($('#formType option:selected').val()) {
            $('#description').text(standardForms[document.getElementById("formType").value].description);
        } else {
            $('#description').text('<fmt:message key="sponsor.protocol.forms.all.added"/>');
        }
    }

    function buildAddFormsDropdown() {
        $('#formType option').remove();
        if (standardForms.length == $('#editGridtable tbody tr').length) {
            $('<option></option>').attr("value",-1).text("No Available Forms").appendTo($('#formType'));
            $("#addButton").hide();
        } else {
            $("#addButton").show();
            _.each(keys, function(item) {
                if (item !== "length" && $('#editGridtable tr[id='+ item +']').length == 0) {
                    $('<option></option>').attr("value",item).text(standardForms[item].name).appendTo($('#formType'));
                }
            });
        }
        showDescription();
    }

    function deleteRow(button){
        $("#editGridtable").dataTable().fnDeleteRow($(button).parents('tr')[0]);
        buildAddFormsDropdown();
    }

    $(document).ready(function() {

        var form = document.getElementById("editFormsForm");
        form.getSerializedState = function() {
            return $("#editGridtable input, #editGridtable select").serialize();
        };

        form.messages = {
            "sponsor.protocol.edit.close.warning" : "<fmt:message key='sponsor.protocol.edit.close.warning'/>"
        };

        var dataRows = ${jsonFormsTable};

        $('#editGridtable').dataTable( {
            aaData : dataRows,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            aoColumns: [{mDataProp: "name", fnRender: function(obj){
                    return "<input type='hidden' name='formTypeIds' value='" + obj.aData.id + "'/>" + obj.aData.name;
                }},
                {mDataProp: null, fnRender : function(obj) {
                    return selectOptionalityFormatter(obj.aData.invOptionality._name, obj.aData, true);
                }},
                {mDataProp: null, fnRender : function(obj) {
                    return selectOptionalityFormatter(obj.aData.subinvOptionality._name, obj.aData, false);
                }},
                {mDataProp : null, fnRender : function(obj) {
                    return '<img class="delete" src="<c:url value="/images/ico_delete.gif"/>" alt="Delete" title="Delete" onclick="deleteRow(this)" id="delete_' + obj.aData.id +'"/>';
                }}
            ],
            fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                $(nRow).attr("id", aData.id);
                return nRow;
            },
            fnInitComplete: function() {
                buildAddFormsDropdown();
                showDescription();
                setUpProtocolForm(form);
            }
        } );

        $("#addButton").click( function(){
            var selectedForm = $('#formType option:selected').val();
            if(selectedForm != -1) {
                $("#editGridtable").dataTable().fnAddData(standardForms[selectedForm]);
                buildAddFormsDropdown();
            }
        });

        var dialogBeforeCloseHandler = function(event) {
            return $("#editFormsForm").length === 0 || form.checkForChangesAndBlock(event);
        };

        $("#popUpDialog").unbind("dialogbeforeclose").bind("dialogbeforeclose", dialogBeforeCloseHandler);

        $("#confirmFormsCancelButton").click(function(event) {
            if (form.checkForChangesAndBlock(event)) {
                $("#popUpDialog").unbind("dialogbeforeclose");
                closeDialog();
            }
        });

    });
</script>
