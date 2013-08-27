<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:set var="nesId" value="profile.primaryOrganization.organization.nesId"/>
<s:if test="%{#nesId == null || #nesId == ''}">
   <fmt:message var="title" key="investigator.profile.organization.create" />
</s:if>
<s:else>
   <fmt:message var="title" key="investigator.profile.organization.update" />
</s:else>
<firebird:messages/>
<s:form namespace="/investigator/profile/contact/ajax" action="createPrimaryOrganization" id="profileForm">

    <div id="tabwrapper">
        <div class="ui-tabs">
            <div class="ui-tabs-panel">
                <firebird:dialogHeader>
                    ${title}<fmt:message key="label.separator"/>
                </firebird:dialogHeader>

                <div class="formcol clear">
                    <s:select id="primaryOrganizationType"
                              name="primaryOrganizationType"
                              list="%{@gov.nih.nci.firebird.data.PrimaryOrganizationType@values()}"
                              listValue="display"
                              headerValue="%{getText('select.type.default.option')}"
                              headerKey=""
                              label="%{getText('label.type')}"
                              requiredLabel="true"/>

                </div>
                <div class="clear"><br></div>

                <div class="formcol">
                    <s:textfield id="profile.primaryOrganization.organization.name" name="profile.primaryOrganization.organization.name" maxlength="160" size="30" cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}" requiredLabel="true"/>
                </div>

                <div class="dotted_line"><br></div>

                <div class="formcol">
                    <s:textfield id="profile.primaryOrganization.organization.email" name="profile.primaryOrganization.organization.email" maxlength="50" size="30" cssStyle="width: 19em;" label="%{getText('textfield.emailAddress')}" requiredLabel="true" requiredPosition="right"/>
                </div>

                <div class="formcol">
                    <firebird:phoneNumber beanPrefix="profile.primaryOrganization.organization" required="${profile.user.ctepUser}"/>
                </div>

                <div class="dotted_line"><br></div>

                <firebird:addressFields beanPrefix="profile.primaryOrganization.organization.postalAddress" />

                <s:hidden name="profile.userDn"/>
                <s:hidden name="profile.id" value="%{profile.id}"/>

                <div class="clear"><br></div>
                <div class="btn_bar">
                    <img id="loadingIcon" src="${pageContext.request.contextPath}/images/loading.gif" alt="Loading..." style="display:none"/>

                    <sj:a id="saveOrgButton" value="Save" formIds="profileForm" targets="profileDialog" cssClass="button" indicator="loadingIcon"
                      onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError" href="#"><fmt:message key="button.save"/></sj:a>
                    <s:a id="cancelOrgButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
                </div>
            </div>
        </div>
    </div>

</s:form>
