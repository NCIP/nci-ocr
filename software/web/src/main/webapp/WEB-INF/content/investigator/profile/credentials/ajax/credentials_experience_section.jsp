<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url namespace="/investigator/profile/credentials/ajax" action="editExperience" var="editUrl" >
  <s:param name="profile.id" value="%{profile.id}" />
</s:url>
<sj:a openDialog="profileDialog" href="%{editUrl}" cssClass="addCredential" id="editExperienceButton"><fmt:message key="button.edit.experience" /></sj:a>
<br/>

<div id="experience" class="richText onecol">
    <s:property value="%{cleanText}" escape="false"/>
</div>