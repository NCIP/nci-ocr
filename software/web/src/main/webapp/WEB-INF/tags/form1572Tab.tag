<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="instructionsKey" required="true"%>
<%@ attribute name="introKey" required="true"%>

<s:set name="namespace" value="@com.opensymphony.xwork2.ActionContext@getContext().actionInvocation.proxy.namespace"></s:set>
<firebird:messages/>
<h2 class="clear"><fmt:message key="registration.fda1572.title"/> </h2>
<s:if test="readOnly">
    <div id="lockedView1572PdfButtonTopDiv">
        <firebird:viewGeneratedFormButton buttonId="view1572PdfButtonTop" form="${form}" useName="true"/>
    </div>
</s:if>
<s:else>
    <div id="unlockedView1572PdfButtonTopDiv" class="formcol_narrow">
        <firebird:viewGeneratedFormButton buttonId="view1572PdfButtonTop" form="${form}" useName="true"/>
        <s:if test="%{registration.form1572.comments != null && !registration.form1572.comments.empty}">
            <firebird:comments retainState="true">
                ${registration.form1572.comments}
            </firebird:comments>
        </s:if>
    </div>
    <div class="line"></div>
    <div class="formcol_row">
        <fmt:message key="${introKey}" />
    </div>
    <br/>
    <firebird:instructionBubble messageKey="${instructionsKey}" id="1572_instructions"/>
    <div id="fda1572_practiceSites" class="credentialSection">
        <s:action name="view" namespace="%{namespace}/fda1572/practiceSite" executeResult="true">
            <s:param name="registration.id" value="registration.id" />
        </s:action>
    </div>
    <br/>

    <div id="fda1572_labs" class="credentialSection">
        <s:action name="view" namespace="%{namespace}/fda1572/lab" executeResult="true">
            <s:param name="registration.id" value="registration.id" />
        </s:action>
    </div>
    <br/>

    <div id="fda1572_irb" class="credentialSection">
        <s:action name="view"  namespace="%{namespace}/fda1572/irb" executeResult="true">
            <s:param name="registration.id" value="registration.id" />
        </s:action>
    </div>

    <jsp:doBody />

    <div>
        <firebird:viewGeneratedFormButton buttonId="view1572PdfButtonBottom" form="${form}" useName="true"/>
    </div>
    <firebird:nextTabButton form="${form}" />
</s:else>
