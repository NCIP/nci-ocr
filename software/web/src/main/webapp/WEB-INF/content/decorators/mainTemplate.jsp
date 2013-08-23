<%@ include file="/WEB-INF/content/decorators/sharedTemplateStart.jsp" %>                
<%@ include file="/WEB-INF/content/common/header.jsp" %>

<!--Main--> 
<div id="main"> 
    
    <!--Breadcrumb Trail-->
    <div id="breadcrumb"> 
        <a href="<c:url value='/'/>" id="home_icon"><span>Home</span></a>
    </div> 
    <!--/Breadcrumb Trail--> 
    <!-- Roles Updated Warning -->
    <s:if test="%{#session[@gov.nih.nci.firebird.web.common.FirebirdUIConstants@ROLES_UPDATED] != null}">
        <div id="warning" class="warningMessage"><fmt:message key='user.add.roles.logout.message'/></div>
    </s:if>
    <!-- /Roles Updated Warning -->
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
