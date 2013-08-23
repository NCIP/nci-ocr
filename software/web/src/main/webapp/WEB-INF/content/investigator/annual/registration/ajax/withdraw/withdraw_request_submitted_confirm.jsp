<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <firebird:dialogHeader><fmt:message key="investigator.withdraw.request.submitted.title" /></firebird:dialogHeader>

    <p><fmt:message key="investigator.withdraw.request.submitted.message" /></p>

    <div class="btn_bar">
        <s:a id="closeBtn" href="#" cssClass="button float_left" onclick="closeDialogAndReload();">
            <fmt:message key="button.close"/>
        </s:a>
        </div>
</firebird:dialogLayout>
