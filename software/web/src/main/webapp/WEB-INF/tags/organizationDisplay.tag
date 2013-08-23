<%@ tag body-content="empty" %>
<%@ attribute name="organization" type="gov.nih.nci.firebird.data.Organization" rtexprvalue="true" required="true" %>
<%@ attribute name="tagVariableName" description="Variable name for this organization display tag and enclosing div ID" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<div id="${tagVariableName}">
    <div class="formcol">
        <firebird:label messageKey="label.name" bold="true" />
        <div id="orgName">${organization.name}</div>

        <firebird:label messageKey="label.address" bold="true" />
        <div id="orgAddress">
            <s:if test="%{#attr.tagVariableName not in #{null,''}}">
                <firebird:addressDisplay address="${organization.postalAddress}" tagVariableName="${tagVariableName}Address"/>
            </s:if><s:else>
                <firebird:addressDisplay address="${organization.postalAddress}" />
            </s:else>
        </div>
    </div>

    <div class="formcol">
        <firebird:label messageKey="label.email" bold="true" />
        <div id="orgEmail">${organization.email}</div>

        <firebird:label messageKey="label.phone" bold="true" />
        <div id="orgPhone">${organization.phoneNumber}</div>

        <firebird:label messageKey="label.ctep.id" bold="true" />
        <div id="orgCtepId">${organization.ctepId}</div>
    </div>
</div>

<s:if test="%{#attr.tagVariableName not in #{null,''}}">
    <script>
        var ${tagVariableName} = {
            setOrganization: function(organization) {
              $("#${tagVariableName} #orgName").html(organization.name);
              ${tagVariableName}Address.setAddress(organization.postalAddress);
              $("#${tagVariableName} #orgEmail").html(organization.email);
              $("#${tagVariableName} #orgPhone").html(organization.phoneNumber);
              $("#${tagVariableName} #orgCtepId").html(organization.ctepId);
            }
        };
    </script>
</s:if>
