<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages />
            <firebird:dialogHeader>
                <fmt:message key="sponsor.verification.pending.title" />
            </firebird:dialogHeader>

            <fmt:message key="sponsor.verification.pending.message" />

            <ul>
                <s:iterator value="unverifiedSponsorRoles">
                    <li>
                        <s:property value="sponsor.name" />
                        (<s:if test="delegate"><fmt:message key="sponsor.delegate.role.text" /></s:if><s:else><fmt:message key="sponsor.representative.role.text" /></s:else>)
                    </li>
                </s:iterator>
            </ul>

            <div class="btn_bar clear">
                <s:a href="#" id="closeBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
            </div>
        </div>
    </div>
</div>
