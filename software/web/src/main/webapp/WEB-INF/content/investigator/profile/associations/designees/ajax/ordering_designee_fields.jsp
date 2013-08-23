<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:dialogLayout>
    <firebird:dialogHeader>
        <fmt:message key="person.association.add.ordering.designee" />:
    </firebird:dialogHeader>
    <firebird:messages />
    <firebird:searchAgainLink/>

    <s:form action="saveOrderingDesignee" id="orderingDesigneeForm">
        <div class="clear"><br></div>

        <firebird:managePersonFormFields beanPrefix="orderingDesignee" fieldsDisabled="${fieldsDisabled}"/>

        <div class="clear"><br></div>
        <div class="btn_bar">
            <a id="saveButton" href="javascript:void(0);" class="button"><fmt:message key="button.save"/></a>
            <s:a id="cancelButton" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
        </div>

    </s:form>
</firebird:dialogLayout>
<script>
$(document).ready(function() {
    $('.searchAgainLink').click(function() {
        performClick('<s:url action="enterAddOrderingDesignee"/>');
     });

    function performClick(url) {
        var formData = $('#orderingDesigneeForm').serialize();
        var target = getCurrentDialog();
        _fbUtil.performAjaxPost(url, target, formData);
    }

    $('#saveButton').click(function() {
        var url = $('#orderingDesigneeForm').attr('action');
        performClick(url);
     });
    $("input").keydown(testForEnter);
});
</script>