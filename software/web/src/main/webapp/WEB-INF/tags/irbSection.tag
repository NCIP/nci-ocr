<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>


<div>
    <s:url namespace="/investigator/profile/associations/org/ajax" action="manageAssociatedOrganizationAjaxEnter" var="addOrgUrl" escapeAmp="false">
        <s:param name="associationType" value="@gov.nih.nci.firebird.data.OrganizationRoleType@IRB.name()" />
        <s:param name="profile.id" value="profile.id"/>
    </s:url>

    <h2>
        <span id="irbAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
        <fmt:message key="registration.fda1572.irb" />
        <sj:a openDialog="registrationDialog" href="%{addOrgUrl}" cssClass="edit" id="addIrb"><fmt:message key="button.add.irb"/></sj:a>
    </h2>

    <table id="irbTable" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table shows the IRBs that exist in the profile. It will show if there are validation problems,
                    a checkbox for adding to the 1572, the name of the organization, the address, and the phone number.">
        <thead>
            <tr>
                <th scope="col" width="5px">&nbsp;<%--Validation error indicator--%></th>
                <th scope="col" width="20px">&nbsp;<%--Selection checkbox--%></th>
                <th scope="col" width="150px"><div><fmt:message key="label.name"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.address"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.phone"/></div></th>
            </tr>
        </thead>
    </table>

    <s:url var="selectIrbUrl" action="selectOrganization" />
    <s:url var="deselectIrbUrl" action="deselectOrganization" />
    <script type='text/javascript'>
    var incomplete = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE"/>';

        $(document).ready(function() {
          var irbs = ${organizationAssociationListings};
            isLongData = irbs.length > ${minPaginationResults};
            $('#irbTable').dataTable( {
                "aaData" : irbs,
                "bInfo" : isLongData,
                "bLengthChange": isLongData,
                "bPaginate": isLongData,
                "bFilter": isLongData,
                "aoColumns": [{mDataProp: null, fnRender : function(obj) {
                                return createIrbValidationErrorColumn(obj.aData);
                              }},
                              {mDataProp: null, fnRender : function(obj) {
                                return createIrbCheckColumn(obj.aData);
                              }},
                              {mDataProp: "name"},
                              {mDataProp: "address", fnRender : function(obj) {
                                  return addressFormatter(obj.aData.address);
                              }},
                              {mDataProp: "phoneNumber"}
                          ],
               fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                   $(nRow).attr("id", aData.organizationExternalId);
                   return nRow;
               }
            });
            checkForNoIrbSelectionsIfIncomplete();
        });

        function createIrbValidationErrorColumn(irb) {
          if (irb.invalid) {
                return "<span class='validationErrorAsterisk'>*</span>";
          }
            return "";
        }

        function createIrbCheckColumn(irb) {
            var checkboxHtml = "<input type='checkbox' name='selectedIds' ";
            var checkBoxId = "irb_"+ irb.organizationExternalId;
            checkboxHtml += "id='"+ checkBoxId + "' ";
            checkboxHtml += "value='" + irb.organizationExternalId + "' ";
            if (irb.selected) {
                checkboxHtml += "checked='checked' ";
            }
            checkboxHtml += "onclick=\"handleIrbClick('" + irb.organizationExternalId + "');\" />";
            return createHiddenCheckboxLabel(checkBoxId) + checkboxHtml;
        }

        function handleIrbClick(organizationExternalId) {
          indicateLoading(true);
            var formData = {};
            formData["registration.id"] = ${registration.id};
            formData["organizationExternalId"] = organizationExternalId;
            var selecting = $('#irb_' + escapeForJquery(organizationExternalId)).is(':checked');
            var url = selecting ? '${selectIrbUrl}' : '${deselectIrbUrl}';
            $.post(url, formData, function(errorMessages) {
                setPageErrorMessages(errorMessages);
                checkForNoIrbSelectionsIfIncomplete();
                indicateLoading(false);
            });
        }

        function checkForNoIrbSelectionsIfIncomplete() {
            var registrationStatus = "${registration.status}";
            if (registrationStatus === incomplete) {
              var showIrbAsterisk = $("#irbTable input:checked").length == 0;
              $("#irbAsterisk").toggle(showIrbAsterisk);
          }
        }
    </script>

</div>
