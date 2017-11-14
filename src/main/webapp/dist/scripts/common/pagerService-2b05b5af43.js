'use strict';

commonModule.factory('pager', ['sessionStorage', function (sessionStorage) {
    var ORDER = {DESC: 'desc', ASC: 'asc'};

    var reverseOrder = function (orderType) {
        return orderType === ORDER.DESC ? ORDER.ASC : ORDER.DESC;
    };

    var getOrderIcon = function (orderName) {
        if (!orderName) {
           return 'fa-sort';
        }
        if (this.orderBy === orderName) {
            if (this.order === 'desc') {
                return 'fa-sort-desc';
            }
            return 'fa-sort-asc';
        }
        return 'fa-sort';
    };

    var flushSessionStorage = function (storeKey, q, totalCount, curPageNo, pageSize, orderBy, order, pagerOrder) {
        sessionStorage.put(storeKey, {q: q, totalCount: totalCount, curPageNo: curPageNo,
            pageSize: pageSize, orderBy: orderBy, order: order, pagerOrder: pagerOrder});
    };

    var Pagination = function (storeKey, func, q, curPageNo, totalCount) {

        var previousInfo = sessionStorage.get(storeKey) || {};
        this.storeKey = storeKey || "defaultKey";
        this.curPageNo = previousInfo.curPageNo || curPageNo || 1;
        this.q = _.clone(previousInfo.q || q || {});
        this.totalCount = previousInfo.totalCount || totalCount || 0;
        this.loadFunc = func || _.noop;
        this.pageSize = previousInfo.pageSize || 25;
        this.orderBy = previousInfo.orderBy || undefined;
        this.order = previousInfo.order || ORDER.DESC;
        this.pagerOrder = previousInfo.pagerOrder || {};
    };

    Pagination.prototype.update = function (q, totalCount, curPageNo) {
        this.q = _.clone(q);
        this.curPageNo = curPageNo || this.curPageNo || 1;
        this.totalCount = totalCount;
        flushSessionStorage(this.storeKey, this.q, this.totalCount,
            this.curPageNo, this.pageSize, this.orderBy, this.order, this.pagerOrder);
    };

    Pagination.prototype.setOrder = function (orderBy, order) {
        if (this.pagerOrder[orderBy]) {
            this.pagerOrder[orderBy] = reverseOrder(this.pagerOrder[orderBy]);
        } else {
            this.pagerOrder[orderBy] = order || ORDER.DESC;
        }
        this.orderBy = orderBy;
        this.order = this.pagerOrder[orderBy];
    };

    Pagination.prototype.setCurPageNo = function (pageNo) {
        this.curPageNo = pageNo;
    };

    Pagination.prototype.setPageSize = function (pageSize) {
        this.pageSize = pageSize;
    };

    Pagination.prototype.getCurPageNo = function () {
        return this.curPageNo || 1;
    };

    Pagination.prototype.getQ = function () {
        return this.q || {};
    };

    Pagination.prototype.getPageSize = function () {
        return this.pageSize;
    };

    Pagination.prototype.getPageOrderBy = function () {
        return this.orderBy;
    };

    Pagination.prototype.getPageOrder = function () {
        return this.order;
    };

    Pagination.prototype.getOrderIcon = function () {
        return getOrderIcon();
    };

    var init = function (storeKey, func, q, curPageNo, totalCount) {
        return new Pagination(storeKey, func, q, curPageNo, totalCount);
    };

    return {
        init: init
    }

}]);

