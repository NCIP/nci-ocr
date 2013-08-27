<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<!--Content-->
<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="person.association.add.subinvestigator" />:
    </firebird:dialogHeader>
    <firebird:messages/>
    <s:form action="select" id="selectSubinvestigatorForm" onsubmit="return false">
        <s:hidden name="profile.id" value="%{profile.id}"/>
        <s:hidden id="selectedPersonKey" name="selectedPersonKey" />

        <s:if test="%{registration.id != null}">
            <s:hidden name="registration.id"/>
        </s:if>
        <firebird:personSearch labelKey="person.search.label" setFocus="true">
            <s:a id="createNewButton" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
            <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
        </firebird:personSearch>
    </s:form>

    <script type="text/javascript">

    <s:url var="createNewUrl" action="enterFields" />
    $(document).ready(function() {
      personSearch.clickSelectButton = function(personId) {
            $('#selectedPersonKey').val(personId);
            var url = $('#selectSubinvestigatorForm').attr('action');
            performClick(url);
        };

        function performClick(url) {
          var serializedForm = $('#selectSubinvestigatorForm').serialize();
            var target = getCurrentDialog();
            _fbUtil.performAjaxPost(url, target, serializedForm);
        }

        $("#createNewButton").click(function() {
          $('#selectedPersonKey').val("");
            var url = '${createNewUrl}';
            performClick(url);
        });
        $("input").keydown(testForEnter);
    });

    </script>
</firebird:dialogLayout>
