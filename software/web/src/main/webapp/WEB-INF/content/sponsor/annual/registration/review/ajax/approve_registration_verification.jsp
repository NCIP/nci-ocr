<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<script type="text/javascript" src="<s:url value="/scripts/spin.min.js"/>"></script>
<firebird:dialogLayout>
    <div id="validating">
        <h1 class="centeredText"><fmt:message key="label.validating"/></h1>
    </div>
    <div id="approved" class="hide">
        <h1 class="centeredText"><img src="<c:url value='/images/veri_check.png'/>" alt="Success" /><fmt:message key="label.approved"/></h1>
        <p class="centeredText"><fmt:message key="sponsor.annual.registration.approved"/></p>
        <div class="center" style="width:130px;">
            <a id="closeBtn" href="javascript:void(0)" class="button blueButton bigButton" onclick="closeDialog();"><fmt:message key="button.close"/></a>
        </div>

    </div>
</firebird:dialogLayout>

<s:url var="approveUrl" namespace="/sponsor/annual/registration/review/ajax" action="approveRegistration">
    <s:param name="registration.id">${registration.id}</s:param>
</s:url>
<script>
    $(document).ready(function () {
        $(".ui-dialog-titlebar").hide();
        var target = getCurrentDialog();
        target.dialog("option", "width", 400);
        $(".ui-tabs-panel", target).height(200);
        target.dialog("option", "position", "center");

    	initializeSpinner();
        var url = '${approveUrl}';
        _fbUtil.performAjaxPost(url, null, function() {
            setTimeout(function() {
            	$("#validating").hide();
                $("#approved").show();
            }, 500);
        });

        $("input").keydown(testForEnter);
    });

    function initializeSpinner() {
    	var opts = {
			lines: 13, // The number of lines to draw
			length: 22, // The length of each line
			width: 8, // The line thickness
			radius: 22, // The radius of the inner circle
			corners: 1, // Corner roundness (0..1)
			rotate: 50, // The rotation offset
			color: '#000', // #rgb or #rrggbb
			speed: 1, // Rounds per second
			trail: 80, // Afterglow percentage
			shadow: false, // Whether to render a shadow
			hwaccel: false, // Whether to use hardware acceleration
			className: 'spinner', // The CSS class to assign to the spinner
			zIndex: 2e9, // The z-index (defaults to 2000000000)
			top: 70, // Top position relative to parent in px
			left: 110 // Left position relative to parent in px
		};
		var target = document.getElementById('validating');
		var spinner = new Spinner(opts).spin(target);
    }
</script>
