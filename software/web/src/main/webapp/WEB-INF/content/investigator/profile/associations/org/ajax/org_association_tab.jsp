<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>

<s:url namespace="/investigator/profile/associations/org/ajax" action="enter" var="organizationAssociationUrl"/>

<firebird:messages/>

<div class="credentialSection">
    <s:url var="addNewPracticeSiteUrl" namespace="/investigator/profile/associations/org/ajax"
           action="manageAssociatedOrganizationAjaxEnter" escapeAmp="false">
        <s:param name="associationType" value="@gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name()"/>
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>

    <h2>
        <fmt:message key="investigator.profile.associatedOrgs.practiceSite"/>
        <sj:a openDialog="profileDialog" href="%{addNewPracticeSiteUrl}" cssClass="edit"
              id="createNewPracticeSite"><fmt:message key="button.add.practice.site"/></sj:a>
    </h2>

    <table id="associatedPracticeSiteOrgTable" class="ui-jqgrid-htable ui-jqgrid-btable"
            summary="This table lists the different Practice Sites which have been added to the profile. It displays the
                    name, CTEP ID, OHRP number, email, phone number, a mailing address, and a link to delete them from the
                    profile.">
        <thead>
            <tr>
                <th scope="col" width="150px"><div><fmt:message key="label.name"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.ctep.id"/></div></th>
                <th scope="col" width="150px">
                    <div>
                        <s:property
                            value="%{getText('organization.association.type.data.' + @gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name())}"/>
                    </div>
                </th>
                <th scope="col" width="150px"><div><fmt:message key="label.email"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.phone"/></div></th>
                <th scope="col" width="150px"><div><fmt:message key="label.address"/></div></th>
                <th scope="col" width="33px"><%--Delete Link--%></th>
            </tr>
        </thead>
    </table>

</div>
<br/>

<div class="credentialSection">
    <s:url var="addNewLabUrl" namespace="/investigator/profile/associations/org/ajax"
           action="manageAssociatedOrganizationAjaxEnter" escapeAmp="false">
        <s:param name="associationType"
                 value="@gov.nih.nci.firebird.data.OrganizationRoleType@CLINICAL_LABORATORY.name()"/>
        <s:param name="profile.id" value="%{profile.id}"/>
    </s:url>
    <h2>
        <fmt:message key="investigator.profile.associatedOrgs.clinicalLaboratories"/>
<sj:a openDialog="profileDialog" href="%{addNewLabUrl}" cssClass="edit" id="createNewLab"><fmt:message key="button.add.clinical.lab"/></sj:a>
</h2>
<s:url var="certificateUrl" action="clinicalLabCertificateEnter" namespace="/investigator/profile/associations/org/ajax" >
<s:param name="profile.id" value="%{profile.id}" />
</s:url>
<table id="associatedLabOrgTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table lists the different Clinical Laboratories which have been added to the profile. It displays the
                name, CTEP ID, email, phone number, a mailing address, a link to view the laboratory certificates,
                and a link to delete them from the profile.">
<thead>
    <tr>
        <th scope="col" width="180px"><div><fmt:message key="label.name"/></div></th>
        <th scope="col" width="180px"><div><fmt:message key="label.email"/></div></th>
        <th scope="col" width="180px"><div><fmt:message key="label.phone"/></div></th>
        <th scope="col" width="180px"><div><fmt:message key="label.address"/></div></th>
        <th scope="col" width="105px"><div><fmt:message key="label.certificates"/></div></th>
        <th scope="col" width="33px"><%--Delete Link--%></th>
    </tr>
</thead>
</table>
</div>
<br/>

<div class="credentialSection">
<s:url var="addNewIRBUrl" namespace="/investigator/profile/associations/org/ajax" action="manageAssociatedOrganizationAjaxEnter" escapeAmp="false">
<s:param name="associationType" value="@gov.nih.nci.firebird.data.OrganizationRoleType@IRB.name()" />
<s:param name="profile.id" value="%{profile.id}" />
</s:url>

<h2>
<fmt:message key="investigator.profile.associatedOrgs.irb"/>
<sj:a openDialog="profileDialog" href="%{addNewIRBUrl}" cssClass="edit" id="createNewIRB"><fmt:message key="button.add.irb"/></sj:a>
</h2>
<table id="associatedIRBOrgTable" class="ui-jqgrid-htable ui-jqgrid-btable"
       summary="This table lists the different Institutional Review Boards which have been added to the profile. It
                displays the name, CTEP ID, email, phone number, mailing address, and a link to delete them from the
                profile.">
<thead>
    <tr>
        <th scope="col" width="230px"><div><fmt:message key="label.name"/></div></th>
        <th scope="col" width="230px"><div><fmt:message key="label.email"/></div></th>
        <th scope="col" width="230px"><div><fmt:message key="label.phone"/></div></th>
        <th scope="col" width="230px"><div><fmt:message key="label.address"/></div></th>
        <th scope="col" width="63px"><%--Delete Link--%></th>
    </tr>
</thead>
</table>
</div>

<s:url var="updateOhrpUrl" action="updateOhrp" namespace="/investigator/profile/associations/org/ajax" >
<s:param name="profile.id" value="%{profile.id}" />
</s:url>

<s:url var="removeAssociationUrl" action="removeAssociatedOrganizationEnter" namespace="/investigator/profile/associations/org/ajax">
<s:param name="profile.id" value="%{profile.id}" />
</s:url>
<script type="text/javascript">

var _associatedOrgPage = (function () {
    var associatedOrgPage = {};

    associatedOrgPage.editOhrpPrefix = 'edit_ohrp_';
    associatedOrgPage.ohrpDivPrefix = 'ohrp_';
    associatedOrgPage.divStorage = "stored_div";
    associatedOrgPage.practiceSite = '<s:property value="@gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE.name()"/>';

    var loadingIcon = $('<div id="loadingIcon" class="ico_btn">' + createImage('loading.gif', 'Loading') + '</div>');

    associatedOrgPage.createOhrpDisplay = function (ohrp, associationId) {
        var editImage = createImageAsLink(this.editOhrpPrefix + associationId, 'Edit', 'ico_edit.gif', 'editOhrpBtn');

        var divId = this.ohrpDivPrefix + associationId;
        return "<div class='ohrpCell' id='" + divId + "'>" + _fbUtil.blankIfNull(ohrp) + editImage + "</div>";
    };

    function createImageAsLink(id, alt, imageFile, className) {
        return "<a href='javascript:void(0)' id='" + id + "' class='" + className + "'>"
                + createImage(imageFile, alt, "ico") + "</a>";
    }

    associatedOrgPage.setUpEditOhrp = function (row, associationId) {
        var editOhrpLinkId = 'a#' + this.editOhrpPrefix + associationId;
        $(row).find(editOhrpLinkId).click(function () {
            clickEdit(associationId);
        });
    };

    function clickEdit(associationId) {
        var div = $('#' + _associatedOrgPage.ohrpDivPrefix + associationId);
        var ohrp = $(div).text();
        var column = $(div).parent();
        $(div).remove();
        createEditDiv(column, ohrp, associationId);
    }

    function createEditDiv(column, ohrp, associationId) {
        var organizationExternalId = $(column).parent().data('organizationExternalId');
        var newDiv = $('<div class="ohrpCell" id="' + _associatedOrgPage.ohrpDivPrefix + associationId + '"></div>');
        var form = createOhrpForm(organizationExternalId, ohrp);
        var btnDiv = createButtonDiv(associationId, ohrp);
        $(form).append(btnDiv);
        $(newDiv).append(form).appendTo(column);
    };

    function createOhrpForm(organizationExternalId, ohrp) {
        var form = $('<form></form>');
        $('<input name="organizationExternalId" type="hidden" />').val(organizationExternalId).appendTo(form);
        $('<input name="associationType" type="hidden"/>').val(associatedOrgPage.practiceSite).appendTo(form);
        $(createNamedElement('input', 'associationOhrp')).val(ohrp).attr('maxlength', 30).width(85).keydown(testForEnter).appendTo(form);
        return form;
    }

    function createButtonDiv(associationId, ohrp) {
        var btnDiv = $('<div class="float_right"></div>');
        var saveBtn = $(createImageAsLink('save_' + associationId, 'Save', 'ico_checkmark.png', 'saveOhrpBtn'));
        var cancelBtn = $(createImageAsLink('cancel_' + associationId, 'Cancel', 'ico_xmark.gif', 'cancelOhrpBtn'));

        $(saveBtn).click(function () {
            setupSaveClick(this, associationId, associatedOrgPage.practiceSite);
        });
        $(cancelBtn).click(function () {
            showOhrpDisplay(associationId, ohrp);
        });

        $(btnDiv).append(cancelBtn).append(saveBtn);
        return btnDiv;
    }

    function setupSaveClick(saveBtn, associationId, associationType) {
        var form = $(saveBtn).parents('form').first();
        var btnDiv = $(saveBtn).parent();
        var formData = $(form).serialize();
        var val = $(form).find("input[name='associationOhrp']").val();
        switchToLoading(btnDiv);
        $.post('<s:property value="#updateOhrpUrl"/>', formData, null, 'json').success(function (data) {
            if (_.isEmpty(data)) {
                showOhrpDisplay(associationId, val);
                clearMessages();
            } else {
                showErrorMessage(btnDiv, data);
            }
        }).error(function (data) {
            showErrorMessage(btnDiv, "<fmt:message key='organization.association.error.ohrp.update'/>")
        });
    }

    function switchToLoading(item) {
        _fbUtil.switchObjects(item, loadingIcon);
    }

    function showErrorMessage(btnDiv, messages) {
        switchFromLoading(btnDiv);
        setPageErrorMessages(messages);
    }

    function switchFromLoading(item) {
        _fbUtil.switchObjects(loadingIcon, item);
    }

    function showOhrpDisplay(associationId, ohrp) {
        var div = $('#' + _associatedOrgPage.ohrpDivPrefix + associationId);
        var column = $(div).parent();
        $(div).remove();
        $(column).html(_associatedOrgPage.createOhrpDisplay(ohrp, associationId));
        _associatedOrgPage.setUpEditOhrp(column, associationId);
    }

    return associatedOrgPage;
})();

$(document).ready(function () {
    var baseUrl = '<s:property value="%{#removeAssociationUrl}"/>';

    var practiceSites = ${practiceSiteAssociationsJSON};
    var isLongData = practiceSites.length > ${minPaginationResults};
    $('#associatedPracticeSiteOrgTable').dataTable({
        "aaData": practiceSites,
        "bInfo": isLongData,
        "bLengthChange": isLongData,
        "bPaginate": isLongData,
        "bFilter": isLongData,
        "aoColumns": [
            {mDataProp: "name"},
            {mDataProp: "ctepId"},
            {mDataProp: "ohrpAssuranceNumber", fnRender: function (obj) {
                return _associatedOrgPage.createOhrpDisplay(obj.aData.ohrpAssuranceNumber, obj.aData.id);
            }},
            {mDataProp: "email"},
            {mDataProp: "phoneNumber"},
            {mDataProp: "postalAddress", fnRender: function (obj) {
                return addressFormatter(obj.aData.postalAddress);
            }},
            {"mDataProp": null, "fnRender": function (obj) {
                return createOrgAssociationDeleteLink(obj.aData, baseUrl,
                        '<s:property value="@gov.nih.nci.firebird.data.OrganizationRoleType@PRACTICE_SITE"/>');
            }}
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            _associatedOrgPage.setUpEditOhrp(nRow, aData.id);
            $(nRow).attr("id", aData.id);
            $(nRow).data("organizationExternalId", aData.externalId);
            return nRow;
        }
    });
    var labs = ${labAssociationsJSON};
    isLongData = labs.length > ${minPaginationResults};
    $('#associatedLabOrgTable').dataTable({
        "aaData": labs,
        "bInfo": isLongData,
        "bLengthChange": isLongData,
        "bPaginate": isLongData,
        "bFilter": isLongData,
        "aoColumns": [
            {mDataProp: "name"},
            {mDataProp: "email"},
            {mDataProp: "phoneNumber"},
            {mDataProp: "postalAddress", fnRender: function (obj) {
                return addressFormatter(obj.aData.postalAddress);
            }},
            {mDataProp: null, fnRender: function (obj) {
                return labCertificateViewLinkFormatter(obj.aData, '<s:property value="%{certificateUrl}" />');
            }},
            {"mDataProp": null, "fnRender": function (obj) {
                return createOrgAssociationDeleteLink(obj.aData, baseUrl,
                        '<s:property value="@gov.nih.nci.firebird.data.OrganizationRoleType@CLINICAL_LABORATORY.name()"/>');
            }}
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            $(nRow).attr("id", aData.id);
            return nRow;
        }
    });
    var irbs = ${irbAssociationsJSON};
    isLongData = irbs.length > ${minPaginationResults};
    $('#associatedIRBOrgTable').dataTable({
        "aaData": irbs,
        "bInfo": isLongData,
        "bLengthChange": isLongData,
        "bPaginate": isLongData,
        "bFilter": isLongData,
        "aoColumns": [
            {mDataProp: "name"},
            {mDataProp: "email"},
            {mDataProp: "phoneNumber"},
            {mDataProp: "postalAddress", fnRender: function (obj) {
                return addressFormatter(obj.aData.postalAddress);
            }},
            {"mDataProp": null, "fnRender": function (obj) {
                return createOrgAssociationDeleteLink(obj.aData, baseUrl,
                        '<s:property value="@gov.nih.nci.firebird.data.OrganizationRoleType@IRB.name()"/>');
            }}
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            $(nRow).attr("id", aData.id);
            return nRow;
        }
    });
});
</script>