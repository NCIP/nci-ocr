<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sjr" uri="/struts-jquery-richtext-tags"%>

<%@ attribute name="id" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" rtexprvalue="false"%>
<%@ attribute name="value" %>
<%@ attribute name="required" %>
<%@ attribute name="maxCharacters" required="true" %>

<s:url value="/scripts/ckeditor.config.js" var="configUrl"/>
<s:set var="maxCharacters">${empty maxCharacters ? "500" : maxCharacters }</s:set>

<s:if test="%{#attr.label == null}">
    <sjr:ckeditor
        id="%{#attr.id}"
        name="%{#attr.name}"
        rows="10"
        cols="80"
        width="800"
        requiredLabel="%{#attr.required}"
        toolbar="FirebirdDefault"
        customConfig="%{configUrl}"
    />
</s:if>
<s:else>
    <sjr:ckeditor
        id="%{#attr.id}"
        name="%{#attr.name}"
        label="%{#attr.label}"
        rows="10"
        cols="80"
        width="800"
        requiredLabel="%{#attr.required}"
        toolbar="FirebirdDefault"
        customConfig="%{configUrl}"
    />
</s:else>


<span id="charactersRemaning"></span>&nbsp;<fmt:message key="label.characters.remaining" />
<div class="commentsText" style="display: none"></div>

<script>

  var richTextArea = {
      commentsEditor : $("#${id}"),
      commentsText : $(".commentsText"),
      charactersRemaining : $("#charactersRemaning"),
      timeout : null,

      updateCount : function() {
        var commentsHtml = richTextArea.commentsEditor.val();
        richTextArea.commentsText.html(commentsHtml);
        var cleanText = trimLeft(richTextArea.commentsText.text());
        cleanText = cleanText.replace(/\t/g, " ").replace(/\n/g, "");
        var charactersRemaining = ${maxCharacters} - cleanText.length;
        window.clearTimeout(richTextArea.timeout);
        richTextArea.timeout = window.setTimeout("richTextArea.charactersRemaining.text(" + charactersRemaining + ");", 200);
      }

  };

  $(function() {
    var editor = CKEDITOR.instances['${id}'];

    var initialValue = "${value}";
    if (initialValue != "") {
      editor.setData(initialValue);
    }

    editor.on('contentDom', function() {
      editor.document.on('keyup', function(event) {
        richTextArea.updateCount();
      })
    });
    richTextArea.updateCount();
  });

</script>