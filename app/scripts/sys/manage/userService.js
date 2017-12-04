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




    this.retrieveDepartment = function () {
        return promise.wrap($http.get(departmentUri));
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

}]);

