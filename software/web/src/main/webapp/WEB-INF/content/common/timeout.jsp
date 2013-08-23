<%@ include file="/WEB-INF/content/common/taglibs.jsp" %>

<c:if test="${not empty pageContext.request.remoteUser}">
    <sj:dialog id="timeoutDialog" modal="true" autoOpen="false" width="600" position="center" resizable="false" cssClass="hide">
        <firebird:dialogLayout>
            <div>
                <h3 class="centeredText"><fmt:message key="timeout.message"/></h3>
                <div class="center" style="width:130px;">
                    <a id="resetTimeoutButton" href="javascript:void(0)" class="button blueButton bigButton">
                        <fmt:message key="button.continue"/>
                    </a>
                </div>
            </div>
        </firebird:dialogLayout>
    </sj:dialog>

    <script type="text/javascript">
        var sessionTimer = null;
        var lastRefresh = 0;
        function logoutUser() {
            window.location = '<s:url namespace="/login" action="logout" />';
        }

        function resetSessionTimer() {
            var curTime = (new Date()).getTime();
            // prevents multiple resets within a defined interval
            if ((curTime - lastRefresh) > SESSION_TIMEOUT_RESET_INTERVAL) {
                // keep server session alive
                if (sessionTimer) {
                    clearTimeout(sessionTimer);
                }
                sessionTimer = setTimeout(logoutUser, SESSION_TIMEOUT);
                lastRefresh = curTime;
            }
        }

        setInterval(function () {
            var timeSinceUpdate = (new Date()).getTime() - lastRefresh;
            if (timeSinceUpdate > (SESSION_TIMEOUT - SESSION_TIMEOUT_WARNING)) {
                $("#timeoutDialog").dialog("open");
                $("#resetTimeoutButton").focus();
            }
        }, 1000);

        $("#resetTimeoutButton").click(function () {
            $("#timeoutDialog").dialog("close");
            resetSessionTimer();
        });

        $(document).ready(resetSessionTimer);
        $(document).keypress(resetSessionTimer);
        $(document).mouseup(resetSessionTimer);
    </script>
</c:if>
