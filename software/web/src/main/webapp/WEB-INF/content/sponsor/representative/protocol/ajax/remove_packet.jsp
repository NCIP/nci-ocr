<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<s:url var="removeRegistrationPacketUrl" namespace="/sponsor/representative/protocol/ajax" action="removeRegistrationPacket">
    <s:param name="registration.id" value="registration.id" />
</s:url>

<firebird:confirmDialog titleKey="sponsor.protocol.remove.investigator.confirm.title" confirmUrl="${removeRegistrationPacketUrl}" dialogId="popUpDialog">
    <h3>
        <fmt:message key="sponsor.protocol.remove.investigator.confirm.message">
            <fmt:param>${registration.profile.person.displayName}</fmt:param>
            <fmt:param>${registration.protocol.protocolTitle}</fmt:param>
        </fmt:message>
    </h3>
</firebird:confirmDialog>