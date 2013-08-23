
            </div>
        </div>
        <!--/NCI OCR-->
         <!--/Content-->
        <script type="text/javascript">
            $(document).ready(function() {
                $.subscribe('enable', function(event, data) {
                    enableDialog();
                });
                $.subscribe('ajaxError', function(event, data) {
                    if(data.statusText === "error") {
                        enableDialog();
                        setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>')
                    }
                });

                $(document).ajaxError(function(event, response) {
                    if (isAborted(response)) {
                      return;
                    }
                    else if (isErrorPage(response)) {
                        setPageErrorMessages($(response.responseText).find(".instructions").text());
                    } else {
                        setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>')
                    }
                  });

                $.subscribe('submit', function(event, data) {
                    disableDialog();
                });
                $.subscribe('dialogClosed', function(event, data) {
                    clearDialog();
                });
                $.subscribe('dialogOpened', function(event, data) {
                    $(data).css({'max-height': $(window).height()-50, 'overflow-y': 'auto'});
                });

                addFocusOutToDialog();
            });

            function isAborted(response) {
                return response.statusText === "abort";
            }

            function isErrorPage(response) {
                return $(response.responseText).find("#errorPage .instructions").length > 0;
            }

            //Without this, any dialogs that reload their content cannot be navigated back into using just the keyboard
            //or if the user clicks out of the dialog they can't navigate back into the dialog using just the keyboard
            function addFocusOutToDialog() {
              $(".ui-dialog-content[id!='timeoutDialog']").focusout(function(event) {
                window.setTimeout("checkForDialogFocus();", 100);
              });
            }

            function checkForDialogFocus() {
              if (isDialogVisible() && isFocusOutsideDialog()) {
                 setFocusToDialog();
              }
            }

            function isDialogVisible() {
              return $(".ui-dialog-content[id!='timeoutDialog']:visible").size() > 0;
            }

            function isFocusOutsideDialog() {
              var focus = document.activeElement;
              if (focus == null) {
                focus =  $(":focus");
              }
              return focus == null ||
              (!$(".ui-dialog-content[id!='timeoutDialog']:visible").is(focus) &&
              $(".ui-dialog-content[id!='timeoutDialog']:visible").find(focus).size() === 0);
            }

        </script>
        <%@ include file="/WEB-INF/content/common/footer.jsp" %>
    </body>
</html>
