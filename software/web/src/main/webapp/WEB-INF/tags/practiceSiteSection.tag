<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>

<%@ attribute name="ohrpRequired" required="false" type="java.lang.Boolean" %>

<div>
    <s:url var="updateOhrpUrl" action="updateOhrp" namespace="/investigator/profile/associations/org/ajax" >
        <s:param name="profile.id" value="profile.id"/>
    </s:url>

    <s:url namespace="/investigator/profile/associations/org/ajax" action="manageAssociatedOrganizationAjaxEnter" var="addOrgUrl" escapeAmp="false">
        <s:param name="associationType" value="@gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name()" />
        <s:param name="profile.id" value="profile.id"/>
        <s:param name="ohrpRequired" value="%{#attr.ohrpRequired}"/>
    </s:url>
    <h2>
        <span id="practiceSiteAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
        <fmt:message key="registration.fda1572.practiceSites" />
        <sj:a openDialog="registrationDialog" href="%{addOrgUrl}" cssClass="edit" id="addPracticeSite"><fmt:message key="button.add.practice.site"/></sj:a>
    </h2>

    <table id="practiceSitesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
           summary="This table shows the Practice Sites that exist in the profile. It will show if there are validation problems,
                    a checkbox for adding to the 1572, the name of the organization, the CTEP ID, OHRP #, the address,
                    and the phone number.">
        <thead>
            <tr>
                <th scope="col" width="5px">&nbsp;<%--Validation Asterix--%></th>
                <th scope="col" width="20px">&nbsp;<%--Selection Checkbox--%></th>
                <th scope="col" width="150px"><fmt:message key="label.name"/></th>
                <th scope="col" width="150px"><fmt:message key="label.ctep.id"/></th>
                <th scope="col" width="150px"><div><s:property value="%{getText('organization.association.type.data.' + @gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name())}"/></div></th>
                <th scope="col" width="150px"><fmt:message key="label.address"/></th>
                <th scope="col" width="150px"><fmt:message key="label.phone"/></th>
            </tr>
        </thead>
    </table>

    <s:url var="selectPracticeSiteUrl" action="selectOrganization" />
    <s:url var="deselectPracticeSiteUrl" action="deselectOrganization" />
    <script type='text/javascript'>
        var editOhrpPrefix = 'edit_ohrp_';
        var ohrpDivPrefix = 'ohrp_';
        var practiceSite = '<s:property value="@gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name()"/>';

        var incomplete = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE"/>';

        $(document).ready(function () {
            var practiceSites = ${organizationAssociationListings};
            isLongData = practiceSites.length > ${minPaginationResults};
            $('#practiceSitesTable').dataTable({
                "aaData": practiceSites,
                "bInfo": isLongData,
                "bLengthChange": isLongData,
                "bPaginate": isLongData,
                "bFilter": isLongData,
                "aoColumns": [
                    {mDataProp: null, fnRender: function (obj) {
                        return createPracticeSiteValidationErrorColumn(obj.aData);
                    }},
                    {mDataProp: null, fnRender: function (obj) {
                        return createLabCheckColumn(obj.aData);
                    }},
                    {mDataProp: "name"},
                    {mDataProp: "ctepId"},
                    {mDataProp: "ohrpAssuranceNumber", fnRender: function (obj) {
                        return createOhrpDisplay(obj.aData.ohrpAssuranceNumber, obj.aData.organizationExternalId);
                    }},
                    {mDataProp: "address", fnRender: function (obj) {
                        return addressFormatter(obj.aData.address);
                    }},
                    {mDataProp: "phoneNumber"}
                ],
                fnRowCallback: function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    setUpEditOhrp(nRow, aData.organizationExternalId);
                    $(nRow).attr("id", aData.organizationExternalId);
                    $(nRow).data("organizationExternalId", aData.organizationExternalId);
                    return nRow;
                }
            });
            checkForNoPracticeSiteSelectionsIfIncomplete();
        });

        function clickEdit(organizationExternalId) {
            var div = $('#' + this.ohrpDivPrefix + escapeForJquery(organizationExternalId));
            var ohrp = $(div).text();
            var column = $(div).parent();
            $(div).remove();
            createEditDiv(column, ohrp, organizationExternalId);
        }

        function createEditDiv(column, ohrp, organizationExternalId) {
            var organizationExternalId = $(column).parent().data('organizationExternalId');
            var newDiv = $('<div class="ohrpCell" id="' + this.ohrpDivPrefix + organizationExternalId + '"></div>');
            var form = createOhrpForm(organizationExternalId, ohrp);
            var btnDiv = createButtonDiv(organizationExternalId, ohrp);
            $(form).append(btnDiv);
            $(newDiv).append(form).appendTo(column);
        }

        function createButtonDiv(organizationExternalId, ohrp) {
            var btnDiv = $('<div class="float_right"></div>');
            var saveBtn = $(createImageAsLink('save_' + organizationExternalId, 'Save', 'ico_checkmark.png', 'saveOhrpBtn'));
            var cancelBtn = $(createImageAsLink('cancel_' + organizationExternalId, 'Cancel', 'ico_xmark.gif', 'cancelOhrpBtn'));

            $(saveBtn).click(function () {
                setupSaveClick(this, organizationExternalId, this.practiceSite);
            });
            $(cancelBtn).click(function () {
                showOhrpDisplay(organizationExternalId, ohrp);
            });

            $(btnDiv).append(cancelBtn).append(saveBtn);
            return btnDiv;
        }

        function setupSaveClick(saveBtn, organizationExternalId, associationType) {
            var form = $(saveBtn).parents('form').first();
            var btnDiv = $(saveBtn).parent();
            var formData = $(form).serialize();
            var val = $(form).find("input[name='associationOhrp']").val();

            switchToLoading(btnDiv);
            $.post('<s:property value="#updateOhrpUrl"/>', formData, null, 'json').success(function (data) {
                if (_.isEmpty(data)) {
                    showOhrpDisplay(organizationExternalId, val);
                    clearMessages();
                } else {
                    showErrorMessage(btnDiv, data);
                }
            }).error(function (data) {
                showErrorMessage(btnDiv, "<fmt:message key='organization.association.error.ohrp.update'/>")
            });
        }

        function showOhrpDisplay(organizationExternalId, ohrp) {
            var div = $('#' + this.ohrpDivPrefix + escapeForJquery(organizationExternalId));
            var column = $(div).parent();
            $(div).remove();
            $(column).html(createOhrpDisplay(ohrp, organizationExternalId));
            setUpEditOhrp(column, organizationExternalId);
        }

        function setUpEditOhrp(row, organizationExternalId) {
            var editOhrpLinkId = 'a#' + this.editOhrpPrefix + escapeForJquery(organizationExternalId);
            $(row).find(editOhrpLinkId).click(function () {
                clickEdit(organizationExternalId);
            });
        }

        function switchToLoading(item) {
            _fbUtil.switchObjects(item, _fbUtil.loadingIcon);
        }

        function showErrorMessage(btnDiv, messages) {
            switchFromLoading(btnDiv);
            setPageErrorMessages(messages);
        }

        function switchFromLoading(item) {
            _fbUtil.switchObjects(_fbUtil.loadingIcon, item);
        }

        function createOhrpForm(organizationExternalId, ohrp) {
            var form = $('<form id="ohrpForm"></form>');
            $('<input name="organizationExternalId" type="hidden" />').val(organizationExternalId).appendTo(form);
            $('<input name="associationType" type="hidden"/>').val(this.practiceSite).appendTo(form);
            $(createNamedElement('input', 'associationOhrp')).val(ohrp).attr('maxlength', 11).width(85).keydown(testForEnter).appendTo(form);
            return form;
        }

        function createOhrpDisplay(ohrp, organizationExternalId) {
            var editImage = createImageAsLink(this.editOhrpPrefix + organizationExternalId, 'Edit', 'ico_edit.gif', 'editOhrpBtn');

            var divId = this.ohrpDivPrefix + organizationExternalId;
            return "<div class='ohrpCell' id='" + divId + "'>" + _fbUtil.blankIfNull(ohrp) + editImage + "</div>";
        }

        function createImageAsLink(id, alt, imageFile, className) {
            return "<a href='javascript:void(0)' id='" + id + "' class='" + className + "'>"
                    + createImage(imageFile, alt, "ico") + "</a>";
        }

        function createPracticeSiteValidationErrorColumn(practiceSite) {
            if (practiceSite.invalid) {
                return "<span class='validationErrorAsterisk'>*</span>";
            }
            return "";
        }

        function createLabCheckColumn(practiceSite) {
            var checkboxHtml = "<input type='checkbox' name='selectedIds' ";
            var checkBoxId = "practiceSite_"+ practiceSite.organizationExternalId;
            checkboxHtml += "id='" + checkBoxId + "' ";
            checkboxHtml += "value='" + practiceSite.organizationExternalId + "' ";
            if (practiceSite.selected) {
                checkboxHtml += "checked='checked' ";
            }
            checkboxHtml += "onclick=\"handlePracticeSiteClick('" + practiceSite.organizationExternalId + "');\" />";
            return createHiddenCheckboxLabel(checkBoxId) + checkboxHtml;
        }

        function handlePracticeSiteClick(organizationExternalId) {
            indicateLoading(true);
            var formData = {};
            formData["registration.id"] = ${registration.id};
            formData["organizationExternalId"] = organizationExternalId;
            var selecting = $('#practiceSite_' + escapeForJquery(organizationExternalId)).is(':checked');
            var url = selecting ? '${selectPracticeSiteUrl}' : '${deselectPracticeSiteUrl}';
            $.post(url, formData, function (errorMessages) {
                setPageErrorMessages(errorMessages);
                checkForNoPracticeSiteSelectionsIfIncomplete();
                indicateLoading(false);
            });
        }

        function checkForNoPracticeSiteSelectionsIfIncomplete() {
            var registrationStatus = "${registration.status}";
            if (registrationStatus === incomplete) {
                var showPracticeSiteAsterisk = $("#practiceSitesTable input:checked").length == 0;
                $("#practiceSiteAsterisk").toggle(showPracticeSiteAsterisk);
            }
        }
    </script>
</div>
