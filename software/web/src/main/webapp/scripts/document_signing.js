/**
 * Provides functions for displaying and working with registration forms to be signed or displayed.
 */
function renderDocumentsTable(dataRows, formDownloadUrl, documentDownloadUrl, tableId) {
    if (tableId == undefined) {
        tableId = "documentsTable";
    }
    var tableLocator = '#' + tableId;
    $(tableLocator).dataTable({
        aaData : dataRows,
        bInfo : false,
        bLengthChange : false,
        bPaginate : false,
        bFilter : false,
        aaSortingFixed: [[ 0, 'asc' ], [1, 'asc']],
        aoColumns: [
                    {mDataProp: "parentForm", bVisible: false},
                    {mDataProp: "name", fnRender : function(obj) {
                      return buildLinkColumn(obj.aData);
                    }},
            {mDataProp: "signingRequired", fnRender :
                function(obj) {
                  return buildSigningRequiredColumn(obj.aData);
                }
              },
            {mDataProp: "signed", fnRender :
                  function(obj) {
                    return buildIsSignedColumn(obj.aData);
                  }
                }
        ],
        fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            $(nRow).attr("id", aData.id);
            return nRow;
        },
        fnDrawCallback: function ( oSettings ) {
          groupFormDocuments(oSettings);
        }
    });

    function buildLinkColumn(document) {
      if (document.registrationForm) {
            return linkFormatter(document.name, {
                paramName:'registrationForm.id',
                paramValue: 'id',
                action : 'downloadForm',
                url : formDownloadUrl
            }, document);
        } else {
            return linkFormatter(document.name,{
                paramName :'file.id',
                paramValue : 'id',
                action : 'download',
                url : documentDownloadUrl
            }, document);
        }
    }

    function buildSigningRequiredColumn(document) {
      if (document.signingRequired) {
            return createImage('ico_checkmark.png', "Signing Required");
         } else {
             return "";
         }
    }

    function buildIsSignedColumn(document) {
      if (document.signed) {
        return createImage('ico_checkmark.png', "Digitally Signed");
      } else {
        return "";
      }
    }

    function groupFormDocuments(oSettings) {
      if (oSettings.aiDisplay.length == 0) {
        return;
      }
      var nTrs = $('#documentsTable tbody tr');
      var iColspan = nTrs[0].getElementsByTagName('td').length;
      var sLastGroup = "";
      for ( var i = 0; i < nTrs.length; i++) {
        var iDisplayIndex = oSettings._iDisplayStart + i;
        var sGroup = oSettings.aoData[oSettings.aiDisplay[iDisplayIndex]]._aData.parentForm;
        if (sGroup != sLastGroup && sGroup != null) {
          var nGroup = document.createElement('tr');
          var nCell = document.createElement('td');
          nCell.colSpan = iColspan;
          nCell.className = "group";
          nCell.innerHTML = sGroup;
          nGroup.appendChild(nCell);
          nTrs[i].parentNode.insertBefore(nGroup, nTrs[i]);
          sLastGroup = sGroup;
        }
      }
   }
}