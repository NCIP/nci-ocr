<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<html>
    <body id="sec">
        <!--Content-->
        <div id="tabwrapper">
            <div class="ui-tabs">
                <div class="ui-tabs-panel">
                    <firebird:messages/>
                    <s:form namespace="/investigator/profile/credentials/ajax" action="saveCertification" id="credentialsCertificationForm">

                        <firebird:dialogHeader>
                            <fmt:message key="credentials.certification.title" />
                        </firebird:dialogHeader>

                        <div class="formcol_wide">

                            <s:select id="credentialsCertificationType"
                                label="%{getText('dropdown.certificationType')}"
                                requiredLabel="true"
                                name="certificationTypeId"
                                list="certificationsWithRankedOnTop"
                                listKey="id"
                                listValue="name"
                                headerKey=""
                                headerValue="--- Select a Certification Type ---"
                                onchange="doesExpire()"/>
                        </div>
                        <div class="clear" ></div>

                        <div class="formcol">
                             <firebird:dateSelect fieldName="effectiveDate" label="datePicker.effectiveDate" required="true"/>
                        </div>

                        <div id="expirationColumn" class="formcol hide">
                             <firebird:dateSelect fieldName="expirationDate" label="textfield.expirationDate" required="true" isFuture="true"/>
                        </div>

                        <s:hidden name="id"/>
                        <s:hidden name="profile.id" value="%{profile.id}"/>

                        <firebird:profileButtonBar idPrefix="manageCertifications" form="credentialsCertificationForm"/>


                        <!--/Content-->
                        <script type="text/javascript">
                        var typeExpires = [];

                        <s:iterator value="certificationsWithRankedOnTop" var="type">
                            typeExpires['${type.id}'] = ${type.expires};
                        </s:iterator>

                        function doesExpire() {
                            if(typeExpires[$('#credentialsCertificationType').val()]) {
                                $('#expirationColumn').show();
                            } else {
                                $('#expirationColumn').hide();
                            }
                        }

                        $(document).ready(doesExpire);
                        </script>

                    </s:form>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function() {
                var index = <s:property value="rankedCertifications.size"/>;
                addSeperatorToSelectList('credentialsCertificationType', index);
            });
        </script>
    </body>
</html>