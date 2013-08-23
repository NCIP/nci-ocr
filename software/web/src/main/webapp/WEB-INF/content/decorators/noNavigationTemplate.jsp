<%@ include file="/WEB-INF/content/decorators/sharedTemplateStart.jsp" %>   
<%@ include file="/WEB-INF/content/common/headerNoNav.jsp" %>

<!--Main-->
<div id="main">

    <!--Breadcrumb Trail-->
    <div id="breadcrumb">
        <span class="buffer">&nbsp;</span>
    </div>
    <!--/Breadcrumb Trail-->

    <!-- Help Link -->
    <firebird:helpButton/>
    <!-- /Help Link -->
    <!--Content-->
    <div id="content_wrapper_outer">
        <div id="content_wrapper">
            <div id="content">
                <decorator:body/>
            </div>
            <firebird:versionStamp/>
        </div>
    </div>
    <!--/Content-->
</div>
<!--/Main-->
<%@ include file="/WEB-INF/content/decorators/sharedTemplateEnd.jsp" %>   