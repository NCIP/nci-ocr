<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:dialogHeader><fmt:message key="sponsor.protocol.investigator.invite.title"/></firebird:dialogHeader>

                    <firebird:messages/>
                    <firebird:instructionBubble messageKey="sponsor.protocol.investigator.invite.instructions"/>
                    <s:form action="invite" id="inviteForm" method="post">
                        <s:select id="filter"
                                  label="Automatically select all Investigators with status"
                                  name="protocol.sponsor.id"
                                  list="@gov.nih.nci.firebird.data.InvitationStatus@values()"
                                  listKey="name()"
                                  listValue="%{getText(key)}"
                                  value="@gov.nih.nci.firebird.data.InvitationStatus@NOT_INVITED"
                                  headerValue="-- Select a Status --"
                                  headerKey="" />
                        <br/>

                        <table id="investigatorsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
                                summary="This table shows the investigators who have been added to the protocol and allows
                                        you to select investigators to invite. It displays a checkbox to select investigators,
                                        the investigator's name, their registration status, and their invitation status.">
                            <thead>
                                <tr>
                                   <th scope="col" width="10px"><%--Checkbox Select--%></th>
                                   <th scope="col" width="150px"><div><fmt:message key="label.investigator"/></div></th>
                                   <th scope="col" width="150px"><div><fmt:message key="label.registration.status"/></div></th>
                                   <th scope="col" width="200px"><div><fmt:message key="label.invitation.status"/></div></th>
                                </tr>
                            </thead>
                        </table>

                        <div class="btn_bar">
                            <s:a id="inviteButton" href="#" cssClass="button"><fmt:message key="button.send.invitations"/></s:a>
                            <s:a id="cancelButton" href="#" cssClass="button"><fmt:message key="button.cancel"/></s:a>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>
        <script type="text/javascript">
        var anyOption = '<fmt:message key="sponsor.protocol.investigator.invite.option.any"/>';
        var noneOption = '<fmt:message key="sponsor.protocol.investigator.invite.option.none"/>';
        var notInvitedText = '<s:property value="%{getText(@gov.nih.nci.firebird.data.InvitationStatus@NOT_INVITED.key)}"/>';
            $(document).ready(function() {
                buildInvestigatorsTable();
                addAnyAndNoneOptionsToFilter();
                addOnChangeToFilter();
                selectRegistrationsWithStatus(notInvitedText);
                addButtonClickHandlers();
            });

            function buildInvestigatorsTable() {
                var registrations = ${registrationsAsJson};
                $('#investigatorsTable').dataTable( {
                    aaData : registrations,
                    bInfo : false,
                    bLengthChange: false,
                    bPaginate: false,
                    bFilter: false,
                    aoColumns: [
                                {mDataProp: null, fnRender : function(obj) { return buildCheckBoxColumn(obj.aData)}},
                                {mDataProp: "investigator.sortableName", bUseRendered: false, fnRender: function (obj) {
                                    return obj.aData.investigator.displayNameForList;
                                }},
                                {mDataProp: "statusText"},
                                {mDataProp: "invitationStatusText"}
                              ],
                   "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                       $(nRow).attr("id", aData.id);
                       return nRow;
                   }
                } );
            }

            function buildCheckBoxColumn(registration) {
                var checkboxId = "selectRegistration_" + registration.id;
                return createHiddenCheckboxLabel(checkboxId) + "<input type='checkbox' id='" + checkboxId + "' />";
            }

            function addAnyAndNoneOptionsToFilter() {
                var filter = $("#filter");
                $(filter).append($('<option/>').attr('value',anyOption).text(anyOption))
                $(filter).append($('<option/>').attr('value',noneOption).text(noneOption))
            }

            function addOnChangeToFilter() {
                $("#filter").change(function (event) {
                  if (event.target.selectedIndex != 0) {
                        var selectedStatus = $("#filter option:selected").text();
                        if (selectedStatus === anyOption) {
                            $("input:checkbox").attr('checked', 'checked');
                        } else {
                            $("input:checkbox").attr('checked', false);
                            selectRegistrationsWithStatus(selectedStatus);
                        }
                        event.preventDefault();
                  }
                });
            }

            function selectRegistrationsWithStatus(selectedStatus) {
                $("tr").each(function(index) {
                    $(this).children("td").each(function(index) {
                        if ($(this).text().indexOf(selectedStatus) === 0) {
                            $(this).parent().find("input:checkbox").attr('checked','checked');
                        }
                    });
                });
            }

            function addButtonClickHandlers() {
                $("#inviteButton").click( function(){
                    disableDialog();
                    var formData = {};
                    formData.invitedRegistrationIds = getSelectedRegistrationIds();
                    formData["protocol.id"] = '${protocol.id}';
                    formData["registration.id"] = '${registration.id}';
                    $.post(document.inviteForm.action, formData, function(data) {
                        enableDialog();
                        $(getCurrentDialog()).html(data);
                    });
                });

                $("#cancelButton").click( function() {
                    closeDialog();
                });
            }

            function getSelectedRegistrationIds() {
                var registrationIds = new Array();
                $("tr:has(input:checked)").each(function(index) {
                    registrationId = $(this).attr("id");
                    registrationIds.push(registrationId);
                });
                return registrationIds;
            }
        </script>
