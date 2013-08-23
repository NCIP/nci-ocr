<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>

<script type="text/javascript">
    closeDialog({dialogId : "${dialogId}", reloadPage : ${empty reloadPage ? false : reloadPage}, reload : ${reload}, tabSet : "${tabset}"});
    var errors = [];
    <s:iterator value="actionErrors" var="error">
        errors.push('<s:property value="error"/>');
    </s:iterator>

    setPageErrorMessages(errors);
</script>
