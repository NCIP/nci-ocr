<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:messages/>
<firebird:viewPerson person="${investigator}"/>
<div class="clear"></div>
<div id="addButton" class="btn_bar">
    <s:a href="#" cssClass="button"><fmt:message key="button.add"/></s:a>
</div>
<script type="text/javascript">

    $(document).ready(function() {
        <s:url action="addInvestigator" namespace="/sponsor/representative/protocol/ajax" var="addInvestigatorUrl">
            <s:param name="protocol.id">${protocol.id}</s:param>
        </s:url>

        $("#addButton").click( function(){
            indicateLoading(true);
            $.post("${addInvestigatorUrl}",{ selectedPersonKey: $("#selectedPersonKey").val() }, function(data) {
                $('#investigatorInfoContent').html(data);
                indicateLoading(false);
                var auto = $("#investigatorAutoComplete");
                auto.val("");
                auto.focus();
            });
        });
    });

</script>