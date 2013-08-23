function SearchSupport(searchInputId, searchResultsTableId, searchUrl) {
    this.searchInputId = searchInputId;
    this.searchResultsTableId = searchResultsTableId;
    this.searchUrl = searchUrl;
}

SearchSupport.prototype.searchKeyEvent = function() {
    var searchBox = $(this.getSearchInputLocator());
    if(this.checkValChanged()) {
        $(searchBox).data('lastVal', $(searchBox).val());
        this.clearTimeout();
        var _this = this;
        $(searchBox).data('searchTimer', setTimeout(function() {_this.autosearch();}, 300));
    }
};

SearchSupport.prototype.getSearchInputLocator = function() {
    return "#" + this.searchInputId;
};

SearchSupport.prototype.getSearchResultsTableLocator = function() {
    return "#" + this.searchResultsTableId;
};

SearchSupport.prototype.isTableConfigured = function() {
    return $(this.getSearchResultsTableLocator()).attr("tableConfigured");
};

SearchSupport.prototype.checkValChanged = function() {
    var searchBox = $(this.getSearchInputLocator());
    return $(searchBox).val() !== $(searchBox).data('lastVal');
};

SearchSupport.prototype.clearTimeout = function() {
    var searchBox = $(this.getSearchInputLocator());
    if($(searchBox).data('searchTimer')) {
        clearTimeout($(searchBox).data('searchTimer'));
    }
};

SearchSupport.prototype.clear = function() {
    $(this.getSearchInputLocator()).val('');
    $(this.getSearchResultsTableLocator()).dataTable().fnClearTable();
};

var isLegalSearch = function(searchString) {
    var containsNonUnderscoreCharacter = _.some(searchString, function(char){ return char !== '_'; });
    return searchString.length >= 3 && containsNonUnderscoreCharacter;
};

SearchSupport.prototype.autosearch = function() {
    var searchBox = $(this.getSearchInputLocator());
    var loadingIcon = $(this.getSearchInputLocator() + " ~ .loading_icon");
    var searchString = $.trim($(searchBox).val());
    var tableLocator = this.getSearchResultsTableLocator();
    if (isLegalSearch(searchString)) {
        $(tableLocator).attr("search_in_progress", true);
        loadingIcon.show();
        this.cancelOldRequest(searchBox);
        var request = $.post(this.searchUrl, {term : searchString}, function(data, textStatus, jqXHR) {
            $(tableLocator).dataTable().fnClearTable();
            $(tableLocator).dataTable().fnAddData(data);
            $(tableLocator).removeAttr("search_in_progress");
            loadingIcon.hide();
        }, 'json');
        $(searchBox).data('request', request);
    } else {
        $(this.getSearchResultsTableLocator()).dataTable().fnClearTable();
    }
};

SearchSupport.prototype.cancelOldRequest = function(searchBox) {
    var request = $(searchBox).data('request');
    if (request) {
        request.abort();
    }
};

SearchSupport.prototype.createSelectButtonColumn = function (externalId, buttonText) {
  return '<a id="select_' + externalId + '" class="button" href="javascript:void(0)">' + buttonText + '</a>';
};

SearchSupport.prototype.completeRow = function(row, rowData) {
    $(row).attr("id", rowData.externalId);

    var addButtonLink = $(row).find("a");
    var _this = this;
    $(addButtonLink).click(function() {
        _this.clickSelectButton(rowData);
    });

    return row;
};

SearchSupport.prototype.clickSelectButton = function(rowData) {
    //Stub, Page should implement this to make sure the correct functionality occurs.
    alert("ERROR: clickSelectButton() not implemented for this page");
};

SearchSupport.prototype.finishTable = function () {
    //Hide the pagination buttons unless pagination is possible.
    $(this.getSearchResultsTableLocator()).attr("tableConfigured", true);
    var numRows = $(this.getSearchResultsTableLocator()).dataTable().fnGetData().length;
    if(numRows <= 5){
        $('div.dataTables_paginate').hide();
    } else {
        $('div.dataTables_paginate').show();
    }

    if(isLegalSearch($(this.getSearchInputLocator()).val())) {
      $('div.dataTables_info').show();
    } else {
      $('div.dataTables_info').hide();
    }
};