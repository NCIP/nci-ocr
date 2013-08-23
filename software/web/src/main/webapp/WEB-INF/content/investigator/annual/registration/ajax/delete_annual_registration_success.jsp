<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<firebird:dialogLayout>
    <firebird:messages />
    <firebird:dialogHeader>
        <fmt:message key="investigator.delete.annual.registration.success.title" />
    </firebird:dialogHeader>
    <p><fmt:message key="investigator.delete.annual.registration.success.message" /></p>
    <div class="btn_bar clear">
        <s:a id="closeBtn" cssClass="button" action="enterBrowseAnnualRegistrations" namespace="/investigator/annual/registration">
            <fmt:message key="button.close"/>
        </s:a>
    </div>
</firebird:dialogLayout>

<script>

$(function() {
  enableDialog();
});

</script>