'use strict';

manageApp.controller('DepartmentCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'UserService', '$timeout','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                        rootService, pager, udcModal, UserService,$timeout,$interval) {

        $scope.dataSource = {};
        $scope.chart = null;
        var chartInitial = function() {
            $scope.chart = $('#chart-container').orgchart({
                'data' : $scope.dataSource,
                //'chartClass': 'edit-state',
                'exportButton': true,
                'exportFilename': '部门组织架构',
                'parentNodeSymbol': 'fa-th-large',
                'nodeContent': 'title',
                'direction': 'l2r'
                //'visibleLevel':'4',
                //'toggleSiblingsResp':true,
                //'pan':true

            });
            $scope.chart.$chartContainer.on('click', '.node', function() {
                var $this = $(this);
                $scope.q.selectDepartment.code = $this.find('.content').text();
                $scope.q.selectDepartment.name = $this.find('.title').text();
                console.log($scope.vm.selectDepartmentCode);
            });
        };

        var redrawDepartment = function(){
            $scope.chart.init({ 'data': $scope.dataSource });
        };



        $scope.q = {
            selectDepartment:{name:null,code:null},
            addDepartment:{name:null,code:null},
            modifyDepartment:{name:null,code:null}
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

        $timeout(function(){
            chartInitial();
            retrieveRootDepartment();
        },500);

        var init = function () {

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
            if(($scope.q.selectDepartment.code==null)||($scope.q.addDepartment.code==null)){
                udcModal.info({"title": "错误信息", "message": "未选择部门或信息不全！"});
                return;
            }
            $scope.q.addDepartment.parentDepartment = $scope.q.selectDepartment;
            UserService.createDepartment($scope.q.addDepartment).then(function () {
                udcModal.info({"title": "处理结果", "message": "增加部门成功"});
                retrieveRootDepartment();
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })
        };
        //删除部门
        $scope.deleteDepartment = function(){
            if($scope.q.selectDepartment.code==null){
                udcModal.info({"title": "错误信息", "message": "未选择部门或信息不全！"});
                return;
            }
            UserService.deleteDepartment($scope.q.selectDepartment).then(function () {
                udcModal.info({"title": "处理结果", "message": "删除部门成功"});
                retrieveRootDepartment();
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })
        };
        //修改部门
        $scope.modifyDepartment = function(){
            if(($scope.q.selectDepartment.code==null)||($scope.q.modifyDepartment.code==null)){
                udcModal.info({"title": "错误信息", "message": "未选择部门或信息不全！"});
                return;
            }
            UserService.modifyDepartment($scope.q.selectDepartment, $scope.q.modifyDepartment).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改部门成功"});
                retrieveRootDepartment();
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })
        };


        init();

    }]);