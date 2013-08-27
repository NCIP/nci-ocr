<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="registration" rtexprvalue="true" required="true"
type="gov.nih.nci.firebird.data.AbstractRegistration"%>
<%@ attribute name="titleKey"%>
<%@ attribute name="retainState" type="java.lang.Boolean"%>

<s:if test="%{registration.sponsorComments not in {null, ''}
        || registration.investigatorComments not in {null, ''}
        || registration.coordinatorComments not in {null, ''}}">
    <firebird:comments titleKey="${titleKey}" retainState="${retainState}">
        <s:if test="%{registration.sponsorComments not in {null, ''}}">
            <h5><fmt:message key="sponsor.comments.header" /></h5>
            <s:property value="%{@gov.nih.nci.firebird.common.RichTextUtil@cleanRichText(registration.formComments)}" escapeHtml="false"/>
            <b><fmt:message key="label.additional.comments"/></b>
            <s:property value="%{@gov.nih.nci.firebird.common.RichTextUtil@cleanRichText(registration.sponsorComments)}" escapeHtml="false"/>
        </s:if>
        <s:if test="%{registration.investigatorComments not in {null, ''}}">
            <h5><fmt:message key="investigator.comments.header" /></h5>
            <s:property value="%{@gov.nih.nci.firebird.common.RichTextUtil@cleanRichText(registration.investigatorComments)}" escapeHtml="false"/>
        </s:if>
        <s:if test="%{registration.coordinatorComments not in {null, ''}}">
            <h5><fmt:message key="coordinator.comments.header" /></h5>
            <s:property value="%{@gov.nih.nci.firebird.common.RichTextUtil@cleanRichText(registration.coordinatorComments)}" escapeHtml="false"/>
        </s:if>
    </firebird:comments>
</s:if>
