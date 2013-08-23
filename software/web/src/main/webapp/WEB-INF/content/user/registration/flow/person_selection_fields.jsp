<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<h1><fmt:message key="user.registration.person.edit.title" /></h1>
<!--Content-->
<s:form action="nextStep" id="personSelectionForm">
    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <firebird:messages/>
                <s:if test="personAutoselected">
                    <s:set var="searchLinkKey" value="%{'user.registration.person.autoselected.search.link'}" />
                    <firebird:instructionBubble messageKey="user.registration.person.autoselected.text" />
                </s:if>
                <s:else>
                    <s:set var="searchLinkKey" value="%{'button.searchAgain'}" />
                </s:else>
                <firebird:managePersonFormFields beanPrefix="accountConfigurationData.person" showProviderNumber="true" requirePhoneNumber="true"/>

                <div class="clear"><br></div>
                <div class="btn_bar">
                    <a id="searchDifferentPersonLink" class="search btn" href="#"><s:property value="%{getText(#searchLinkKey)}"/> </a>
                    <firebird:userRegistrationPreviousButton/>
                    <firebird:userRegistrationNextButton/>
                </div>
            </div>
        </div>
    </div>
</s:form>

<script>

$(document).ready(function(){
    $('#searchDifferentPersonLink').click(function() { clickUserRegistrationSelectButton('<s:url action="currentStep"/>', {navigationOption :'search'}) });
});

</script>