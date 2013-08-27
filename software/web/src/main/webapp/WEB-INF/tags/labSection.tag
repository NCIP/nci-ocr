<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>

<div>
    <s:url namespace="/investigator/profile/associations/org/ajax" action="manageAssociatedOrganizationAjaxEnter" var="addOrgUrl" escapeAmp="false">
        <s:param name="associationType" value="@gov.nih.nci.firebird.data.OrganizationRoleType@CLINICAL_LABORATORY.name()" />
        <s:param name="profile.id" value="profile.id"/>
    </s:url>
    <h2>
        <span id="labAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
        <fmt:message key="registration.fda1572.labs" />
        <sj:a openDialog="registrationDialog" href="%{addOrgUrl}" cssClass="edit" id="addLab"><fmt:message key="button.add.clinical.lab"/> </sj:a>
    </h2>

    <table id="associatedLabsTable" class="ui-jqgrid-htable ui-jqgrid-btable"
           summary="This table shows the Clinical Laboratories that exist in the profile. It will show if there are validation problems,
                    a checkbox for adding to the 1572, the name of the organization, the address, and the phone number.">
        <thead>
            <tr>
                <th scope="col" width="5px">&nbsp;<%--Validation Asterix--%></th>
                <th scope="col" width="20px">&nbsp;<%--Selection Checkbox--%></th>
                <th scope="col" width="200px"><fmt:message key="label.name"/></th>
                <th scope="col" width="200px"><fmt:message key="label.address"/></th>
                <th scope="col" width="200px"><fmt:message key="label.phone"/></th>
            </tr>
        </thead>
    </table>

    <s:url var="selectLabUrl" action="selectOrganization" />
    <s:url var="deselectLabUrl" action="deselectOrganization" />
    <script type='text/javascript'>
        var INCOMPLETE_STATUS = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE"/>';

        $(document).ready(function () {
            var labs = ${organizationAssociationListings};
            var isLongData = labs.length > ${minPaginationResults};
            $('#associatedLabsTable').dataTable({
                "aaData": labs,
                "bInfo": isLongData,
                "bLengthChange": isLongData,
                "bPaginate": isLongData,
                "bFilter": isLongData,
                "aoColumns": [
                    {mDataProp: null, fnRender: function (obj) {
                        return createLabValidationErrorColumn(obj.aData);
                    }},
                    {mDataProp: null, fnRender: function (obj) {
                        return createLabCheckColumn(obj.aData);
                    }},
                    {mDataProp: "name"},
                    {mDataProp: "address", fnRender: function (obj) {
                        return __addressFormatter(obj.aData.address);
                    }},
                    {mDataProp: "phoneNumber"}
                ],
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    $(nRow).attr("id", aData.id);
                    return nRow;
                }
            });
            checkForNoLabSelectionsIfIncomplete();
        });

        function createLabValidationErrorColumn(lab) {
            if (lab.invalid) {
                return "<span class='validationErrorAsterisk'>*</span>";
            }
            return "";
        }

        function createLabCheckColumn(lab) {
            var checkboxHtml = "<input type='checkbox' name='selectedIds' ";
            var checkBoxId = "lab_"+ lab.id;
            checkboxHtml += "id='" + checkBoxId + "' ";
            checkboxHtml += "value='" + lab.id + "' ";
            if (lab.selected) {
                checkboxHtml += "checked='checked' ";
            }
            checkboxHtml += "onclick=\"handleLabClick('" + lab.id + "');\" />";
            return createHiddenCheckboxLabel(checkBoxId) + checkboxHtml;
        }

        function handleLabClick(id) {
            indicateLoading(true);
            var formData = {};
            formData["registration.id"] = ${registration.id};
            formData["organization.id"] = id;
            var selecting = $('#lab_' + id).is(':checked');
            var url = selecting ? '${selectLabUrl}' : '${deselectLabUrl}';
            $.post(url, formData, function (errorMessages) {
                setPageErrorMessages(errorMessages);
                checkForNoLabSelectionsIfIncomplete();
                indicateLoading(false);
            });
        }

        function checkForNoLabSelectionsIfIncomplete() {
            var registrationStatus = "${registration.status}";
            if (registrationStatus === INCOMPLETE_STATUS) {
                var showLabAsterisk = $("#associatedLabsTable input:checked").length == 0;
                $("#labAsterisk").toggle(showLabAsterisk);
            }
        }
    </script>
</div>
