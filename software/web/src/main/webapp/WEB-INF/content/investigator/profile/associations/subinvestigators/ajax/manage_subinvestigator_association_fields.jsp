<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="person.association.add.subinvestigator" />:
    </firebird:dialogHeader>
    <firebird:messages/>
    <firebird:searchAgainLink/>

  <s:form action="save" id="subinvestigatorForm">
      <div class="clear"><br></div>
      <s:if test="%{registration.id != null}">
          <s:hidden name="registration.id"/>
      </s:if>

      <firebird:managePersonFormFields beanPrefix="subinvestigator" fieldsDisabled="${fieldsDisabled}"/>

      <div class="clear"><br></div>
      <div class="btn_bar">
            <a id="saveButton" href="javascript:void(0);" class="button"><fmt:message key="button.save"/></a>
            <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
      </div>

  </s:form>
</firebird:dialogLayout>
<s:url var="searchAssociatedOrgUrl" action="enterSearch" />
<script>
$(document).ready(function() {
    $('.searchAgainLink').click(function() {
        performClick('<s:url action="enterSearch"/>');
     });

    function performClick(url) {
        var formData = $('#subinvestigatorForm').serialize();
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target, formData);
    }

    $('#saveButton').click(function() {
        var url = $('#subinvestigatorForm').attr('action');
        performClick(url);
     });
    $("input").keydown(testForEnter);
});
</script>