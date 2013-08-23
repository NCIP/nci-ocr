<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script type="text/javascript" src="<c:url value='/scripts/jquery.tokeninput.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/styles/token-input.css'/>" type="text/css" />
<firebird:signAndSubmitRegistrationDialog>

    <div class="line"></div>

    <firebird:label forId="commentsEditor" messageKey="label.comments"/>
    <s:set name="maxCommentCharacters" value="@gov.nih.nci.firebird.web.action.investigator.registration.common.CompletionActionProcessor@MAX_CHAR_COUNT" />
    <firebird:richTextArea id="commentsEditor" name="comments" maxCharacters="${maxCommentCharacters}" />

    <div class="clear"></div>

    <br/>
    <firebird:instructionBubble messageKey="forms.submission.additional.email.recepients" />
    <br/>
    <span class="warningMessage" id="invalidEmailAddressMessage"></span>
    <s:textfield id="additionalEmailAddresses" name="notificationEmailAddresses" />
    <br/>

</firebird:signAndSubmitRegistrationDialog>

<script type="text/javascript">

var invalidEmailTextSuffix = '<s:property value="%{getText('forms.submission.invalid.email.address.suffix')}"/>';

$(document).ready(function () {
  createTokenInput();
  populateTokenInput();
});

function createTokenInput() {
  $("#additionalEmailAddresses").tokenInput(null, {
        preventDuplicates: true,
        allowFreeTagging: true,
        hintText: null,
        searchingText: null,
        onAdd: function (item) {
          var email = item.name;
          if (!isValidEmail(email)) {
            $("#additionalEmailAddresses").tokenInput("remove", item);
            showInvalidEmailAddressMessage(email);
          } else {
            hideInvalidEmailAddressMessages();
          }
        }
      });
}

function showInvalidEmailAddressMessage(email) {
  var invalidEmailText = email + " " + invalidEmailTextSuffix;
  $("#invalidEmailAddressMessage").text(invalidEmailText);
}
function hideInvalidEmailAddressMessages(email) {
  $("#invalidEmailAddressMessage").text("");
}

function populateTokenInput() {
  $("#additionalEmailAddresses").tokenInput("clear");
  var emailAddresses = "${notificationEmailAddresses}".split(',');
  _.each(emailAddresses, function(emailAddress) {
    if (emailAddresses != "") {
        $("#additionalEmailAddresses").tokenInput("add", {id: emailAddress, name: emailAddress});
    }
  })
}
</script>