<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>
<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <firebird:messages/>

                <firebird:dialogHeader>
                    <fmt:message key="registration.attachments.upload.file.title"/>
                </firebird:dialogHeader>

                <s:form id="uploadForm" action="upload" enctype="multipart/form-data" method="POST">
                    <div class="formcol_wide">
                        <s:file name="dataFile.data" id="data" requiredPosition="right" size="30" requiredLabel="true"
                                label="%{getText('label.file')}" labelposition="top" labelSeparator="" onKeyDown="this.blur()" onContextMenu="return false;"/>
                    </div>
                    <div class="formcol">
                        <s:textfield name="dataFile.description" id="description" label="Description" size="40" maxlength="%{@gov.nih.nci.firebird.data.FirebirdFile@DESCRIPTION_LENGTH}"/>
                    </div>
                    <s:hidden name="profile.id" value="%{profile.id}"/>
                    <s:hidden name="registration.id" value="%{registration.id}"/>
                </s:form>

                <firebird:profileButtonBar idPrefix="uploadFile" form="uploadForm" dialogId="registrationDialog"/>
        </div>
    </div>
</div>

<script>
$(document).ready(function() {
    $("input").keydown(testForEnter);
    $.publish('injectCsrfTokens');
});
</script>
