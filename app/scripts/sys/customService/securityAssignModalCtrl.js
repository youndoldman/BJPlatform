customServiceApp.controller('SecurityAssignModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','$timeout','$interval', 'udcModal',function ($scope, close, MendSecurityComplaintService, title, initVal,$timeout,$interval,udcModal) {
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
    $scope.$watch('$viewContentLoaded',function () {
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
        MendSecurityComplaintService.retrieveDepartmentLower("root").then(function (rootDepartment) {
            $scope.vm.rootDepartment = rootDepartment.items[0];
            $scope.dataSource = departmentConvertToDataSoure($scope.vm.rootDepartment);
            redrawDepartment();
        });
    };
    //================================================



    $scope.modalTitle = title;
    $scope.vm = {
        security:null,
        rootDepartment: {},
        selectDepartment:{code:null,name:null}
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        if($scope.vm.selectDepartment.code!=null){
            $scope.vm.security.department = $scope.vm.selectDepartment;
            $scope.vm.security.processStatus = "PTHandling";
            MendSecurityComplaintService.modifySecurity($scope.vm.security).then(function () {
                udcModal.info({"title": "处理结果", "message": "投诉单指派成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "投诉单指派失败 "+value.message});
            })
        }

    };

    var init = function () {
        $scope.vm.security = _.clone(initVal);
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