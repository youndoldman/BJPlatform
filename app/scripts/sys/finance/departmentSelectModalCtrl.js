/**
 * Created by Administrator on 2018/4/13.
 */
financeApp.controller('DepartmentSelectModalCtrl', ['$scope', 'close', 'FinanceService', 'title', 'initVal','$timeout','$interval', function ($scope, close, FinanceService, title, initVal,$timeout,$interval) {
    $scope.dataSource = {};
    $scope.chart = null;
    var chartInitial = function() {
        $scope.chart = $('#chart-container').orgchart({
            'data' : $scope.dataSource,
            'chartClass': 'edit-state',
            'exportFilename': 'SportsChart',
            'parentNodeSymbol': 'fa-th-large',
            'nodeContent': 'title'
        });
        $scope.chart.$chartContainer.on('click', '.node', function() {
            var $this = $(this);
            $scope.vm.selectDepartment.code = $this.find('.content').text();
            $scope.vm.selectDepartment.name = $this.find('.title').text();
        });
    }
    var redrawDepartment = function(){
        $scope.chart.init({ 'data': $scope.dataSource });
    };
    $timeout(function() {
        chartInitial();
        retrieveRootDepartment();

    });

    var departmentConvertToDataSoure = function (department) {
        var chartColors = ["middle-level","product-dept","rd-dept","pipeline1","frontend1"];
        var random = Math.floor(Math.random()*5);
        var data = {name:department.name,title:department.code,children:[],className:chartColors[random]};
        for (var i=0; i<department.lstSubDepartment.length;i++){
            data.children.push(departmentConvertToDataSoure(department.lstSubDepartment[i]))
        }
        return data;
    };
    var retrieveRootDepartment = function () {
        FinanceService.retrieveDepartmentLower("root").then(function (rootDepartment) {
            $scope.vm.rootDepartment = rootDepartment.items[0];
            $scope.dataSource = departmentConvertToDataSoure($scope.vm.rootDepartment);
            redrawDepartment();
        });
    };
    //================================================



    $scope.modalTitle = title;
    $scope.vm = {
        rootDepartment: {},
        selectDepartment:{code:null,name:null}
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        $scope.close($scope.vm.selectDepartment);
    };

    var init = function () {
        //规避不同作用域的BUG
        $scope.timer = $interval( function(){
            refleshSelectInfo()
        }, 100);

    };
    var refleshSelectInfo = function(){
        $scope.vm.selectDepartment = $scope.vm.selectDepartment;
    };

    init();
}]);