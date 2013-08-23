<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<%@ attribute name="titleKey"%>
<%@ attribute name="retainState" type="java.lang.Boolean"%>

<s:set var="titleKey">
    ${empty titleKey ? 'default.comments.header' : titleKey }
</s:set>

<div id="commentsDiv" class="commentsdiv">
    <div class="commentHeader opendiv">
        <h4>
            <s:property value="%{getText(#titleKey)}"/>
        </h4>
    </div>
    <div class="content">
        <div class="richText">
            <jsp:doBody/>
        </div>
    </div>
</div>
<div class="clear"></div>
<script type="text/javascript">

    var retainState = "${retainState}" === "true";
    var cookieName;

    $(document).ready(function() {
        $(".commentHeader").click(function() {
          toggleShowComments(this, 500);
        });

        if (retainState) {
            commentLength = $(".commentsdiv:visible").text().length;
            cookieName = 'commentsCollapsed-' + commentLength;
            var collapsed = $.cookie(cookieName);

            if (collapsed === "true") {
              toggleShowComments($(".commentHeader:visible"), 0);
            }
        }
    });

    function toggleShowComments(comments, speed) {
        $(comments).next(".content").slideToggle(speed);
        if($(comments).hasClass('opendiv')) {
            setCollapsedCookieIfNecessary('true');
            $(comments).removeClass('opendiv').addClass('closeddiv');
        } else {
            setCollapsedCookieIfNecessary('false');
            $(comments).removeClass('closeddiv').addClass('opendiv');
        }
    }

    function setCollapsedCookieIfNecessary(value) {
      if (retainState) {
            $.cookie(cookieName, value);
      }
    }
</script>
