<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h1><fmt:message key="user.registration.role.selection.title"/></h1>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <s:form id="roleSelectionForm" action="nextStep">
                <s:fielderror>
                    <s:param>selectedRoles</s:param>
                </s:fielderror>
                <s:checkboxlist name="selectedRoles" list="availableRoles" listValue="display" template="checkboxlistVertical"/>
            </s:form>
            <div class="btn_bar">
                <firebird:userRegistrationNextButton displayButtonAlways="true"/>
            </div>
        </div>
    </div>
</div>

<script>
var selectedRoles = getSelectedValues();

function getSelectedValues() {
    var vals = [];
    $('[name="selectedRoles"]:checked').each(function() {
       vals.push($(this).val());
    });
    return vals.join(',');
}

$(document).ready(function() {
    selectAndDisablePreselectedRole();
    $('[name="selectedRoles"]').click(function() {
        var checkedBoxes = getSelectedValues();
        if(checkedBoxes === selectedRoles) {
            $('#returnToVerificationLink').show();
        } else {
            $('#returnToVerificationLink').hide();
        }
    });
});

function selectAndDisablePreselectedRole() {
  var preselectedRole = "${accountConfigurationData.preselectedRole}";
  if (preselectedRole != "") {
      $('[value=' + preselectedRole + ']').prop("checked", true).attr("disabled", "disabled");
       $('<input>').attr({
          type: 'hidden',
          name: 'selectedRoles',
          value: preselectedRole
      }).appendTo($('#roleSelectionForm'));
  }
}
</script>