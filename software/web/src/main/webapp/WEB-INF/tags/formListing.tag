<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ attribute name="form" rtexprvalue="true" required="true"
type="gov.nih.nci.firebird.data.AbstractRegistrationForm"%>

<s:url var="notStartedImageUrl" value='/images/form_icon_ns.png' />
<s:url var="inProgressImageUrl" value='/images/form_icon_progress.png' />
<s:url var="completedImageUrl" value='/images/form_icon_complete.png' />
<s:url var="incompleteImageUrl" value='/images/form_icon_incomplete.png' />
<s:url var="attachmentImageUrl" value='/images/form_icon_attach.png' />
<s:url var="commentImageUrl" value='/images/ico_comments_present.gif' />

<s:div cssClass="form" id="%{#form.id}" href='#' onclick="selectTab('form_%{#form.formType.name.toLowerCase().replaceAll(' ','_')}_tab')">
    <s:if test="#form.registration.lockedForInvestigator">
        <div class="formIcon"> <img alt="Completed" src="${completedImageUrl}"/> </div>
    </s:if>
    <s:elseif test="#form.formStatus == @gov.nih.nci.firebird.data.FormStatus@NOT_STARTED">
        <div class="formIcon"> <img alt="Not Started" src="${notStartedImageUrl}"/> </div>
    </s:elseif> <s:elseif test="#form.formStatus == @gov.nih.nci.firebird.data.FormStatus@IN_PROGRESS">
        <div class="formIcon"> <img alt="In Progress" src="${inProgressImageUrl}"/> </div>
    </s:elseif> <s:elseif test="#form.formStatus == @gov.nih.nci.firebird.data.FormStatus@COMPLETED">
        <div class="formIcon"> <img alt="Completed" src="${completedImageUrl}"/> </div>
    </s:elseif> <s:elseif test="#form.formStatus == @gov.nih.nci.firebird.data.FormStatus@INCOMPLETE">
        <div class="formIcon"> <img alt="Incomplete" src="${incompleteImageUrl}"/> </div>
    </s:elseif> <s:else>
        <div class="formIcon"> <img alt="In Progress" src="${inProgressImageUrl}"/> </div>
    </s:else>

    <div class="float formDescription">
        <span class="bold"><s:property value='%{#form.formType.description}' /></span>
        <br />
        <span id="formStatus"><s:property value='%{#form.formStatus.display}' /></span>
        <s:if test="#form.commentsLinkToInvestigatorToBeDisplayed">
            <s:url action="enterComments" var="commentsUrl" escapeAmp="false">
                <s:param name="registration.id">${registration.id}</s:param>
                <s:param name="formType.id">%{#form.formType.id}</s:param>
            </s:url>
            <sj:a id="viewFormComments_%{#form.id}" openDialog="registrationDialog" href="%{commentsUrl}"><img alt="View / Edit Comments" src="${commentImageUrl}" class="vertical-align-middle" /></sj:a>
        </s:if>
    </div>
    <s:if test="#form.numberOfAdditionalDocuments > 0">
        <div class="float_right boldHeader additionalAttachmentsCount">
            <img alt="Additional Documents Count" src="${attachmentImageUrl}"/>
            <span id="additionalDocumentsCount"><s:property value='%{#form.numberOfAdditionalDocuments}' /></span>
        </div>
    </s:if>
    <br/><br/>
    <span class="date">
        <fmt:message key="label.last.updated"/>: <s:date name="formStatusDate" format="MM/dd/yyyy" />
    </span>
</s:div>