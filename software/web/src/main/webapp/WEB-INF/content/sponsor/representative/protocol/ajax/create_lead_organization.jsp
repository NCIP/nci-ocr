<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
<firebird:dialogHeader>
    <fmt:message key="sponsor.protocol.lead.organization.title"/>:
</firebird:dialogHeader>
<firebird:messages/>
<div id="leadOrganizationAccordion">
    <h3><a href="#"><fmt:message key="sponsor.protocol.lead.organization.select.organization.header"/></a></h3>

    <div id="selectOrganization">
        <div id="searchOrganization"
             class="search_section hide">
             <s:fielderror>
                    <s:param>organizationSearch</s:param>
                </s:fielderror>
            <firebird:organizationSearch labelKey="organization.search.label" setFocus="true">
                <s:a id="createNewOrganization" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
            </firebird:organizationSearch>
        </div>
        <div id="selectedOrganization"
             class="selection_section hide">
            <s:hidden id="selectedOrganizationExternalId" name="selectedOrganizationExternalId"/>
            <firebird:searchAgainLink/>
            <firebird:organizationDisplay organization="${organization}"
                                          tagVariableName="organizationDisplay"/>
        </div>
        <div id="newOrganization"
             class="create_new_section hide">
            <firebird:searchAgainLink/>
            <div class="formcol">
                <s:textfield id="organization.name" name="organization.name"
                             maxlength="160" size="30" requiredLabel="true"
                             cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}"/>
            </div>
            <div class="formcol clear">
                <s:textfield id="organization.email" name="organization.email"
                             maxlength="50" size="30" cssStyle="width: 19em;"
                             label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
            </div>

            <div class="formcol">
                <firebird:phoneNumber beanPrefix="organization" varPrefix="leadOrganization"/>
            </div>
            <div class="dotted_line"><br></div>
            <firebird:addressFields beanPrefix="organization.postalAddress" varPrefix="leadOrganization"/>
            <s:hidden name="createNewOrganization" value="true"/>
        </div>
    </div>
    <h3>
        <a href="#"><fmt:message key="sponsor.protocol.lead.organization.select.principal.investigator.header"/></a>
    </h3>

    <div id="selectPerson">
        <div id="searchPerson"
             class="search_section hide">
            <s:fielderror>
                <s:param>personSearch</s:param>
            </s:fielderror>
            <firebird:personSearch labelKey="person.search.label">
                <s:a id="createNewPerson" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
            </firebird:personSearch>
        </div>
        <div id="selectedPerson"
             class="selection_section hide">
            <s:hidden id="selectedPersonExternalId" name="selectedPersonExternalId"/>
            <firebird:searchAgainLink/>
            <firebird:viewPerson person="${principalInvestigator}"
                                 tagVariableName="principalInvestigator"/>
        </div>
        <div id="newPerson"
             class="create_new_section hide">
            <firebird:searchAgainLink/>
            <firebird:managePersonFormFields beanPrefix="principalInvestigator"/>
            <s:hidden name="createNewPerson" value="true"/>
        </div>
    </div>
</div>
<div class="btn_bar clear">
    <s:a id="saveLeadOrganizationButton" cssClass="button" href="#" onclick="saveLeadOrganization()"><fmt:message key="button.save"/></s:a>
    <s:a id="cancelLeadOrganizationButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
</div>

<s:url var="saveLeadOrganizationUrl" action="saveAndValidateLeadOrganization"/>
<script type="text/javascript">

    var errorsInSectionPrefixText = '<s:property value="%{getText('accordion.form.errors.in.section')}"/>';
    var SEARCH_SECTION = 0;
    var SELECTION_SECTION = 1;
    var CREATE_NEW_SECTION = 2;
    var protocolFields = protocolFields || {};

    function getSearchAgainFunction(sectionName) {
        return function () {
            getSectionFunction(sectionName, function (index) {
                if (index === SEARCH_SECTION) {
                    showSection(this);
                } else {
                    hideSection(this);
                }
            });
        }
    }

    function showSection(section) {
        $(section).wrapInner("<form/>").show();
        $(section).parent().height($(section).height());
    }

    function hideSection(section) {
        var section$ = $(section);
        section$.hide();
        $("input:not([type='hidden'])", section$).val("");
        $("form", section$).children().first().unwrap();
        $(".wwerr", section$).remove();
        $(".fieldError", section$).removeClass("fieldError");
    }

    function getSectionFunction(sectionName, iterationFunc) {
        $("#" + sectionName + " > div").each(iterationFunc);
    }

    function getCreateNewFunction(sectionName) {
        return function () {
            getSectionFunction(sectionName, function (index) {
                if (index === CREATE_NEW_SECTION) {
                    showSection(this);
                } else {
                    hideSection(this);
                }
            });
        }
    }

    function wrapDataInFormsIfNecessary() {
        $("#leadOrganizationAccordion > div > div:not(.hide)").each(function () {
            $(this).wrapInner("<form/>");
        });
    }

    function addErrorMessagesIfNecessary() {
        var messages = [];
        var sectionIndex = 0;
        $("#leadOrganizationAccordion").children("h3,div").each((function () {
            var sectionHeader = "";
            return function (index) {
                if ($(this).is("h3")) {
                    sectionHeader = $(this).text();
                } else {
                    if ($(".wwerr", $(this)).length > 0) {
                        if (messages.length === 0) {
                            $("#leadOrganizationAccordion").accordion("option", "active", sectionIndex);
                        }
                        messages.push(errorsInSectionPrefixText + " " + sectionHeader);
                    }
                    sectionIndex++;
                }
            };
        })());
        setPageErrorMessages(messages);
    }

    function saveLeadOrganization() {
        disableDialog();

        var organizationExternalId = $("#selectedOrganizationExternalId").val();
        var personExternalId = $("#selectedPersonExternalId").val();
        if (!verifyOrganizationNotAlreadySelected(organizationExternalId, personExternalId)) {
          enableDialog();
          return;
        }

        var url = '<s:property value="#saveLeadOrganizationUrl"/>'
        var target = getCurrentDialog();

        var formData = "";
        $("#leadOrganizationAccordion").children("div").each(function () {
            if (formData) {
                formData += "&"
            }
            formData += $("form", this).serialize();
        });
        $.post(url, formData, function (data) {
            enableDialog();
            var selectedOrganizationExternalId = $("#selectedOrganizationExternalId", data).val();
            if ($(".wwerr", data).length === 0) {
                addLeadOrganizationRow(data);
                closeDialog({});
            } else {
                $(target).html(data);
            }
        });
    }

    function verifyOrganizationNotAlreadySelected(organizationExternalId, personExternalId) {
        if (protocolFields.checkLeadOrganizationAdded(organizationExternalId, personExternalId)) {
            setPageErrorMessages("<fmt:message key='sponsor.protocol.lead.organization.duplicate.error'/>");
            return false;
        }
        return true;
    }

    function addLeadOrganizationRow(pageData) {
        var personKey = $("#selectedPersonExternalId", pageData).val();
        var personName = $("#personDisplayName", pageData).text();
        var organizationKey = $("#selectedOrganizationExternalId", pageData).val();
        var organizationName = $("#orgName", pageData).text();
        protocolFields.addLeadOrganizationRow(organizationKey, organizationName, personKey, personName);
    }

    personSearch.clickSelectButton = function (person) {
        $('#selectedPersonExternalId').val(person.externalId);
        $("#selectPerson > div").each(function (index) {
            if (index === SELECTION_SECTION) {
                showSection(this);
                principalInvestigator.setPerson(person);
            } else {
                hideSection(this);
            }
            clearMessages();
        });
    }
    $("#selectPerson .searchAgainLink").click(getSearchAgainFunction("selectPerson"));
    $("#searchPerson #createNewPerson").click(getCreateNewFunction("selectPerson"));

    organizationSearch.clickSelectButton = function (organization) {
        $('#selectedOrganizationExternalId').val(organization.externalId);
        $("#selectOrganization > div").each(function (index) {
            if (index === SELECTION_SECTION) {
                showSection(this);
                $(this).data("selection", organization);
                organizationDisplay.setOrganization(organization);
            } else {
                hideSection(this);
            }
            clearMessages();
        });
    };
    $("#selectOrganization .searchAgainLink").click(getSearchAgainFunction("selectOrganization"));
    $("#searchOrganization #createNewOrganization").click(getCreateNewFunction("selectOrganization"));
    $("#leadOrganizationAccordion").accordion({ heightStyle: "content" });

    function showNecessarySection(hasSelected, createNew, sectionId) {
        if (hasSelected) {
            showSection("#" + sectionId + " .selection_section");
        } else if (createNew) {
            showSection("#" + sectionId + " .create_new_section");
        } else {
            showSection("#" + sectionId + " .search_section");
        }
    }

    $(document).ready(function () {
        showNecessarySection(<s:property value='%{organization.externalId != null}'/>,
                <s:property value='%{createNewOrganization}'/>, "selectOrganization")
        showNecessarySection(<s:property value='%{principalInvestigator.externalId != null}'/>,
                <s:property value='%{createNewPerson}'/>, "selectPerson")
        wrapDataInFormsIfNecessary();
        addErrorMessagesIfNecessary();
        $("input").keydown(testForEnter);
    });
</script>
</firebird:dialogLayout>
