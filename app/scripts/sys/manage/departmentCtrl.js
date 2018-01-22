'use strict';

manageApp.controller('DepartmentCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'UserService', '$timeout','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                        rootService, pager, udcModal, UserService,$timeout,$interval) {

        $scope.dataSource = {};
        $scope.chart = null;
        var chartInitial = function() {
            $scope.chart = $('#chart-container').orgchart({
                'data' : $scope.dataSource,
                'chartClass': 'edit-state',
                'exportButton': true,
                'exportFilename': 'SportsChart',
                'parentNodeSymbol': 'fa-th-large',
                'nodeContent': 'title'
            });
            $scope.chart.$chartContainer.on('click', '.node', function() {
                var $this = $(this);
                $scope.q.selectDepartment.code = $this.find('.content').text();
                $scope.q.selectDepartment.name = $this.find('.title').text();
                console.log($scope.vm.selectDepartmentCode);
            });
        }
        var redrawDepartment = function(){
            $scope.chart.init({ 'data': $scope.dataSource });
        };



        $scope.q = {
            selectDepartment:{name:null,code:null},
            addDepartment:{name:null,code:null}
        };

        $scope.vm = {
            rootDepartment: {},
        };


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
            UserService.retrieveDepartmentLower("root").then(function (rootDepartment) {
                $scope.vm.rootDepartment = rootDepartment.items[0];
                console.log($scope.vm.rootDepartment);
                $scope.dataSource = departmentConvertToDataSoure($scope.vm.rootDepartment);
                redrawDepartment();
            });
        };


        var init = function () {
            chartInitial();
            retrieveRootDepartment();


            //规避不同作用域的BUG
            $scope.timer = $interval( function(){
                refleshSelectInfo()
            }, 100);

        };
        var refleshSelectInfo = function(){
            $scope.q.selectDepartment = $scope.q.selectDepartment;
        }
        //增加部门
        $scope.addDepartment = function(){
            $scope.q.addDepartment.parentDepartment = $scope.q.selectDepartment;
            UserService.createDepartment($scope.q.addDepartment).then(function () {
                udcModal.info({"title": "处理结果", "message": "增加部门成功"});
                retrieveRootDepartment();
            });
        };
        //删除部门
        $scope.deleteDepartment = function(){
            UserService.deleteDepartment($scope.q.selectDepartment).then(function () {
                udcModal.info({"title": "处理结果", "message": "删除部门成功"});
                retrieveRootDepartment();
            });
        }


        init();

    }]);