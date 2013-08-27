<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.person.search.title"/></h1>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form id="selectPersonForm" action="currentStep" onsubmit="return false">
                <s:hidden id="selectedPersonKey" name="selectedPersonKey" />
            </s:form>
            <firebird:personSearch labelKey="person.search.label" instructionsKey="person.search.instructions.current.user" setFocus="true"/>
            <div class="btn_bar">
                <firebird:userRegistrationPreviousButton/>
                <s:if test="%{#session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@ACCOUNT_CONFIGURATION_DATA].person != null}">
                    <s:a id="cancelBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.cancel"/></s:a>
                </s:if>
                <s:a id="createNewBtn" cssClass="button" href="javascript:void(0)"><fmt:message key="button.create.new"/></s:a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        personSearch.clickSelectButton = function(personId) {
            $('#selectedPersonKey').val(personId);
            var url = $('form').first().attr('action');
            var serializedForm = $('form').serialize();
            var target = '#registrationContent';
            _fbUtil.performAjaxPost(url, target, serializedForm);
        };

        $('#cancelBtn').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>') });
        $('#createNewBtn').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>', {navigationOption : 'NEW'}) });

    });
</script>
