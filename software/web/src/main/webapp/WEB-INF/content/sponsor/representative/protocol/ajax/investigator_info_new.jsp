<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<!--Content-->
<firebird:messages/>
<s:form id="createInvestigatorForm" action="addInvestigator" namespace="/sponsor/representative/protocol/ajax">
    <div class="clear"><br></div>

    <firebird:managePersonFormFields beanPrefix="investigator"/>
    <s:hidden name="protocol.id"/>

    <div class="clear"><br></div>
    <div class="btn_bar">
        <img id="loadingIcon" src="${pageContext.request.contextPath}/images/loading.gif" alt="Loading..." class="hide"/>

        <sj:a id="saveCreateProtocolInvestigatorBtn" value="Save" formIds="createInvestigatorForm" targets="investigatorInfoContent" cssClass="button" indicator="loadingIcon"
              onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError" href="#"><fmt:message key="button.save"/></sj:a>
    </div>
</s:form>
