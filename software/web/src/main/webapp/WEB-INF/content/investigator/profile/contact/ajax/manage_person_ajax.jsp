<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:set var="nesId" value="profile.person.nesId"/>
<fmt:message var="title" key="investigator.profile.person.update" />
<s:url var="action" value="updatePersonAjax"/>

<firebird:messages/>
<s:form namespace="/investigator/profile/contact/ajax" action="%{#action}" id="profileForm">
    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <firebird:dialogHeader>
                    <fmt:message key="investigator.profile.person.update" /><fmt:message key="label.separator"/>
                </firebird:dialogHeader>

                <firebird:instructionBubble messageKey="investigator.profile.person.update.message"/>

                <firebird:managePersonFormFields showProviderNumber="true" beanPrefix="person"/>

                <div class="clear"><br></div>
                <div class="btn_bar">
                    <img id="loadingIcon" src="${pageContext.request.contextPath}/images/loading.gif" alt="Loading..." style="display:none"/>

                    <sj:a id="saveButton" value="Save" formIds="profileForm" targets="profileDialog" cssClass="button" indicator="loadingIcon"
                      onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError" href="#"><fmt:message key="button.save"/></sj:a>
                    <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
            </div>
        </div>
    </div>
</s:form>
