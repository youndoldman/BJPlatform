'use strict';

manageApp.service('UserService', ['$http', 'URI', 'promiseWrapper','$q', function ($http, URI, promise,$q) {

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




    var dataURItoBlob = function(dataURI) {
        // convert base64/URLEncoded data component to raw binary data held in a string
        var byteString;
        if (dataURI.split(',')[0].indexOf('base64') >= 0)
            byteString = atob(dataURI.split(',')[1]);
        else
            byteString = unescape(dataURI.split(',')[1]);

        // separate out the mime component
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

        // write the bytes of the string to a typed array
        var ia = new Uint8Array(byteString.length);
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        return new Blob([ia], {
            type: mimeString
        });
    };

    //图片压缩
    this.resizeFile = function(file) {
        var deferred = $q.defer();
        var img = new Image();
        try {
            var reader = new FileReader();
            reader.onload = function(e) {
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);


            img.onload = function () {
                //resize the image using canvas
                var canvas = document.createElement("canvas");
                var ctx = canvas.getContext("2d");
                ctx.drawImage(img, 0, 0);
                var MAX_WIDTH = 100;
                var MAX_HEIGHT = 100;
                var width = MAX_WIDTH;
                var height = MAX_HEIGHT;
                //if (width > height) {
                //    if (width > MAX_WIDTH) {
                //        height *= MAX_WIDTH / width;
                //        width = MAX_WIDTH;
                //    }
                //} else {
                //    if (height > MAX_HEIGHT) {
                //        width *= MAX_HEIGHT / height;
                //        height = MAX_HEIGHT;
                //    }
                //}
                canvas.width = width;
                canvas.height = height;
                var ctx = canvas.getContext("2d");
                ctx.drawImage(img, 0, 0, width, height);

                //change the dataUrl to blob data for uploading to server
                var dataURL = canvas.toDataURL('image/jpeg');
                var blob = dataURItoBlob(dataURL);

                deferred.resolve(blob);

            }
        } catch (e) {
            deferred.resolve(e);
        }
        return deferred.promise;

    };



}]);

