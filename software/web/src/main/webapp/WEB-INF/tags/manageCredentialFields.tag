<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="saveAction" required="true"%>
<%@ attribute name="searchAgainAction" required="true"%>
<%@ attribute name="organizationPrefix" required="true"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>
            <s:form action="%{#attr.saveAction}" id="credentialForm">

                <jsp:doBody/>

               <div class="clear"><br></div>

               <span style="float: right; padding-top: .5em;">
                   <s:url action="%{#attr.searchAgainAction}"  var="credentialOrganizationSearchUrl" />
                   <sj:a targets="profileDialog" href="%{credentialOrganizationSearchUrl}"
                       formIds="credentialForm" cssClass="search" id="credentialOrganizationSearch" onClickTopics="submit" onSuccessTopics="enable"
                       onErrorTopics="ajaxError">
                   <fmt:message key="button.searchAgain" /></sj:a>
               </span>
               <s:hidden name="issuerSearchKey"/>
               <s:if  test="%{!#parameters.containsKey('issuerSearchKey') || issuerSearchKey not in #{null, ''}}" >
                    <firebird:organizationDisplay organization="${credential.issuer}" />
                </s:if>
                <s:else>
                    <div class="formcol">
                       <s:textfield id="%{#attr.organizationPrefix}.issuer.name" name="%{#attr.organizationPrefix}.issuer.name" maxlength="160" size="30" requiredLabel="true"
                           cssStyle="width: 19em;" label="%{getText('textfield.organization.name')}"
                          />
                    </div>

                    <div class="dotted_line"><br></div>

                    <div class="formcol">
                       <s:textfield id="%{#attr.organizationPrefix}.issuer.email" name="%{#attr.organizationPrefix}.issuer.email" maxlength="50" size="30" cssStyle="width: 19em;"
                           label="%{getText('textfield.emailAddress')}" requiredLabel="true"/>
                    </div>

                    <div class="formcol">
                        <firebird:phoneNumber beanPrefix="${organizationPrefix}.issuer"/>
                    </div>

                    <div class="dotted_line"><br></div>

                    <firebird:addressFields beanPrefix="${organizationPrefix}.issuer.postalAddress" />

                </s:else>

                <firebird:profileButtonBar idPrefix="manageCredential" form="credentialForm"/>

                <s:hidden name="profile.id" value="%{profile.id}"/>

            </s:form>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
      $("input").keydown(testForEnter);
    });
</script>
