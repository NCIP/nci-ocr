<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ attribute name="form" rtexprvalue="true" required="true" type="gov.nih.nci.firebird.data.AbstractRegistrationForm"%>

<s:a id="nextFormButton_%{form.formType.formTypeEnum.name()}" href="#" onclick="selectNextTab()" cssClass="button nextFormButton float_right" />
<div class="clear"></div>

<script>

  $(function() {
    addNextButtonIfNeccessary()
  });

  function addNextButtonIfNeccessary() {
    var curTab = $("#tabwrapper .ui-tabs-loading");
    var nextTab = curTab.next();
    if (nextTab.length === 0) {
      $(".nextFormButton").remove();
    } else {
      var nextText = '<s:property value="%{getText(\"button.next\")}"/>';
      var nextTabText = nextTab.text();
      var nextButtonText = nextText + " (" + nextTabText + ")";
      $(".nextFormButton").text(nextButtonText);
    }
  }

  function selectNextTab() {
    var nextTabIndex = getCurrentSelectedTabIndex() + 1;
    $("#tabwrapper").tabs("option", "selected", nextTabIndex);
  }

</script>