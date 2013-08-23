<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="messageKey" required="true"%>
<%@ attribute name="required" required="false" type="java.lang.Boolean"%>
<%@ attribute name="forId"%>
<%@ attribute name="showSeparator" type="java.lang.Boolean" %>
<%@ attribute name="bold" type="java.lang.Boolean" %>

<s:if test="%{#attr.required || #attr.bold}">
    <s:set var="labelClass" value="'requiredLabel'"/>
</s:if>
<s:else>
    <s:set var="labelClass" value="''"/>
</s:else>

<s:if test="#attr.forId in {null, ''}">
  <br>
    <span class="<s:property value='%{#labelClass}'/>">
        <s:if test="%{#attr.required}">
            <span class="required">*</span>
        </s:if>
        <fmt:message key="${messageKey}"/>
        <s:if test="%{#attr.showSeparator == null || #attr.showSeparator == true}">
            <fmt:message key="label.separator"/>
        </s:if>
    </span>
</s:if><s:else>
    <label for="${forId}" class="<s:property value='%{#labelClass}'/>">
        <s:if test="%{#attr.required}">
            <span class="required">*</span>
        </s:if>
        <fmt:message key="${messageKey}"/>
        <s:if test="%{#attr.showSeparator == null || #attr.showSeparator == true}">
            <fmt:message key="label.separator"/>
        </s:if>
    </label>
</s:else>
