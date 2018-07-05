'use strict';

manageApp.service('UserService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var usersUri = URI.resources.users;
    var groupsUri = URI.resources.groups;
    var departmentUri = URI.resources.department;

    this.toViewModel = function (userFromApi) {
        return {
            id: userFromApi.id,
            userId: userFromApi.userId,
            name: userFromApi.name,
            password: userFromApi.password,
            identity: userFromApi.identity,
            userGroup: userFromApi.userGroup,
            department: userFromApi.department,
            jobNumber: userFromApi.jobNumber,
            mobilePhone: userFromApi.mobilePhone,
            officePhone: userFromApi.officePhone,
            email: userFromApi.email,
            createTime: userFromApi.createTime
        }
    };
    this.toViewModelGroup = function (groupFromApi) {
        return {
            id: groupFromApi.id,
            code: groupFromApi.code,
            name: groupFromApi.name,
        }
    };

    this.toViewModelDepartment = function (departmentFromApi) {
        return {
            id: departmentFromApi.id,
            code: departmentFromApi.code,
            name: departmentFromApi.name,
        }
    };

    this.retrieveGroups = function () {
        return promise.wrap($http.get(groupsUri));
    };

    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

    this.getUserInfoById = function (userId) {
        return promise.wrap($http.get(usersUri + '/' + userId));
    };

    this.createUser = function (user) {
        return promise.wrap($http.post(usersUri, user));
    };

    this.modifyUser = function (user) {
        return promise.wrap($http.put(usersUri + "?userId=" + user.userId, user));
    };

    this.deleteUser = function (user) {
        return promise.wrap($http.delete(usersUri + "?userId=" + user.userId));
    };

    //查询部门信息(下级递归)
    this.retrieveDepartmentLower = function (code) {
        return promise.wrap($http.get(departmentUri + "/Lower"+"?code=" +code));
    };

    //查询部门信息(上级递归)
    this.retrieveDepartmentUpper = function (code) {
        return promise.wrap($http.get(departmentUri + "/Upper"+"?code=" +code));
    };

    //增加部门
    this.createDepartment = function (department) {
        return promise.wrap($http.post(departmentUri, department));
    };

    //删除部门
    this.deleteDepartment = function (department) {
        return promise.wrap($http.delete(departmentUri+"?code="+department.code));
    };

    //修改部门
    this.modifyDepartment = function (originalDepartment, destDepartment) {
        return promise.wrap($http.put(departmentUri+"?code="+originalDepartment.code, destDepartment));
    };


}]);

