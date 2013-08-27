var US_COUNTRY_CODE = "USA";
var CAN_COUNTRY_CODE = "CAN";
var SESSION_TIMEOUT = 1800000;
var SESSION_TIMEOUT_RESET_INTERVAL = 10000;
var SESSION_TIMEOUT_WARNING = 30000;

function log() {
    try {
        console.log.apply(console, arguments);
    } catch(e) {
        try {
            opera.postError.apply(opera, arguments);
        } catch(e2) {
            alert(Array.prototype.join.call(arguments, " "));
        }
    }
}

function setFocusToFirstControl() {
    for (var f=0; f < document.forms.length; f++) {
        for (var i=0; i < document.forms[f].length; i++) {
            var elt = document.forms[f][i];
            if (elt.type !== "hidden" && !elt.disabled && elt.id !== 'enableEnterSubmit') {
                try {
                    elt.focus();
                    return;
                } catch(er) {
                }
            }
        }
    }
}

function indicateLoading(show) {
    if (show === undefined) {
        show = true;
    }
    var loadingImgSrc = contextPath + "/images/loading.gif";
    $.loading(show, {
        align : 'center',
        img : loadingImgSrc,
        mask : true,
        delay : 50
    });
}

/*
 * Takes in an associative array of options that can can contain
 * any or all of the following properties:
 * dialogId - the id of the dialog to be closed, defaults to whatever dialog is found to be open.
 * reload - boolean indicating whether the current tab should be reloaded after close. defaults to false.
 * reloadPage - boolean indicating whether the page should be refreshed whether or not a tab is present.
 * tabSet - the id of the tabset which contains the tab to be reloaded,
 *      useful to make sure the tab that is reloaded is correct (if within a subset of tabs). defaults to 'tabwrapper'.
 */
function closeDialog(params) {
    var options = (params ? params : {});
    var tabSet = (isBlank(options.tabSet) ? "tabwrapper" : options.tabSet);
    var dialog = getDialog(options.dialogId);

    if ($(dialog).dialog('isOpen')) {
        $(dialog).dialog('close');
        //Reload the current tab when a dialog box is closed
        if (options.reload) {
            var isTab = $('#' + tabSet).data("ui-tabs");
            if (options.reloadPage || !isTab) {
                refreshPage();
                return;
            } else {
                reloadCurrentTab(tabSet);
            }
        }
        clearDialog();
        enableDialog(options.dialogId, true);
    }
}

function closeDialogAndReload() {
    closeDialog({reload : true});
}

function clearDialog(dialogId) {
    var dialog = getDialog(dialogId);
    $(dialog).html('');
    // This prevents the caching issue with the dialog's href
    $(dialog).dialog( "option", "open", null);
}

function getDialog(dialogId) {
    var dialog;
    if(isBlank(dialogId)) {
        dialog = getCurrentDialog();
    } else {
        dialog = $('#' + dialogId).first();
        if (dialog.length === 0) {
            dialog = getCurrentDialog();
        }
    }
    return dialog;
}

function getCurrentDialog() {
    return $("#" + $(".ui-dialog-content[id!='timeoutDialog']").attr("id"));
}

function reloadCurrentTab(tabset) {
    indicateLoading(true);
    if (!(tabset) || isBlank(tabset)) {
        tabset = "tabwrapper";
    }
    var current_index = getCurrentSelectedTabIndex(tabset);
    $("#" + tabset).tabs('load',current_index);
    indicateLoading(false);
}

function getCurrentSelectedTabIndex(tabset) {
  if (!(tabset) || isBlank(tabset)) {
        tabset = "tabwrapper";
    }
    var curTab = $("#" + tabset + " .ui-state-active");
    return curTab.index();
  }

function selectTab(tabId, tabset) {
    if (isBlank(tabset)) {
        tabset = "tabwrapper";
    }

    tabId = $('#' + tabId + " a").attr('href');
    $("#" + tabset).tabs('select', tabId);
}

function disableDialog(dialogId) {
    var dialog = getDialog(dialogId);

    indicateLoading();
    try {
        $(dialog).dialog({
            disabled : true
        });
    } catch(e) {
        // if for some reason a dialog can't be found, exit cleanly.
    }
}

function enableDialog(dialogId, leaveLoadingImage) {
    var dialog = getDialog(dialogId);

    if (leaveLoadingImage === undefined) {
        leaveLoadingImage = false;
    }
    if (!leaveLoadingImage) {
        indicateLoading(false);
    }

    try {
        $(dialog).dialog({
            disabled : false
        });
        setFocusToDialog();
    } catch (e) {
        // if for some reason a dialog can't be found, exit cleanly.
    }
}

function setFocusToDialog() {
    $(".ui-dialog-content[id!='timeoutDialog']:visible").parent().focus();
}

//A Formatter for the js Grid to format an Address for display
//TODO DEPRECATED
function addressFormatter(cellvalue, options, rowObject) {
    if (options.colModel.formatoptions) {
        return __addressFormatter(cellvalue, options.colModel.formatoptions.delim);
    } else {
        return __addressFormatter(cellvalue);
    }
}

//TODO DEPRECATED
function personFormatter(cellvalue, options, rowObject) {
    if (!cellvalue) {
        cellvalue = rowObject;
    }
    var tooltip = cellvalue.displayNameForList + " | " + __addressFormatter(cellvalue.postalAddress, " | ");
    return "<span title='" + tooltip + "'>" + cellvalue.displayNameForList + "</span>";
}

//TODO rename
function __addressFormatter(address, delimitor) {
    var delim = delimitor ? delimitor : "<br>";
    var formattedValue = address.streetAddress + delim;
    if (!isBlank(address.deliveryAddress)) {
        formattedValue += address.deliveryAddress + delim;
    }
    formattedValue += address.city;
    if (!isBlank(address.stateOrProvince)) {
        formattedValue += ", " + address.stateOrProvince;
    }
    formattedValue +=  " " + address.postalCode + delim;
    formattedValue += address.country;

    return formattedValue;
}

function organizationFormatter(org, delim) {
    if(!delim) {
        delim = "<br>";
    }
    var address = __addressFormatter(org.postalAddress, delim);
    var formattedValue = org.name + delim + address;

    return formattedValue;
}

//a link formatter that ties to base paramValue on the cell value.
//TODO DEPRECATED
function linkedImageFormatter(cellvalue, options, rowObject) {
    if (!cellvalue) {
        return "";
    }
    options = simplifyOptions(cellvalue, options, rowObject);
    var imageTag = createLinkImage(cellvalue, options);
    options.text = imageTag;
    return linkFormatter(imageTag, options, rowObject);
}

// a link formatter that ties to base paramValue on the cell value.
//TODO DEPRECATED
function linkFormatter(cellvalue, options, rowObject) {
    if (!cellvalue) {
        return "";
    }
    if(!rowObject) {
        rowObject = options;
    }
    options = simplifyOptions(cellvalue, options, rowObject);

    var href = "href=\"" + options.url + "\"" ;
    var id = (!isBlank(options.action)) ? ("id=\"" + options.action + "." + rowObject.id) +"\"" : "";
    var text = (options.text) ? options.text : cellvalue;
    var onclick = (options.onclick) ? " onclick='" + options.onclick + "' " : "";
    var clazz = (!isBlank(options.htmlClass)) ? (" class='" + options.htmlClass +"'") : "";

    return  "<a " + id  + " " + href + onclick + clazz + ">" + text + "</a>";
}

//a link formatter that ties to base paramValue on the cell value.
function labCertificateViewLinkFormatter(rowObject, url) {
    options = {
        "url":url,
        "paramName":'associationId',
        "paramValue": 'id',
        "imageUrl" : contextPath + '/images/ico_edit.gif',
        "imageTitle" : 'Update',
        "action": 'view',
        "target": 'profileDialog'
    };

    var numCert = rowObject.certificateCount;
    options.text = createLinkImage("", options) + "&nbsp;(" + numCert + " added)";
    return ajaxLinkFormatter("", options, rowObject);
}

function credentialEditLink(rowObj, url, pageType, dialog) {
    if (!dialog) {
        dialog = "profileDialog";
    }
    return ajaxLinkFormatter(createImage('ico_edit.gif', 'Edit'),
    {
        'url': url,
        'paramName':'id',
        'paramValue': 'id',
        'action': 'edit',
        'target': dialog,
        'addParam':'page=' + pageType
    },
    rowObj);
}

function credentialDeleteLink(rowObj, url, pageType, dialog) {
    if (!dialog) {
        dialog = "profileDialog";
    }
    return ajaxLinkFormatter(createImage('ico_delete.gif', 'Delete'),
    {
        'url': url,
        'paramName':'id',
        'paramValue': 'id',
        'action': 'delete',
        'target': dialog,
        'addParam':'page=' + pageType
    },
    rowObj);
}

function createOrgAssociationDeleteLink(association, url, associationType, dialog) {
    if (!dialog) {
        dialog = "profileDialog";
    }
    return ajaxLinkFormatter(createImage('ico_delete.gif', 'Delete'),
        {
            'url': url,
            'paramName':'searchKey',
            'paramValue': 'organizationId',
            'action': 'delete',
            'target': dialog,
            'addParam':'associationType=' + associationType
        },
        association);
}


//a link formatter that ties to base paramValue on the cell value.
//TODO DEPRECATED
function ajaxImageFormatter(cellvalue, options, rowObject) {
    options = simplifyOptions(cellvalue, options, rowObject);

    imageTag = createLinkImage(cellvalue, options);
    return ajaxLinkFormatter(imageTag, options, rowObject);
}

// a link formatter that ties to base paramValue on the cell value.
//TODO DEPRECATED
function ajaxLinkFormatter(cellvalue, options, rowObject) {
    if(!rowObject) {
        rowObject = options;
    }
    options = simplifyOptions(cellvalue, options, rowObject);
    var text = (options.text) ? options.text : cellvalue;
    var id = (!isBlank(options.action)) ? ("id=\"" + options.action + "." + rowObject.id) +"\"" : "";
    var loading = (!isBlank(options.showLoading)) ? "indicateLoading();" : "";
    var clazz = (!isBlank(options.htmlClass)) ? (" class='" + options.htmlClass +"'") : "";

    return "<a " + id + " href=\"javascript:void(0)\" onclick=\"" + loading + "clickAjaxLink('" + options.target +"','" + options.url + "')\"" + clazz + ">" + text + "</a>";
}

function clickAjaxLink(target, url) {
    if (!isBlank(target) && target !== 'undefined') {
        var div = $("#" + target).load(url);
        if(div.dialog) {
            div.dialog('open');
        }
    } else {
        $.post(url);
    }
}

//a formatter for setting up a link that when clicked will go to the specified tab.
//TODO DEPRECATED
function tabLinkFormatter(cellvalue, options, rowObject) {
    if (!cellvalue) {
        return "";
    }
    return "<a id='" + rowObject.name + "Link' href='#' onclick=\"selectTab('form_"+ rowObject[options.tab]+"Tab')\">" + cellvalue + "</a>";
}

//TODO DEPRECATED
function setupGridLinkOp(cellvalue, options, rowObject) {
    var op = {
        url: options.colModel.formatoptions.url,
        paramName: 'see linkFormatter.paramName',
        paramValue:'"cellvalue.param"'
    };
    if (options.colModel.formatoptions) {
        op = $.extend({},op,options.colModel.formatoptions);
    }

    op.id = options.rowId;
    op.imageTitle = options.colModel.formatoptions.imageTitle;
    op.imageTitlePrefix = options.colModel.formatoptions.imageTitlePrefix;
    op.imageUrl = options.colModel.formatoptions.imageUrl;

    return op;
}

//TODO DEPRECATED
function simplifyOptions(cellvalue, options, rowObject) {

    if(options.configured) {
        return options;
    }
    var op;
    if (!options.colModel) {
        op = options;
    } else {
        op = setupGridLinkOp(cellvalue, options, rowObject);
    }

    var paramValue;
    if (rowObject) {
        paramValue = getNestedProperty(rowObject,op.paramValue);
    } else {
        paramValue = op.paramValue;
    }

    if (!isBlank(paramValue)) {
        op.url += (op.url.indexOf("?") > 0 ? "&" : "?") + op.paramName + "=" + paramValue;
    }
    if (op.addParam) {
        op.url += (op.url.indexOf("?") > 0 ? "&" : "?") + op.addParam;
    }
    op.configured = true;
    return op;
}

//TODO DEPRECATED
function createLinkImage(cellvalue, options) {
    var imageTitle;
    if (!options.imageTitle) {
        imageTitle = options.imageTitlePrefix + " " + cellvalue;
    } else {
        imageTitle = options.imageTitle;
    }

    var imageUrl = options.imageUrl;
    return "<img src='" + imageUrl +"' title='" + imageTitle + "' alt='" + imageTitle + "' />";
}

function createImage(image, altText, cssClass) {
    var imageUrl = contextPath + "/images/" + image;
    var css = '';
    if(!isBlank(cssClass)) {
        css = " class='" + cssClass + "' ";
    }
    return "<img src='" + imageUrl +"' title='" + altText + "' alt='" + altText + "'" + css + "/>";
}

function isBlank(input) {
    return input === null || $.trim(input) === '';
}

function createNamedElement(type, name) {
    var element = null;
    // Try the IE way; this fails on standards-compliant browsers
    try {
        element = $('<'+type+' name="'+name+'" />');
    } catch (e) {
    }
    if (!element || isBlank($(element).attr("name"))) {
        // Non-IE browser; use canonical method to create named element
        element = $("<" +type + "/>");
        $(element).attr("name", name);
    }
    return element;
}

function formatDate(val, includeDay) {
    var ret = "";
    if (!isBlank(val)) {
        var dateFields = /(\d{4})-(\d\d)-(\d\d)/.exec(val);

        ret += dateFields[2] + "/";
        if(includeDay) {
            ret += dateFields[3] + "/";
        }
        ret += dateFields[1];
    } else {
        ret = "N/A";
    }
    return ret;
}

function formatDateOrEmpty(val, includeDay) {
  var ret = "";
  if (!isBlank(val)) {
    ret = formatDate(val, includeDay);
  }
  return ret;
}

function getDateAsYear(val) {
    var ret = "";
    if (!isBlank(val)) {
        var dateFields = /(\d{4})-(\d\d)-(\d\d)/.exec(val);
        ret = dateFields[1];
    }
    return ret;
}

function getNestedProperty(obj, property) {
    var props = property.split(".");
    var paramValue = obj;
    for (var i = 0; i < props.length; i++) {
        paramValue = paramValue[props[i]];
    }
    return paramValue;
}

function refreshPage(timeoutMilli, isTab) {
    if(!timeoutMilli || !/^\d+$/.test(timeoutMilli)) {
        timeoutMilli = 500;
    }
    indicateLoading(true);
    if (isTab) {
        setTimeout('reloadCurrentTab();', timeoutMilli);
    } else {
        setTimeout('window.location.reload();', timeoutMilli);
    }
}

function commentDisplayFormatter(cellvalue, options, rowObject) {
    if(!rowObject) {
        rowObject = options;
    }

    if(!isBlank(cellvalue)) {
        return ajaxLinkFormatter(
            createImage('ico_comments_present.gif', 'View / Edit Comments'),
            {
                url: options.url,
                paramName:'formType.id',
                paramValue: 'formTypeId',
                action: 'comments',
                target: options.dialog
            },
            rowObject);
    }
    return "";
}

/**
 * @param form id string or form instanceid of the form to watch.
 **/
function setUpProtocolForm(form) {

    form.checkForChangesAndBlock = function (event) {
        if (form.hasChanges() && !confirm(form.messages["sponsor.protocol.edit.close.warning"])) {
            event.preventDefault();
            event.stopImmediatePropagation();
            return false;
        }
        return true;
    };

    form.hasChanges = function() {
        return !_.isEqual(form.formSnapshot, form.getSerializedState());
    };

    form.formSnapshot = form.getSerializedState();
}

//For User Registration Module
function clickUserRegistrationSelectButton(url, params) {
    disableDialog();

    $.post(url, params, function(data) {
        enableDialog();
        $('#registrationContent').html(data);
    });
}

function escapeForJquery(stringToEscape) {
    return stringToEscape.replace(/(\.|:)/g,'\\$1');
}

function clearAutoComplete(autoCompleteId) {
    $('#' + autoCompleteId + '_widget').val('');
    $('#' + autoCompleteId).val('');
}

function setPageErrorMessages(messages) {
    setMessages(messages, 'pageErrorMessage');
}

function setMessages(messages, css) {
    var messageDiv = getLocalMessageDiv();
    clearMessages();
    if (!_.isUndefined(messages) && (!_.isEmpty(messages))){
        var messageList = $('<ul></ul>').appendTo(messageDiv);
        if (!_.isArray(messages)) {
            messages = [messages];
        }
        _.each(messages, function(message) {
            $(messageList).append(createMessage(message, css));
        });
    }
}

function getLocalMessageDiv() {
    var dialog = getCurrentDialog();
    if ($(dialog).length > 0 && $(dialog).dialog('isOpen')) {
        return $(dialog).find('.errors').last();
    } else if($("#tabwrapper div.ui-tabs-panel:not([aria-hidden='true'])").length > 0) {
        return $("#tabwrapper div.ui-tabs-panel:not([aria-hidden='true'])").find('.errors').first();
    } else {
        return $('.errors').first();
    }
}

function clearMessages() {
    $('.errors').each(function(){
        $(this).html('');
    });
}

function createMessage(messageText, css) {
    return $('<li class="'+ css + '"></li>').text(messageText);
}

function setPageSuccessMessage(messages) {
    setMessages(messages, 'validMessage');
}

function addSeperatorToSelectList(selectListId, index) {
    $('#' + selectListId + ' option:eq(' + index + ')').after('<option disabled="disabled"/>');
}

function testForEnter(event) {
    if (event.keyCode === 13 || event.which === 13) {
        event.preventDefault();
    }
}

function isValidEmail(email) {
  var emailRegEx = /[a-z0-9!#$%&'\*\+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
  return emailRegEx.test(email);
}

function trimLeft(text) {
  return text.replace(/^\s+/, "");
}

function createHiddenCheckboxLabel(forId) {
  return "<label for='" + forId + "' class='hide'>Select</label>";
}

var _fbUtil = (function(){
    var fbUtil = {};

    fbUtil.loadingIcon =  $('<div class="ico_btn">' + createImage('loading.gif','Loading') + '</div>');

    fbUtil.blankIfNull = function (input) {
        if (input === null) {
            return '';
        }
        return input;
    };

    fbUtil.switchObjects = function (current, toDisplay) {
        var parent = $(current).parent();
        $(current).detach();
        $(parent).append(toDisplay);
    };

    /*
     * A Utility method to easily just dump the string display of an object to an alert for debug purposes.
     */
    fbUtil.showObjectAsString = function (object) {
        alert(JSON.stringify(object));
    };

    fbUtil.performAjaxPost = function(url, target, formData, callback) {
        var success = false;
        disableDialog();
        $.post(url, formData).success(function(data){
            success = true;
            if(target) {
                $(target).html(data);
            }
         }).error(function(){
             setPageErrorMessages('<fmt:message key="error.problem.submitting.data"/>');
         }).complete(function(){
             if(callback) {
                 callback.call();
             }
             enableDialog();
         });
        return success;
    };

    return fbUtil;
})();
