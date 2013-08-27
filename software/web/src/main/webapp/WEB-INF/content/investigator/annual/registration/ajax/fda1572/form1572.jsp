<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<firebird:form1572Tab instructionsKey="annual.registration.fda1572.help.text" introKey="annual.registration.fda1572.intro">
    <br/>
    <h2>
        <span id="phaseSelectionAsterisk" class="validationErrorAsterisk" style="display:none">*</span>
        <fmt:message key="annual.registration.1572.phase.selection.header"/>
    </h2>
    <div class="onecol">
        <br/>
        <s:form id="phaseSelectionForm" action="save" namespace="/investigator/annual/registration/ajax/fda1572">
            <s:checkbox id="phaseOne" name="registration.form1572.phaseOne" onclick="updatePhaseSelections()" label="%{getText('annual.registration.1572.phase.one.label')}" labelposition="right"></s:checkbox>
            <div class="clear"></div>
            <br/>
            <s:checkbox id="phaseTwoOrThree" name="registration.form1572.phaseTwoOrThree" onclick="updatePhaseSelections()" label="%{getText('annual.registration.1572.phase.two.or.three.label')}" labelposition="right"></s:checkbox>
            <div class="clear"></div>
            <s:hidden name="registration.id" value="%{registration.id}"/>
        </s:form>
    </div>
    <br/>
</firebird:form1572Tab>

<s:if test="!registration.lockedForInvestigator">
    <firebird:nextTabButton form="${form}" />
</s:if>

<script>
var incomplete = '<s:property value="@gov.nih.nci.firebird.data.RegistrationStatus@INCOMPLETE"/>';

$(new function() {
  checkForNoPhaseSelectionsIfIncomplete();
});

function updatePhaseSelections() {
  indicateLoading(true);
    $.post($('#phaseSelectionForm').attr("action"),$('#phaseSelectionForm').serialize(), function(errorMessages) {
      setPageErrorMessages(errorMessages);
      checkForNoPhaseSelectionsIfIncomplete();
      indicateLoading(false);
    });
}

function checkForNoPhaseSelectionsIfIncomplete() {
    var registrationStatus = "${registration.status}";
    if (registrationStatus === incomplete) {
        var showIrbAsterisk = $("#phaseSelectionForm input:checked").length == 0;
        $("#phaseSelectionAsterisk").toggle(showIrbAsterisk);
    }
  }
</script>