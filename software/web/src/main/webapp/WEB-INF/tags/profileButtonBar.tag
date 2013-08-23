<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="form" required="true" %>
<%@ attribute name="idPrefix" required="true" %>
<%@ attribute name="isDelete" required="false" %>
<%@ attribute name="dialogId" rtexprvalue="true" required="false" %>

<s:set var="saveButton" value = "%{#attr.idPrefix + 'SaveBtn'}"/>

<s:set var="cancelButton" value="%{#attr.idPrefix + 'CancelBtn'}"/>

<s:set var="dialogId" value="%{#attr.dialogId in {null, ''} ? 'profileDialog' : #attr.dialogId}" />

<div class="btn_bar clear">
    <s:if test="%{#attr.isDelete}">
        <sj:a id="%{#saveButton}" value="Delete" formIds="%{#attr.form}"
        targets="%{dialogId}" cssClass="button" href="#"
        onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.delete"/></sj:a>
    </s:if>
    <s:else>
        <sj:a id="%{#saveButton}" value="Save" formIds="%{#attr.form}"
        targets="%{dialogId}" cssClass="button" href="#"
        onSuccessTopics="enable" onClickTopics="submit"
        onCompleteTopics="closeIfComplete" onErrorTopics="ajaxError"><fmt:message key="button.save"/></sj:a>
    </s:else>

    <s:a id="%{#cancelButton}" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
</div>

<script type="text/javascript">
$(document).ready(function() {
    $.subscribe('closeIfComplete', function(event,data) {
        if($('.ui-dialog .errorMessage,.ui-dialog .fielderror').length == 0) {
            closeDialogAndReload();
        }
    });
});
</script>
