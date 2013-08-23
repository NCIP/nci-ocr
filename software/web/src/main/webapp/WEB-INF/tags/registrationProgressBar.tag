<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ attribute name="registration" rtexprvalue="true" required="true"
    type="gov.nih.nci.firebird.data.AbstractRegistration"%>

<div class="progressBar" id="registrationProgressBar"></div>

<script type="text/javascript">

var requiredText = '<s:property value="@gov.nih.nci.firebird.data.FormOptionality@REQUIRED.display"/>';
var completedText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@COMPLETED.display"/>';
var acceptedText = '<s:property value="@gov.nih.nci.firebird.data.FormStatus@ACCEPTED.display"/>';

var progressBar = {};

progressBar.setRegistrationStatusAndReturnCompletionPercentage = function () {
    if (!${registration.submittable}) {
        return 100;
    }

    var requiredForms = 0;
    var completedRequiredForms = 0;

    <s:iterator value="registration.forms" var="form">
        if ("${form.formOptionality.display}" == requiredText) {
            requiredForms++;
            if ("${form.formStatus.display}" == completedText ||
                "${form.formStatus.display}" == acceptedText) {
                completedRequiredForms++;
            }
        }
    </s:iterator>

    var completionStatusSuffix = '<fmt:message key="label.required.forms.progress.bar.completion.status.suffix"/>';
    var registrationStatus = "${registration.status.display}";
    var completionStatus = registrationStatus + " (" + completedRequiredForms + "/" + requiredForms + " " + completionStatusSuffix;
    $("#registrationStatus").text(completionStatus);

    if (requiredForms === 0) {
        return 100;
    } else {
        return completedRequiredForms / requiredForms * 100;
    }
}

progressBar.createProgressBar = function (completionPercentage) {
    $("#registrationProgressBar").progressbar({
        value: completionPercentage
    });
}

$(document).ready(function () {
    var completionPercentage = progressBar.setRegistrationStatusAndReturnCompletionPercentage();
    progressBar.createProgressBar(completionPercentage);
});

</script>