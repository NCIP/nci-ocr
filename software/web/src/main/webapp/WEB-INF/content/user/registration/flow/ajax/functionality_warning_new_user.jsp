<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:messages/>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:dialogHeader><fmt:message key="user.registration.functionality.warning.title"/></firebird:dialogHeader>
            <p><fmt:message key="user.registration.functionality.warning.subtext"/></p>

            <s:iterator value="unverifiedRoles" var="role">
                <h3><s:property value="#this.display"/></h3>
                <div class="richText clear">
                    <s:property value="%{getText('user.registration.functionality.warning.' + #this.name())}" escapeHtml="false"/>
                </div>
            </s:iterator>

            <br/><br/>
            <p><fmt:message key="functionality.warning.helpdesk.contact.info"/></p>

            <div class="btn_bar clear">
                <a id="acknowledgeBtn" class="button" href="#" onclick="closeDialog();"><fmt:message key="button.confirm"/></a>
            </div>
        </div>
    </div>
</div>