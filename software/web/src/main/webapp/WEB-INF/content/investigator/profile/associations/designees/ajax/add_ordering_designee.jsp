<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="person.association.add.ordering.designee" />:
    </firebird:dialogHeader>
    <firebird:messages />
    <s:form id="selectOrderingDesigneeForm" action="selectOrderingDesignee" onsubmit="return false">
        <s:hidden name="profile.id" value="%{profile.id}" />
        <s:hidden id="selectedPersonExternalId" name="selectedPersonExternalId" />

        <firebird:personSearch labelKey="person.search.label" setFocus="true">
            <s:a id="createNewButton" cssClass="button" href="javascript:void(0);"><fmt:message key="button.create.new"/></s:a>
            <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
        </firebird:personSearch>
    </s:form>

<script type="text/javascript">

    <s:url var="createNewUrl" action="enterOrderingDesigneeFields" />
    $(document).ready(function() {
        personSearch.clickSelectButton = function(person) {
            $('#selectedPersonExternalId').val(person.externalId);
            var url = $('#selectOrderingDesigneeForm').attr('action');
            performClick(url);
        };

        function performClick(url) {
            var serializedForm = $('#selectOrderingDesigneeForm').serialize();
            var target = getCurrentDialog();
            _fbUtil.performAjaxPost(url, target, serializedForm);
        }

        $("#createNewButton").click(function() {
            $('#selectedPersonExternalId').val("");
            var url = '${createNewUrl}';
            performClick(url);
        });
        $("input").keydown(testForEnter);
    });

    </script>
</firebird:dialogLayout>
