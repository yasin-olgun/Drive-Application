var app = angular.module("my-app",
    ['ngRoute', 'ngResource', 'ui.bootstrap', 'ngFileUpload']);

app.config(function ($routeProvider, $locationProvider) {

    $locationProvider.html5Mode(true);

    $routeProvider
        .when('/my-drive', {
            template: '<my-drive></my-drive>'
        })
        .when('/login', {
            template: '<login></login>'
        })
        .when('/register', {
            template: '<register></register>'
        })
        .when('/shared-with-me', {
            template: '<shared-with-me></shared-with-me>'
        })
        .otherwise({
            redirectTo: "/"
        });
});


app.factory('UserApi', ['$resource', function ($resource) {

    var baseUrl = "/user";

    return $resource('/user/:id', {id: '@id'}, {
        login: {
            method: 'POST',
            url: baseUrl + "/login"
        },
        register: {
            method: 'POST',
            url: baseUrl + "/register"
        },
        files: {
            method: 'GET',
            url: baseUrl + "/file",
            isArray: true,
        },
        upload: {
            method: 'POST',
            url: baseUrl + "/upload",
            transformRequest: function (data) {

                var fd = new FormData();

                fd.append("file", data);
                return fd;
            },
            headers: {"Content-Type": undefined}
        },
        delete: {
            method: 'DELETE',
            url: baseUrl + "/:id"
        },
        getUser: {
            method: 'GET',
            url: baseUrl
        },
        share: {
            method: 'POST',
            url: baseUrl + '/share/'
        },
        unshare: {
            method: 'POST',
            url: baseUrl + '/unshare'
        },
        sharedWithMe: {
            method: 'GET',
            url: baseUrl + '/shared-file',
            isArray: true,
        },
        userList: {
            method: 'POST',
            url: baseUrl,
            isArray: true,
        }
    });

}]);


app.filter("timeago", function () {
    //time: the time
    //local: compared to what time? default: now
    //raw: wheter you want in a format of "5 minutes ago", or "5 minutes"
    return function (time, local, raw) {
        if (!time) return "never";

        if (!local) {
            (local = Date.now())
        }

        if (angular.isDate(time)) {
            time = time.getTime();
        } else if (typeof time === "string") {
            time = new Date(time).getTime();
        }

        if (angular.isDate(local)) {
            local = local.getTime();
        } else if (typeof local === "string") {
            local = new Date(local).getTime();
        }

        if (typeof time !== 'number' || typeof local !== 'number') {
            return;
        }

        var
            offset = Math.abs((local - time) / 1000),
            span = [],
            MINUTE = 60,
            HOUR = 3600,
            DAY = 86400,
            WEEK = 604800,
            MONTH = 2629744,
            YEAR = 31556926,
            DECADE = 315569260;

        if (offset <= MINUTE) span = ['', raw ? 'now' : 'less than a minute'];
        else if (offset < (MINUTE * 60)) span = [Math.round(Math.abs(offset / MINUTE)), 'min'];
        else if (offset < (HOUR * 24)) span = [Math.round(Math.abs(offset / HOUR)), 'hr'];
        else if (offset < (DAY * 7)) span = [Math.round(Math.abs(offset / DAY)), 'day'];
        else if (offset < (WEEK * 52)) span = [Math.round(Math.abs(offset / WEEK)), 'week'];
        else if (offset < (YEAR * 10)) span = [Math.round(Math.abs(offset / YEAR)), 'year'];
        else if (offset < (DECADE * 100)) span = [Math.round(Math.abs(offset / DECADE)), 'decade'];
        else span = ['', 'a long time'];

        span[1] += (span[0] === 0 || span[0] > 1) ? 's' : '';
        span = span.join(' ');

        if (raw === true) {
            return span;
        }
        return (time <= local) ? span + ' ago' : 'in ' + span;
    }
});
