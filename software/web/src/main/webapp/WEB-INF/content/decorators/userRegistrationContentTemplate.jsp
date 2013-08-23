<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>

<s:if test="newUser">
    <s:url var="cancelUrl" namespace="/login" action="logout" />
</s:if> <s:else>
    <s:url var="cancelUrl" namespace="/user" action="enterMyAccount"/>
</s:else>

<!--Breadcrumb Trail-->
<s:set value="@gov.nih.nci.firebird.web.common.FirebirdUIConstants@REGISTRATION_FLOW_CONTROLLER" var="controllerVar"/>
<s:set value="#session[#controllerVar]" var="controller" />
<s:set value="@gov.nih.nci.firebird.web.common.registration.RegistrationFlowStep@VERIFICATION" var="verificationStep" />
<div id="breadcrumb">
    <span class="buffer">&nbsp;</span>
    <s:iterator value="#controller.flowSteps" var="step">
        <s:if test="#step == #controller.currentStep">
            <span class="selected crumb"><s:property value="#step.display"/></span>
        </s:if>
        <s:elseif test="#step in #controller.visitedSteps">
            <span class="crumb"><s:property value="#step.display"/></span>
        </s:elseif>
        <s:else>
            <span class="disabled crumb"><s:property value="#step.display"/></span>
        </s:else>
    </s:iterator>
</div>
<!--/Breadcrumb Trail-->

<!-- Help Link -->
<firebird:helpButton/>
<!-- /Help Link -->

<!--Content-->
<div id="content_wrapper_outer">
    <div id="content_wrapper">
        <div id="content">
            <s:if test="%{#verificationStep in #controller.visitedSteps
                        && #controller.currentStep != #verificationStep}">
                <s:url var="verificationUrl" action="gotoStep">
                    <s:param name="flowStepToGoTo" value="#verificationStep"/>
                </s:url>
                <a id="returnToVerificationLink" href="#"><fmt:message key="user.registration.return.to.verification.link"/></a>
                <script>
                $(document).ready(function(){
                   $('#returnToVerificationLink').click(function(){
                       var url = '<s:property value="%{#verificationUrl}"/>';
                       var serializedForm = $('form').serialize();
                      $.post(url, serializedForm, function(data){
                          $('#registrationContent').html(data);
                      })
                   });
                });
                </script>
                <div class="clear"></div>
            </s:if>
            <decorator:body/>
            <div class="clear"><br></div>
            <s:a id="cancelBtn"  href="%{cancelUrl}" cssClass="btn"><fmt:message key="user.registration.cancel.registration.link" /></s:a>
        </div>
        <firebird:versionStamp/>
    </div>
</div>
<!--/Content-->