'use strict';

manageApp.controller('UserListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'UserService', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, UserService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchUsers();
        };

        $scope.pager = pager.init('UserListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            userName: null,
            userGroup:{code:null}
        };

        $scope.vm = {
            userList: [],
            userGroups: []
        };
        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchUsers();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./manage/userModal.htm",
                controller: "UserModalCtrl",
                inputs: {
                    title: '新增用户',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchUsers();
                }
            })
        };

        $scope.viewDetails = function (user) {
            $location.path('/users/' + user.id);
        };

        $scope.modify = function (user) {
            udcModal.show({
                templateUrl: "./manage/userModal.htm",
                controller: "UserModalCtrl",
                inputs: {
                    title: '修改用户',
                    initVal: user
                }
            }).then(function (result) {
                if (result) {
                    searchUsers();
                }
            })
        };

        $scope.delete = function (user) {
            udcModal.confirm({"title": "删除用户", "message": "是否永久删除用户信息 " + user.name})
                .then(function (result) {
                    if (result) {
                        UserService.deleteUser(user).then(function () {
                            searchUsers();
                        });
                    }
                })
        };

        var searchUsers = function () {
            var queryParams = {
                userName: $scope.q.userName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                groupCode:$scope.q.userGroup.code
            };

            UserService.retrieveUsers(queryParams).then(function (users) {
                $scope.pager.update($scope.q, users.total, queryParams.pageNo);
                $scope.vm.userList = _.map(users.items, UserService.toViewModel);
                //console.log($scope.vm.userList);
            });
        };

        var retrieveUserGroups = function(){
            UserService.retrieveGroups().then(function (userGroups) {
                $scope.vm.userGroups = _.map(userGroups.items, UserService.toViewModelGroup);
            });
        };

        var init = function () {
            searchUsers();
            retrieveUserGroups();
        };


        init();

    }]);

manageApp.controller('UserModalCtrl', ['$scope', 'close', 'UserService', 'title', 'initVal','$timeout','$interval', function ($scope, close, UserService, title, initVal,$timeout,$interval) {
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
        UserService.retrieveDepartmentLower("root").then(function (rootDepartment) {
            $scope.vm.rootDepartment = rootDepartment.items[0];
            $scope.dataSource = departmentConvertToDataSoure($scope.vm.rootDepartment);
            redrawDepartment();
        });
    };
    //================================================



    $scope.modalTitle = title;
    $scope.vm = {
        user: {
        },
        departmentInfo:"",
        rootDepartment: {},
    };
    $scope.isModify = false;

    $scope.userGroups = [];

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (user) {
        if (user.name != "" && title == "新增用户") {
            UserService.createUser(user).then(function () {
                $scope.close(true);
            })
        } else if (user.name != "" && title == "修改用户") {
            UserService.modifyUser(user).then(function () {
                $scope.close(true);
            })
        }
    };

    var init = function () {


        $scope.vm.user = _.clone(initVal);
        if($scope.vm.user.department==null){
            $scope.vm.user.department = {code:null,name:null};
        }
        if(title == "修改用户") {
            $scope.isModify = true;
        }
        UserService.retrieveGroups().then(function (userGroups) {
            $scope.userGroups = _.map(userGroups.items, UserService.toViewModelGroup);
            if(title == "新增用户") {
                $scope.vm.user.userGroup = $scope.userGroups[0];
            }else {
                for(var i=0; i<$scope.userGroups.length; i++){
                    if($scope.vm.user.userGroup.id == $scope.userGroups[i].id){
                        $scope.vm.user.userGroup = $scope.userGroups[i];
                        break;
                    }
                }
            }

        });
        if(title == "新增用户") {

        }else{
            //查询完整的部门路径
            retriveDepartmentInfo();

        }




        //规避不同作用域的BUG
        $scope.timer = $interval( function(){
            refleshSelectInfo()
        }, 100);

    };
    var refleshSelectInfo = function(){
        $scope.vm.user.department = $scope.vm.user.department
        $scope.vm.departmentInfo = $scope.vm.departmentInfo;

    };
    //查询完整的部门路径
    var retriveDepartmentInfo = function(){
        if($scope.vm.user.department!=null){
            console.log($scope.vm.user.department);
            UserService.retrieveDepartmentUpper($scope.vm.user.department.code).then(function (department) {
                console.log($scope.vm.user.department);
                $scope.vm.departmentInfo = $scope.vm.user.department.name;
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