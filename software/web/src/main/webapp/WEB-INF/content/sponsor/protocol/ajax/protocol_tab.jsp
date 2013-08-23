<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h2>
    <fmt:message key="sponsor.protocol.tab.title" />

    <s:if test="%{sponsorRepresentative}">
        <s:a id="editProtocol" namespace="/sponsor/representative/protocol" action="editProtocol" cssClass="edit">
            <s:param name="protocol.id" value="%{protocol.id}"/>
            <fmt:message key="button.edit" />
        </s:a>
    </s:if>
</h2>

<firebird:protocolInformation protocol="${protocol}" />
<firebird:protocolHistory protocol="${protocol}" isSponsor="true"/>