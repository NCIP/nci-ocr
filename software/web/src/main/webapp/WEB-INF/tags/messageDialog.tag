<%@ tag body-content="scriptless"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="titleKey" required="true"%>
<%@ attribute name="messageKey"%>
<%@ attribute name="reloadOnClose" type="java.lang.Boolean"
    description="Whether or not to reload the underlying page when the close button is clicked. Default is true" %>

<firebird:dialogLayout>
    <firebird:messages />
    <firebird:dialogHeader>
        <fmt:message key="${titleKey}" />
    </firebird:dialogHeader>

    <s:if test="%{#attr.messageKey not in {null, ''}}">
        <p><fmt:message key="${messageKey}" /></p>
    </s:if>

    <jsp:doBody />

    <div class="btn_bar clear">
        <s:if test="%{#attr.reloadOnClose == null || #attr.reloadOnClose}">
            <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialogAndReload();"><fmt:message key="button.close"/></s:a>
        </s:if>
        <s:else>
            <s:a id="closeBtn" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
        </s:else>
    </div>
</firebird:dialogLayout>
