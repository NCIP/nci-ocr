<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<span class="vertical-align-super">
    <s:a id="backButton" cssClass="button" href="javascript:void(0)" onclick="navigateBack();"><fmt:message key="button.back"/></s:a>
</span>

<script>

var tabStack = new Array();
var backClicked = false;

function initiateBackButton() {
    selectPassedInFormsTab();
    $( "#tabwrapper" ).tabs({
         select: function(event, ui) {
             addTabToNavigationStack();
         }
     });
}

function selectPassedInFormsTab() {
    var form = "<s:property value='%{#parameters[\"form\"]}' />";
    if (form != "") {
        var selectedTab = getSelectedTabIndex(form);
        $("#tabwrapper").tabs("option", "active" , selectedTab);
    }
}

function addTabToNavigationStack() {
  if (!backClicked) {
    tabStack.push(getCurrentSelectedTabIndex());
  } else {
    backClicked = false;
  }
}

function getSelectedTabIndex(form) {
    var selectedTabIndex = 0;
    $(".tab").each(function(index, tab) {
        if ($(tab).attr("id") == "form_" + form + "_tab") {
          selectedTabIndex = index;
        }
    });
    return selectedTabIndex;
}

function navigateBack() {
    backClicked = true;
    var tabIndex = tabStack.pop();
    if (tabIndex != undefined) {
      $("#tabwrapper").tabs("option", "active", tabIndex);
    } else {
      history.go(-1);
    }
}

</script>