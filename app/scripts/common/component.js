'use strict';

commonModule.directive('njTableNoData', [function () {
    return {
        restrict: 'AE',
        template: '<h3 class="table-nodata text-center">暂无数据</h3>'
    }
}]);
