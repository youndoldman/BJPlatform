'use strict';

commonModule.directive('njPager', [function () {

    var calculateVisiblePageRange = function (curPageNo, pageCount) {
        var startPageNo;
        var endPageNo;
        startPageNo = curPageNo < 4 ? 1 : (curPageNo - 3);
        endPageNo = startPageNo + 6;
        if (endPageNo > pageCount) {
            endPageNo = pageCount;
            startPageNo = endPageNo - 6 < 1 ? 1 : endPageNo - 6;
        }
        return _.range(startPageNo, endPageNo + 1);
    };

    var calculatePageCount = function (totalCount, pageSize) {
        if (pageSize > 0 && totalCount > 0) {
            var pageCount = Math.ceil(totalCount / pageSize);
            return pageCount < 1 ? 1 : pageCount;
        }
        return 1;
    };

    return {
        restrict: 'AE',
        templateUrl: '../pages/common/njTablePager.htm',
        scope: {
            pager: "="
        },
        replace: true,
        link: function ($scope, $element, attrs) {

            $scope.pageSizeOptions = [10, 25, 50, 100];
            $scope.hidePageSizeOptions = attrs.hidePageSizeOptions;

            function updateRangeCount() {
                $scope.pageCount = calculatePageCount($scope.pager.totalCount, $scope.pager.pageSize);
                $scope.pageRange = calculateVisiblePageRange($scope.pager.curPageNo, $scope.pageCount);
            }

            function init() {
                updateRangeCount();
            }

            $scope.pageSizeChanged = function () {
                $scope.gotoPage(1);
                updateRangeCount();
            };

            $scope.gotoPage = function (page) {
                $scope.pager.loadFunc(page)
            };

            $scope.$watch('pager.curPageNo', function () {
                updateRangeCount();
            });

            $scope.$watch('pager.totalCount', function () {
                updateRangeCount();
            });

            init();

        }
    }
}]);
