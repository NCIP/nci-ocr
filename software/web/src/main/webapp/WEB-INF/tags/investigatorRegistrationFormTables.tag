<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="registration" rtexprvalue="true" required="true"
    type="gov.nih.nci.firebird.data.AbstractRegistration"%>

<div class="boldHeader"><fmt:message key="label.required.forms" /></div>
    <div id="requiredFormsTable" class="annualRegistrationFormsTable">
        <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@REQUIRED}" var="form">
            <firebird:formListing form="${#form}" />
        </s:iterator>
    </div>
    <div class="clear"></div>
    <s:if test="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@OPTIONAL}.size > 0">
        <br/>
        <div class="boldHeader"><fmt:message key="label.optional.forms" /></div>
        <div id="optionalFormsTable" class="annualRegistrationFormsTable">
            <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@OPTIONAL}" var="form">
                <firebird:formListing form="${#form}" />
            </s:iterator>
        </div>
        <div class="clear"></div>
    </s:if>
    <s:if test="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@SUPPLEMENTARY}.size > 0">
        <br/>
        <div class="boldHeader"><fmt:message key="label.supplemental.forms" /></div>
        <div id="supplementalFormsTable" class="annualRegistrationFormsTable">
            <s:iterator value="registration.forms.{?#this.formOptionality == @gov.nih.nci.firebird.data.FormOptionality@SUPPLEMENTARY}" var="form">
                <firebird:formListing form="${#form}" />
            </s:iterator>
        </div>
        <div class="clear"></div>
    </s:if>