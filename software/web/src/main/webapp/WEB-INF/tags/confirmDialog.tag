<%@ tag body-content="scriptless"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="titleKey" required="true"%>
<%@ attribute name="confirmUrl" required="true"%>
<%@ attribute name="dialogId" rtexprvalue="true" required="false" %>

<firebird:dialogLayout>
    <firebird:messages />
    <firebird:dialogHeader>
        <fmt:message key="${titleKey}" />
    </firebird:dialogHeader>
    <jsp:doBody/>
    <div class="btn_bar clear">
        <sj:a id="confirmBtn" value="Confirm" cssClass="button"
            href="%{#attr.confirmUrl}" onClickTopics="submit" targets="%{#attr.dialogId}"
            onErrorTopics="ajaxError"><fmt:message key="button.confirm"/></sj:a>
        <s:a id="cancelBtn" href="# " cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
    </div>
</firebird:dialogLayout>