<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<%@ attribute name="saveAction" required="true"%>
<%@ attribute name="organizationSearchLabel" required="true"%>

<div id="tabwrapper">
    <div class="ui-tabs">
        <div class="ui-tabs-panel">
            <s:form action="%{#attr.saveAction}" id="credentialForm">

                <jsp:doBody/>

                <s:hidden id="issuerSearchKey" name="issuerSearchKey" />
                <s:hidden name="page" value="%{@gov.nih.nci.firebird.web.common.FirebirdUIConstants@RETURN_FIELDS_PAGE}"/>
                <s:hidden name="profile.id" value="%{profile.id}"/>
            </s:form>
            <div class="blank_space"></div>
            <firebird:organizationSearch labelKey="${organizationSearchLabel}" required="true"/>
            <script type="text/javascript">
                $(document).ready(function() {
                    organizationSearch.clickSelectButton = function(searchKey) {
                        disableDialog();
                        $('#issuerSearchKey').val(searchKey);

                        var href = $('#credentialForm').attr('action');
                        var serializedForm = $('#credentialForm').serialize();
                        $.post(href, serializedForm).success(function(data) {
                            enableDialog();
                            $('#profileDialog').html(data);
                        }).error(function (){
                            enableDialog();
                            setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>')
                        });
                    };
                });
            </script>
            <div class="btn_bar clear">
                <s:url var="createNewCredentialUrl" action="%{#attr.saveAction}"/>
                <sj:a id="createNewOrganization" formIds="credentialForm" targets="profileDialog" href="%{#createNewCredentialUrl}"
                      cssClass="button" onSuccessTopics="enable" onClickTopics="submit" onErrorTopics="ajaxError"><fmt:message key="button.create.new"/></sj:a>
                <s:a id="manageCredentialCancelBtn" href="#" cssClass="button" onclick="closeDialog();"><fmt:message key="button.cancel"/></s:a>
            </div>
        </div>
    </div>
</div>
