<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader>
                <fmt:message key="registration.subinvestigator.manage.title"/>
            </firebird:dialogHeader>
            <firebird:instructionBubble messageKey="registration.subinvestigator.add.from.profile.instructions" />
            <firebird:messages/>
            <s:form id="addSubInvestigatorsFromProfileForm">
                <table id="profileSubinvestigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                        summary="This table displays all of the subinvestigators which have been previously added to your
                                profile. You can select to add them to this registration with the checkbox, and view their
                                name, email, and phone number.">
                    <thead>
                        <tr>
                            <th scope="col" width="20px">&nbsp;<%--Checkbox column--%></th>
                            <th scope="col" width="175px"><div><fmt:message key="label.name"/></div></th>
                            <th scope="col" width="175px"><div><fmt:message key="label.email"/></div></th>
                            <th scope="col" width="150px"><div><fmt:message key="label.phone"/></div></th>
                        </tr>
                    </thead>
                </table>
            </s:form>

            <div class="btn_bar">
                <s:a id="saveButton" href="#" cssClass="button"><fmt:message key="button.save"/></s:a>
                <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>

        </div>
    </div>
</div>
<s:url action="addFromProfile" var="addSubInvestigatorsUrl">
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>
<script type='text/javascript'>
    var _profileSubinvestigatorsPage = (function(){
        var page = {};
        page.createCheckColumn = function(person) {
            var checkboxHtml = "<input type='checkbox' name='selectedIds' ";
            var checkboxId = "subInvestigator_"+ person.id;
            checkboxHtml += "id='"+ checkboxId + "' ";
            checkboxHtml += "value='" + person.id + "' ";
            checkboxHtml += "/>";
            return createHiddenCheckboxLabel(checkboxId) + checkboxHtml;
        };

        return page;
    })();


    $(document).ready(function() {

        $("#saveButton").click( function(){
            var url = '<s:property value="addSubInvestigatorsUrl"/>';
            var data = $('#addSubInvestigatorsFromProfileForm').serialize();
            var target = "#registrationDialog";
            _fbUtil.performAjaxPost(url, target, data);
        });

        var profileSubinvestigators = ${profileSubinvestigatorsAsJson};

        $('#profileSubinvestigatorsTable').dataTable( {
            aaData : profileSubinvestigators,
            bInfo : false,
            bLengthChange: false,
            bPaginate: false,
            bFilter: false,
            oLanguage: {
                sEmptyTable : '<fmt:message key="registration.subinvestigator.none.from.profile"/>'
            },
            aoColumns: [{mDataProp: null, fnRender : function(obj) {
                            return _profileSubinvestigatorsPage.createCheckColumn(obj.aData);
                        }},
                        {mDataProp: "sortableName", bUseRendered: false, fnRender : function(obj) {
                             return personFormatter(obj.aData);
                        }},
                        {mDataProp: "email"},
                        {mDataProp: "phoneNumber"}
                      ],
           fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
               $(nRow).attr("id", aData.id);
               return nRow;
           }
        });
    });
</script>