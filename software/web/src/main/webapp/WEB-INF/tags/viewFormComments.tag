<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="sponsor.protocol.forms.reject.comment.header">
            <fmt:param value="${form.formType.description}" />
        </fmt:message>
    </firebird:dialogHeader>

    <div id="comments" class="richText">
        <s:property value="%{cleanFormComments}" escape="false"/>
    </div>
    <div class="clear"></div>

    <div class="btn_bar">
        <s:a href="#" id="closeCommentsBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.close"/></s:a>
    </div>
</firebird:dialogLayout>
