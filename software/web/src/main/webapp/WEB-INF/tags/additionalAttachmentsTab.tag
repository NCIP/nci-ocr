<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/struts-jquery-tags" prefix="sj" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="firebird" %>

<s:url var="downloadLink" action="download" namespace="/investigator/profile/files/ajax" >
    <s:param name="profile.id" value="%{profile.id}"/>
</s:url>
<s:url var="selectFileUrl" action="selectFile" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}"/>
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>
<s:url var="deselectFileUrl" action="deselectFile" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}"/>
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>
<s:url var="uploadDialogEnter" action="enterUploadDialog" escapeAmp="false">
    <s:param name="profile.id" value="%{profile.id}"/>
    <s:param name="registration.id" value="%{registration.id}"/>
</s:url>

<h2 class="clear">
    <fmt:message key="registration.attachments.title"/>
</h2>
<firebird:messages/>

<firebird:instructionBubble messageKey="registration.attachments.text" id="additionalAttachmentsInstructions"/>

<s:if test="%{!readOnly}">
    <sj:a openDialog="registrationDialog" href="%{uploadDialogEnter}" cssClass="button" id="addFileButton"><fmt:message key="button.add"/></sj:a>
</s:if>
<table id="filesTable" class="ui-jqgrid-htable ui-jqgrid-btable"
        summary="This table shows the additional documents uploaded to the profile. It displays a checkbox for selection
                to add it to the registration, the file name, and the file description.">
    <thead>
        <tr>
            <th scope="col" width="30px"><%--Checkbox column--%></th>
            <th scope="col" width="225px"><div><fmt:message key="label.file.name" /></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.description" /></div></th>
            <th scope="col" width="225px"><div><fmt:message key="label.upload.date" /></div></th>
        </tr>
    </thead>
</table>

<script type="text/javascript">
    var _attachmentsPage = (function(){
        var page = {
            isLocked : <s:property value="readOnly"/>,

            createCheckColumn : function(file) {
                var checkboxHtml = "";
                if (!this.isLocked) {
                    checkboxHtml = "<input type='checkbox' "
                    var checkboxId = "file_"+ file.id;
                    checkboxHtml += "id='"+ checkboxId + "' ";
                    checkboxHtml += "value='" + file.id + "' ";
                    if (file.selected) {
                        checkboxHtml += "checked ";
                    }
                    checkboxHtml += "onclick='_attachmentsPage.submitAndSwitch(this, " + file.id + ")'";
                    checkboxHtml += "/>";
                }
                return createHiddenCheckboxLabel(checkboxId) + checkboxHtml;
            },

            switchToLoading : function(item) {
                _fbUtil.switchObjects(item, _fbUtil.loadingIcon);
            },

            switchFromLoading : function(item) {
                _fbUtil.switchObjects(_fbUtil.loadingIcon, item);
            },

            submitAndSwitch : function(checkbox, fileId) {
                var url = _attachmentsPage.getSubmitUrl(checkbox);
                var formData = { 'selectedFile.id' : fileId };
                _attachmentsPage.switchToLoading(checkbox);
                $.post(url, formData).success(function(data){
                     _attachmentsPage.switchFromLoading(checkbox);
                }).error(function(){
                     setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>');
                     $(checkbox).prop("checked", false);
                });
            },

            getSubmitUrl : function(checkbox) {
                  if ($(checkbox).attr("checked")) {
                      return '${selectFileUrl}';
                  } else {
                      return '${deselectFileUrl}';
                  }
              }

        }
        return page;
    })();

    $(document).ready(function() {
        var dataRows = ${filesAsJson};
        var isLongData = dataRows.length > ${minPaginationResults};
        $('#filesTable').dataTable( {
            aaData : dataRows,
            bInfo : isLongData,
            bLengthChange : isLongData,
            bPaginate : isLongData,
            bFilter : isLongData,
            aaSorting : [[1, 'asc'], [3, 'desc']],
            aoColumns : [{mDataProp : null, fnRender : function(obj) {
                            return _attachmentsPage.createCheckColumn(obj.aData);
                        }},
                        {mDataProp: "name", fnRender : function(obj) {
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
                          }}
                      ],
           fnRowCallback: function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
               $(nRow).attr("id", aData.id);
               return nRow;
           },
           fnInitComplete : function() {
               indicateLoading(false);
           }
        });
    });
</script>

