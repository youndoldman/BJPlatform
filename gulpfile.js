var gulp = require('gulp'),
    del = require('del'),
    compass = require('gulp-compass'),
    rev = require('gulp-rev'),
    revCollector = require('gulp-rev-collector'),
    minifyHtml = require("gulp-minify-html"),
    uglify = require('gulp-uglify'),
    uglifycss = require('gulp-uglifycss'),
    imagemin = require('gulp-imagemin'),
    runSequence = require('run-sequence'),
    path = require('path'),
    connect = require('gulp-connect'),
    proxy = require('http-proxy-middleware');

var srcPaths = {
    images: 'app/images/**/*',
    scripts: 'app/scripts/**/*',
    styles: 'app/styles/scss/',
    html: 'app/pages/**/*',
    lib: 'app/lib/**/*',
    testDataBase:'app/test-data/**/*'
};

var distPaths = {
    scripts: 'src/main/webapp/dist/scripts/',
    images: 'src/main/webapp/dist/images/',
    css: 'src/main/webapp/dist/styles/stylesheets/',
    html: 'src/main/webapp/dist/pages/',
    lib: 'src/main/webapp/dist/lib/',
    testDataBase:'src/main/webapp/dist/test-data/'
};

var revPaths = {
    scripts: 'src/main/webapp/dist/tmp/rev/js/',
    css: 'src/main/webapp/dist/tmp/rev/css/'
};

gulp.task('jsmin', function () {
    return gulp.src(srcPaths.scripts)
        .pipe(uglify())
        .pipe(rev())
        .pipe(gulp.dest(distPaths.scripts))
        .pipe(rev.manifest())
        .pipe(gulp.dest(revPaths.scripts));
});

gulp.task('dev-js', function () {
    return gulp.src(srcPaths.scripts)
        .pipe(rev())
        .pipe(gulp.dest(distPaths.scripts))
        .pipe(rev.manifest())
        .pipe(gulp.dest(revPaths.scripts));
});

gulp.task('compass', function () {
    return gulp.src(srcPaths.styles)
        .pipe(compass({
            css: 'src/main/webapp/dist/tmp/styles/stylesheets',
            sass: 'app/styles/scss',
            image: 'app/styles/images'
        }))
        .pipe(gulp.dest('src/main/webapp/dist/tmp/styles/stylesheets'));
});

gulp.task('cssmin',['compass'], function () {
    return gulp.src('src/main/webapp/dist/tmp/styles/stylesheets/**/*')
        .pipe(uglifycss({
            "maxLineLen": 80,
            "uglyComments": true
        }))
        .pipe(rev())
        .pipe(gulp.dest(distPaths.css))
        .pipe(rev.manifest())
        .pipe(gulp.dest(revPaths.css));
});

gulp.task('htmlrev', function () {
    return gulp.src(['src/main/webapp/dist/tmp/rev/**/*.json', srcPaths.html])
        .pipe(revCollector())
        .pipe(minifyHtml())
        .pipe(gulp.dest(distPaths.html))
        .pipe(connect.reload());
});

gulp.task('imagemin', function () {
    return gulp.src(srcPaths.images)
        .pipe(imagemin({
            optimizationLevel: 5, //类型：Number  默认：3  取值范围：0-7（优化等级）
            progressive: true, //类型：Boolean 默认：false 无损压缩jpg图片
            interlaced: true, //类型：Boolean 默认：false 隔行扫描gif进行渲染
            multipass: true //类型：Boolean 默认：false 多次优化svg直到完全优化
        }))
        .pipe(gulp.dest(distPaths.images));
});

gulp.task('copylib', function () {
    return gulp.src([srcPaths.lib]).pipe(gulp.dest(distPaths.lib));
});

gulp.task('clean', function () {
    return del(['src/main/webapp/dist/*', 'app/tmp/*']);
});

gulp.task('dev-clean', function () {
    return del(['src/main/webapp/dist/*', '!src/main/webapp/dist/lib', '!src/main/webapp/dist/images']);
});

gulp.task('build', function (callback) {
    runSequence('clean', ['jsmin', 'compass', 'cssmin', 'imagemin', 'copylib'], ['htmlrev'], callback);
});

gulp.task('dev-build', function (callback) {
    runSequence('dev-clean', 'dev-js', 'compass', 'cssmin', ['htmlrev'], callback);
});

gulp.task('serve', function() {
    connect.server({
        root: ['./src/main/webapp/dist'],
        port: 8000,
        livereload: true,
        middleware: function(connect, opt) {
            return [
                proxy('/api',  {
                    target: 'http://localhost:8080',
                    changeOrigin:true
                })
            ]
        }
    });
});


gulp.task('watch', function () {
    gulp.watch(['app/pages/**/*', 'app/scripts/**/*', 'app/styles/scss/**/*', 'app/test-data/**/*'], ['dev-build']);
});

gulp.task('dev', ['serve', 'watch']);

gulp.task('default', ['dev']);



