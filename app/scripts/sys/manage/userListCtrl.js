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
                console.log($scope.vm.userList);
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

manageApp.controller('UserModalCtrl', ['$scope', 'close', 'UserService', 'title', 'initVal', function ($scope, close, UserService, title, initVal) {
    $scope.modalTitle = title;
    $scope.vm = {
        user: {
        }
    };
    $scope.isModify = false;

    $scope.userGroups = [];
    $scope.departments = [];

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
        console.log($scope.vm.user);
        if(title == "修改用户") {
            $scope.isModify = true;
        }
        UserService.retrieveGroups().then(function (userGroups) {
            $scope.userGroups = _.map(userGroups.items, UserService.toViewModelGroup);
            console.log($scope.userGroups);
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
        UserService.retrieveDepartment().then(function (departments) {
            $scope.departments = _.map(departments.items, UserService.toViewModelDepartment);
            if(title == "新增用户") {
                $scope.vm.user.department = $scope.departments[0];
            }else {
                for(var i=0; i<$scope.departments.length; i++){
                    if($scope.vm.user.department.id == $scope.departments[i].id) {
                        $scope.vm.user.department = $scope.departments[i];
                        break;
                    }
                }
            }
        });
    };

    init();
}]);