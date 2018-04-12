'use strict';

customServiceApp.controller('MendModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','$timeout','$interval', function ($scope, close, MendSecurityComplaintService, title, initVal,$timeout,$interval) {
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
            $scope.vm.user.department.code = $this.find('.content').text();
            $scope.vm.user.department.name = $this.find('.title').text();
            //查询完整的部门路径
            retriveDepartmentInfo();
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
        MendSecurityComplaintService.retrieveDepartmentLower("root").then(function (rootDepartment) {
            $scope.vm.rootDepartment = rootDepartment.items[0];
            $scope.dataSource = departmentConvertToDataSoure($scope.vm.rootDepartment);
            redrawDepartment();
        });
    };
    //================================================



    $scope.modalTitle = title;
    $scope.vm = {
        mend: {
        },
        departmentInfo:"",
        rootDepartment: {},
    };
    $scope.isModify = false;

    $scope.userGroups = [];

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (mend) {
        if (title == "报修单详情") {
            $scope.close(true);
        } else if (title == "报修单指派") {
            MendSecurityComplaintService.modifyMend(mend).then(function () {
                $scope.close(true);
            })
        }else if (title == "报修单处理") {
            MendSecurityComplaintService.modifyMend(mend).then(function () {
                $scope.close(true);
            })
        }
    };

    var init = function () {
        $scope.vm.mend = _.clone(initVal);
        if($scope.vm.mend.department==null){
            $scope.vm.mend.department = {code:null,name:null};
        }
        if(title != "报修单指派") {
            $scope.isModify = true;
        }
        //查询完整的部门路径
        retriveDepartmentInfo();

        //规避不同作用域的BUG
        $scope.timer = $interval( function(){
            refleshSelectInfo()
        }, 100);

    };
    var refleshSelectInfo = function(){
        $scope.vm.mend.department = $scope.vm.mend.department
        $scope.vm.departmentInfo = $scope.vm.departmentInfo;

    };
    //查询完整的部门路径
    var retriveDepartmentInfo = function(){
        if($scope.vm.mend.department!=null){
            console.log($scope.vm.mend.department);
            MendSecurityComplaintService.retrieveDepartmentUpper($scope.vm.mend.department.code).then(function (department) {
                console.log($scope.vm.mend.department);
                $scope.vm.departmentInfo = $scope.vm.mend.department.name;
            var tempDepartment = department.items[0];
            while(tempDepartment.parentDepartment!=null){
                tempDepartment = tempDepartment.parentDepartment;
                $scope.vm.departmentInfo = $scope.vm.departmentInfo+"-"+tempDepartment.name;
            }
        })
        }
    }

    init();
}]);