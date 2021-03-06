'use strict';

manageApp.controller('UserListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'UserService', '$timeout', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, UserService,$timeout) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchUsers();
        };

        $scope.pager = pager.init('UserListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            userId: null,
            userName: null,
            userGroup:{name:"全选",code:null},
            liableDepartmentCode: null,
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

        $scope.uploadImage = function (user) {
            udcModal.show({
                templateUrl: "./manage/userImageModal.htm",
                controller: "UserImageModalCtrl",
                inputs: {
                    title: '上传用户照片',
                    initVal: user
                }
            }).then(function (result) {
                if (result) {
                }
            })
        };

        $scope.delete = function (user) {
            udcModal.confirm({"title": "删除用户", "message": "是否永久删除用户信息 " + user.name})
                .then(function (result) {
                    if (result) {
                        UserService.deleteUser(user).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除用户成功 "});
                            searchUsers();
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "修改用户失败 " + value.message});
                        })
                    }
                })
        };

        var searchUsers = function () {
            var queryParams = {
                userId: $scope.q.userId,
                userName: $scope.q.userName,
                pageNo: $scope.pager.getCurPageNo(),
                orderBy:"alive_status desc",
                pageSize: $scope.pager.pageSize,
                groupCode:$scope.q.userGroup.code,
                departmentCode:$scope.q.liableDepartmentCode
            };

            UserService.retrieveUsers(queryParams).then(function (users) {
                $scope.pager.update($scope.q, users.total, queryParams.pageNo);
                $scope.vm.userList = users.items;//_.map(users.items, UserService.toViewModel);
                //console.log($scope.vm.userList);
            });
        };

        var retrieveUserGroups = function(){
            UserService.retrieveGroups().then(function (userGroups) {
                $scope.vm.userGroups = _.map(userGroups.items, UserService.toViewModelGroup);
                //剔除客户
                for(var i=0; i<$scope.vm.userGroups.length;i++){
                    if($scope.vm.userGroups[i].code=="00004"){//客户
                        $scope.vm.userGroups.splice(i,1);
                    }
                }
                //增加全选
                $scope.vm.userGroups.unshift({name:"全选",code:null});
                $scope.q.userGroup = $scope.vm.userGroups[0];
            });
        };

        var init = function () {
            searchUsers();
            retrieveUserGroups();
        };

        //修改用户的权限状态
        $scope.changeServiceStatus = function (user) {
            var messageDesc = "";
            var newUser = {userId:user.userId, serviceStatus:null};
            if(user.serviceStatus.index==0){
                messageDesc="是否禁用用户抢单权限！";
                newUser.serviceStatus = "SUSForbidden";
            }else{
                messageDesc="是否恢复用户抢单权限！";
                newUser.serviceStatus = "SUSNormal";
            }

            udcModal.confirm({"title": "权限变更", "message": messageDesc + user.name})
                .then(function (result) {
                    if (result) {
                        UserService.modifyUser(newUser).then(function () {
                            udcModal.info({"title": "处理结果", "message": "权限变更成功 "});
                            searchUsers();
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "权限变更失败 " + value.message});
                        })
                    }
                })
        };

        $scope.initDepartmentSelect = function () {
            udcModal.show({
                templateUrl: "./manage/departmentSelectModal.htm",
                controller: "DepartmentSelectModalCtrl",
                inputs: {
                    title: '百江燃气组织架构',
                    initVal: {}
                }
            }).then(function (result) {
                if (result!=null) {
                    $scope.q.liableDepartmentCode = result.code;
                    //console.info($scope.q.liableDepartmentCode);
                    searchUsers();
                }
            })
        };

        init();

    }]);

manageApp.controller('UserModalCtrl', ['$scope', 'close', 'UserService', 'title', 'initVal','$document','$interval', 'udcModal','$timeout',function ($scope, close, UserService, title, initVal,$document,$interval,udcModal,$timeout) {
    $scope.dataSource = {};
    $scope.chart = null;
    var chartInitial = function () {
        $scope.chart = $('#chart-container').orgchart({
            'data': $scope.dataSource,
            'chartClass': 'edit-state',
            'exportFilename': 'SportsChart',
            'parentNodeSymbol': 'fa-th-large',
            'nodeContent': 'title',
            'direction': 'l2r'
            //'visibleLevel':'4',
            //'toggleSiblingsResp':true,
            //'pan':true
        });
        $scope.chart.$chartContainer.on('click', '.node', function () {
            var $this = $(this);
            $scope.vm.user.department.code = $this.find('.content').text();
            $scope.vm.user.department.name = $this.find('.title').text();
            //查询完整的部门路径
            retriveDepartmentInfo();
        });
    };
    var redrawDepartment = function () {
        $scope.chart.init({'data': $scope.dataSource});
    };

    $timeout(function(){
        chartInitial();
        retrieveRootDepartment();
    },500);

    var departmentConvertToDataSoure = function (department) {
        var chartColors = ["middle-level", "product-dept", "rd-dept", "pipeline1", "frontend1"];
        var random = Math.floor(Math.random() * 5);
        var data = {name: department.name, title: department.code, children: [], className: chartColors[random]};
        for (var i = 0; i < department.lstSubDepartment.length; i++) {
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
        user: {},
        departmentInfo: "",
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
                udcModal.info({"title": "处理结果", "message": "创建用户成功！"});
                //$scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "创建用户失败 " + value.message});
            })
        } else if (user.name != "" && title == "修改用户") {
            UserService.modifyUser(user).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改用户成功！"});
                //$scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "修改用户失败 " + value.message});
            })
        }
    };

    var init = function () {
        $scope.vm.user = _.clone(initVal);
        //删除抢单权限状态
        delete $scope.vm.user.serviceStatus;
        //删除多余元素
        deleteUserRedundance();
        if ($scope.vm.user.department == null) {
            $scope.vm.user.department = {code: null, name: null};
        }
        if (title == "修改用户") {
            $scope.isModify = true;
        }
        UserService.retrieveGroups().then(function (userGroups) {
            $scope.userGroups = _.map(userGroups.items, UserService.toViewModelGroup);
            //剔除客户
            for (var i = 0; i < $scope.userGroups.length; i++) {
                if ($scope.userGroups[i].code == "00004") {//客户
                    $scope.userGroups.splice(i, 1);
                }
            }

            if (title == "新增用户") {
                $scope.vm.user.userGroup = $scope.userGroups[0];
            } else {
                for (var i = 0; i < $scope.userGroups.length; i++) {
                    if ($scope.vm.user.userGroup.id == $scope.userGroups[i].id) {
                        $scope.vm.user.userGroup = $scope.userGroups[i];
                        break;
                    }
                }
            }

        });
        if (title == "新增用户") {

        } else {
            //查询完整的部门路径
            retriveDepartmentInfo();

        }


        //规避不同作用域的BUG
        $scope.timer = $interval(function () {
            refleshSelectInfo()
        }, 100);

    };
    var refleshSelectInfo = function () {
        $scope.vm.user.department = $scope.vm.user.department
        $scope.vm.departmentInfo = $scope.vm.departmentInfo;

    };
    //查询完整的部门路径
    var retriveDepartmentInfo = function () {
        if ($scope.vm.user.department != null) {
            console.log($scope.vm.user.department);
            UserService.retrieveDepartmentUpper($scope.vm.user.department.code).then(function (department) {
                console.log($scope.vm.user.department);
                $scope.vm.departmentInfo = $scope.vm.user.department.name;
                var tempDepartment = department.items[0];
                while (tempDepartment.parentDepartment != null) {
                    tempDepartment = tempDepartment.parentDepartment;
                    $scope.vm.departmentInfo = $scope.vm.departmentInfo + "-" + tempDepartment.name;
                }
            })
        }
    };
    var deleteUserRedundance = function () {
        delete $scope.vm.user.createTime;
        delete $scope.vm.user.updateTime;
        delete $scope.vm.user.userPosition;
        delete $scope.vm.user.aliveStatus;
        delete $scope.vm.user.aliveUpdateTime;

    };

    init();
}]);

manageApp.controller('UserImageModalCtrl', ['$scope', 'close', 'UserService', 'title', 'initVal','$document','$interval', 'udcModal','$timeout','Upload','URI',function ($scope, close, UserService, title, initVal,$document,$interval,udcModal,$timeout,Upload,URI) {

    $scope.modalTitle = title;
    $scope.vm = {
        image:null,
        user: {},
          };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (user) {
        console.log($scope.vm.image);
        UserService.resizeFile($scope.vm.image).then(function(url_compressed) {
            var url = URI.resources.SysUserPhoto+"/"+user.userId;  //params是model传的参数，图片上传接口的url


            Upload.upload({
                method: 'PUT',
                url: url,
                file: url_compressed
            }).success(function () {
                udcModal.info({"title": "处理结果", "message": "上传用户照片成功 "});
            }).error(function () {
                udcModal.info({"title": "处理结果", "message": "上传用户照片失败 "});
            });
        }, function(err_reason) {
            console.log(err_reason);
        });
    };

    var init = function () {

        $scope.vm.user = _.clone(initVal);
        $scope.vm.image = URI.resources.SysUserPhoto+"/"+$scope.vm.user.userId;



    };


    init();
}]);
