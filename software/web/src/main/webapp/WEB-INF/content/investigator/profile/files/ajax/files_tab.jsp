<%@ include file="/WEB-INF/content/common/taglibs.jsp"%>
<h2>
    <fmt:message key="investigator.profile.files"/>
</h2>
<firebird:messages/>

<firebird:instructionBubble messageKey="investigator.profile.files.instructions"/>

<s:form action="upload" id="uploadForm" namespace="/investigator/profile/files/ajax" enctype="multipart/form-data" method="POST" >
    <div class="formcol_wide">
        <s:file name="dataFile.data" id="data" requiredPosition="right" size="30" requiredLabel="true"
                label="%{getText('label.file')}" labelposition="top" labelSeparator="" onKeyDown="this.blur()" onContextMenu="return false;"/>
    </div>
    <div class="formcol">
        <s:textfield name="dataFile.description" id="description" label="%{getText('label.description')}" size="40" maxlength="%{@gov.nih.nci.firebird.data.FirebirdFile@DESCRIPTION_LENGTH}"/>
    </div>
    <div class="formcol">
        <sj:submit id="upload" cssClass="btn_upload" targets="Files" value="Upload" onclick="indicateLoading();"/>
    </div>
    <s:hidden name="profile.id" value="%{profile.id}"/>
</s:form>

<div class="clear"><br></div>

<s:url var="downloadLink" action="download" namespace="/investigator/profile/files/ajax" >
    <s:param name="profile.id" value="%{profile.id}"/>
</s:url>
<s:url var="deleteLink" action="delete" namespace="/investigator/profile/files/ajax" >
    <s:param name="profile.id" value="%{profile.id}"/>
</s:url>

<table id="gridtable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table displays files which have been uploaded to the user's profile. It displays the file name,
                which can be clicked to download the file, the entered description, the date uploaded, and a link to delete
                the file from the profile if necessary.">
    <thead>
        <tr>
            <th scope="col" width="225px"><div><fmt:message key="label.file"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.description"/></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.upload.date"/></div></th>
            <th scope="col" width="30px"><%--Delete column--%></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        var dataRows = ${filesJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#gridtable').dataTable( {
            aaData : dataRows,
            bInfo : isLongData,
            bLengthChange : isLongData,
            bPaginate : isLongData,
            bFilter : isLongData,
            aoColumns : [{mDataProp: "name", fnRender : function(obj) {
                            var content = obj.aData.name;
                            content = linkFormatter(content,{
                                paramName :'file.id',
                                paramValue : 'id',
                                action : 'download',
                                url : '<s:property value="downloadLink"/>'
                            }, obj.aData);

                            return content;
                          }},
                          {mDataProp: "description"},
                          {mDataProp: "uploadDate", stype : "date", fnRender : function(obj) {
                              return formatDate(obj.aData.uploadDate, true);
                          }},
                          {mDataProp : null, fnRender : function(obj) {
                              var cellvalue = obj.aData.id;
                              var options = { id :  obj.aData.id,
                                      action : 'delete',
                                      url:'<s:property value="deleteLink"/>',
                                      target: 'Files',
                                      imageUrl : '<s:url value="/images/ico_delete.gif" />',
                                      imageTitle : 'Delete',
                                      paramName:'id',
                                      paramValue: 'id',
                                      showLoading: true};

                              return ajaxImageFormatter(cellvalue, options, obj.aData);
                              }}
                      ],
           fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           },
           fnInitComplete : function() {
               indicateLoading(false);
                $("input").keydown(testForEnter);
           }
        });
    });
</script>
