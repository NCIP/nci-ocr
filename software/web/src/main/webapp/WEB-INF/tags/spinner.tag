<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript" src="<s:url value="/scripts/spin.min.js"/>"></script>

<%@ attribute name="messageKey" required="true"%>

<div id="spinner">
    <h1 class="centeredText"><fmt:message key="${messageKey}" /></h1>
</div>

<script>

$(function() {
  initializeSpinner();
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
    var target = document.getElementById('spinner');
    var spinner = new Spinner(opts).spin(target);
}

</script>