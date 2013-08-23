<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon" />
<link rel="shortcut icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon" />

<s:url value="/jqueryui" var="jqueryui" />
<sj:head jquerytheme="custom-theme" customBasepath="%{jqueryui}" loadAtOnce="true" />
<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.dataTables.min.js"/>"></script>
<script type="text/javascript" language="javascript" src="<s:url value="/scripts/jquery.maskedinput-1.3.1.min.js"/>"></script>
<script type="text/javascript" language="javascript" src="<s:url value="/scripts/underscore-min.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery.cookie.js'/>"></script>
<link href="<c:url value='/styles/demo_table.css'/>" rel="stylesheet" type="text/css" />

<!--Dropdown Menus-->
<script src="<s:url value="/scripts/superfish.js"/>"></script>
<link href="<c:url value='/styles/superfish.css'/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/dropdown_menu.js'/>"></script>
<script type="text/javascript">
    anylinkmenu.init("menuanchorclass")
</script>
<!--Dropdown Menus-->


<!--Loading Indicator-->
<script type="text/javascript" language="javascript" src="<c:url value='/scripts/jquery.loading.1.6.4.min.js'/>"></script>
<!--Loading Indicator-->

<!--NCI OCR CSS-->
<link href="<c:url value='/styles/firebird.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/styles/overwrites.css'/>" rel="stylesheet" type="text/css" media="all" />
<!--[if IE]>
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/ie.css'/>" />
<![endif]-->
<!--/NCI OCR CSS-->

<script type="text/javascript" language="javascript">
    var contextPath = "<%=request.getContextPath()%>";
</script>
<script type="text/javascript" language="javascript" src="<c:url value='/scripts/firebird.js'/>"></script>