<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.investigator.selection.title"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <firebird:investigatorSearchSelect />
            <div class="btn_bar clear">
                <firebird:userRegistrationPreviousButton/>
                <firebird:userRegistrationNextButton displayButtonAlways="true"/>
            </div>
        </div>
    </div>
</div>

<s:url var="addUrl" action="addInvestigator"/>
<s:url var="deleteUrl" action="removeInvestigator"/>

<script type="text/javascript">
    <s:iterator value="accountConfigurationData.selectedInvestigators" var="investigatorProfile">
        _investigatorSelectionPage.addInvestigatorToList('<s:property value="#investigatorProfile.id"/>', '<s:property value="#investigatorProfile.person.displayName"/>');
    </s:iterator>

    _investigatorSelectionPage.saveAddition = function(profileId) {
        return _investigatorSelectionPage.saveChange('<s:property value="addUrl"/>', profileId);
    };

    _investigatorSelectionPage.saveRemoval = function(profileId) {
        return _investigatorSelectionPage.saveChange('<s:property value="deleteUrl"/>', profileId);
    };

    _investigatorSelectionPage.saveChange = function(ajaxUrl, profileId) {
        var isSuccessful = false;
        $.ajax({
            type: 'POST',
            url: ajaxUrl,
            data: {'profileId' : profileId},
            async: false,
            success: function() {
                isSuccessful = true;
            }
        });
        return isSuccessful;
    };

</script>
