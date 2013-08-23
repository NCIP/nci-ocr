<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <body id="sec">
        <!--Content-->
        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <h1>
                        <fmt:message key="registration.review.finalize.dialog.header"/>
                    </h1>
                    <firebird:messages/>

                    <div class="btn_bar">
                        <s:a href="#" id="returnBtn" cssClass="button" onclick="closeDialog();"><fmt:message key="button.return.to.overview"/></s:a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>