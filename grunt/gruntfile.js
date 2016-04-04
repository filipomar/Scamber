var Utils = {
    getFileLocations: function(grunt, paths, filterFn) {
        var files = [];

        paths.forEach(function(file) {
            grunt.file.recurse(file, function(absPath) {
                if (!filterFn(absPath)) {
                    return;
                }
                files.push(absPath);
            });
        });

        return files;
    },

    concatContents: function(grunt, files, contentEvalutator) {
        var contents = files
            .map(function(filePath) {
                var content = grunt.file.read(filePath);
                if (!contentEvalutator) {
                    return content;
                }
                return contentEvalutator(content);
            });

        return contents.join('\n');
    },

    getConfig: function(grunt, taskName, args) {
        var project = args[0];
        var fileIndex = args[1];

        return grunt.config(taskName)[project + fileIndex];
    },

    writeFile: function(grunt, folder, fileName, contents) {
        grunt.file.mkdir(folder);
        grunt.file.write(folder + fileName, contents);
    },

    isExtension: function(absPath, desiredExtension) {
        var extension = absPath.substring(absPath.length - desiredExtension.length, absPath.length);
        return extension === desiredExtension;
    }
};

var TaskRegister = {
    TS_CONCAT_TASK_NAME: 'concatTS',
    JS_CONCAT_TASK_NAME: 'concatJS',

    GENERATED_FILES_FOLDER: 'generated/',

    registerTSConcat: function(grunt) {
        var TaskRegister = this;

        var TS_CONCAT_TASK_NAME = TaskRegister.TS_CONCAT_TASK_NAME;
        var GENERATED_FILES_FOLDER = TaskRegister.GENERATED_FILES_FOLDER;

        var TS_EXTENSION = 'ts';
        var JS_EXTENSION = 'js';

        var REFERENCE_REMOVAL_REGEX = /\/{3,3} *<reference path=".+\.ts" \/>/gm;

        grunt.registerTask(TS_CONCAT_TASK_NAME, function() {
            var config = Utils.getConfig(grunt, TS_CONCAT_TASK_NAME, this.args);

            var files =
                Utils.getFileLocations(grunt, config.files, function(absPath) {
                    return Utils.isExtension(absPath, TS_EXTENSION);
                });

            var contents = Utils.concatContents(grunt, files, function(content) {
                return content.replace(REFERENCE_REMOVAL_REGEX, '\n');
            });

            Utils.writeFile(grunt, GENERATED_FILES_FOLDER, config.outputFile, contents);
        });
    },

    registerJSConcat: function(grunt) {
        var JS_CONCAT_TASK_NAME = this.JS_CONCAT_TASK_NAME;
        var GENERATED_FILES_FOLDER = this.GENERATED_FILES_FOLDER;

        var JS_EXTENSION = 'js';

        grunt.registerTask(JS_CONCAT_TASK_NAME, function() {
            var config = Utils.getConfig(grunt, JS_CONCAT_TASK_NAME, this.args);

            var files =
                Utils.getFileLocations(grunt, config.files, function(absPath) {
                    return Utils.isExtension(absPath, JS_EXTENSION);
                });

            var contents = Utils.concatContents(grunt, files);

            Utils.writeFile(grunt, GENERATED_FILES_FOLDER, config.outputFile, contents);
        });
    }
};

module.exports = function(grunt) {
    grunt.log.writeln('Runing FretOnde Grunt.\n'['green']);

    TaskRegister.registerTSConcat(grunt);
    TaskRegister.registerJSConcat(grunt);

    var TS_EXTENSION = 'ts';
    var JS_EXTENSION = 'js';
    var CSS_EXTENSION = 'css';
    var CONFIG_FILE = 'config.json';

    var runOnly = [];

    var watchConf = {};
    var concatTsConf = {};
    var typescriptConf = {};
    var jshintConf = {
        options: {
            curly: true,
            eqeqeq: true,
            eqnull: true,
            browser: true,
            futurehostile: true,
            undef: true,
            latedef: true,
            maxdepth: 2,
            maxparams: 4,
            nocomma: true,
            notypeof: true,
            // unused: true,
            globals: {
                console: true,
                document: true
            },
        }
    };
    var concatJsConf = {};
    var uglifyConf = {
        options: {
            sourceMap: true,
            sourceMapIncludeSources: true
        }
    };
    var cssMinConf = {
        options: {
            keepSpecialComments: 0,
            sourceMap: false
        }
    };

    grunt.file.readJSON(CONFIG_FILE)
    .forEach(function(project) {
        project.javascript.forEach(function(jsFileConf, k) {
            var typescriptFilesToListen = [];
            var javascriptFilesToListen = [];

            var generatedFilesToListen = [];

            var outHash = jsFileConf.outputFile.replace(/[^\d\w]/gmi, '').toLowerCase();

            jsFileConf.folders.forEach(function(folderPath) {
                typescriptFilesToListen.push(folderPath + '/*.' + TS_EXTENSION);
                javascriptFilesToListen.push(folderPath + '/*.' + JS_EXTENSION);
            });

            // TS pre generation
            (function() {
                var tsOutHash = outHash + '.' + TS_EXTENSION;
                var finalFile = TaskRegister.GENERATED_FILES_FOLDER + tsOutHash + '.js';
                var tsBaseTaskName = project.projectName + k + TS_EXTENSION;

                // Listens for any files in their original folders, calls the concat ts task
                watchConf[tsBaseTaskName] = {
                    files: typescriptFilesToListen,
                    tasks: [
                        [TaskRegister.TS_CONCAT_TASK_NAME, project.projectName, k].join(':')
                    ]
                };

                runOnly.push([ TaskRegister.TS_CONCAT_TASK_NAME, project.projectName, k ]);

                // Concats the changed files into the generated folder
                concatTsConf[project.projectName + k] = {
                    files: jsFileConf.folders,
                    outputFile: tsOutHash
                };

                // Listens for ts files changes inside the generated folder
                watchConf[tsBaseTaskName + 'postConcat'] = {
                    files: [TaskRegister.GENERATED_FILES_FOLDER + tsOutHash],
                    tasks: ['ts:' + tsBaseTaskName]
                };

                runOnly.push([ 'ts', tsBaseTaskName ]);

                // Compiles ts files inside the generated folder into regular js files
                typescriptConf[tsBaseTaskName] = {
                    files: [{
                        src: TaskRegister.GENERATED_FILES_FOLDER + tsOutHash,
                        dest: finalFile
                    }],
                    options: {
                        comments: true,
                        fast: 'never',
                        sourceMap: false
                    }
                };

                generatedFilesToListen.push(finalFile);
            })();

            // JS pre generation
            (function() {
                var jsOutHash = outHash + '.' + JS_EXTENSION;
                var jsBaseTaskName = project.projectName + k + JS_EXTENSION;
                var jshintFilesToIgnore = [];

                jsFileConf.folders.forEach(function(folderPath) {
                    jshintFilesToIgnore.push('!' + folderPath + '/*.lib.' + JS_EXTENSION);
                    jshintFilesToIgnore.push('!' + folderPath + '/*.ignore.' + JS_EXTENSION);
                });

                // Watches any js files for changes on their original preset folders
                watchConf[jsBaseTaskName] = {
                    files: javascriptFilesToListen,
                    tasks: ['jshint:' + jsBaseTaskName, [TaskRegister.JS_CONCAT_TASK_NAME, project.projectName, k].join(':')]
                };

                runOnly.push([ 'jshint', jsBaseTaskName ]);
                runOnly.push([ TaskRegister.JS_CONCAT_TASK_NAME, project.projectName, k ]);

                jshintConf[jsBaseTaskName] = {
                    files: {
                        src: javascriptFilesToListen.concat(jshintFilesToIgnore)
                    }
                };

                concatJsConf[project.projectName + k] = {
                    files: jsFileConf.folders,
                    outputFile: jsOutHash
                };

                generatedFilesToListen.push(TaskRegister.GENERATED_FILES_FOLDER + jsOutHash);
            })();

            // Final file generation
            (function() {
                var finalTaskName = project.projectName + k;

                watchConf[finalTaskName] = {
                    files: generatedFilesToListen,
                    tasks: ['uglify:' + finalTaskName]
                };

                runOnly.push([ 'uglify', finalTaskName ]);

                uglifyConf[finalTaskName] = {
                    src: generatedFilesToListen.reverse(),
                    dest: jsFileConf.outputFile
                };
            })();
        });

        project.css.forEach(function(cssFileConf, k) {
            var cssFilesToListen = [];

            cssFileConf.folders.forEach(function(folderPath) {
                cssFilesToListen.push(folderPath + '/*.' + CSS_EXTENSION);
            });

            var cssBaseTaskName = project.projectName + k + CSS_EXTENSION;

            watchConf[cssBaseTaskName] = {
                files: cssFilesToListen,
                tasks: [
                    'cssmin:' + cssBaseTaskName
                ]
            };

            cssMinConf[ cssBaseTaskName ] = {
                files: {}
            };

            cssMinConf[ cssBaseTaskName ].files[ cssFileConf.outputFile ] = cssFilesToListen;

            runOnly.push([ 'cssmin', cssBaseTaskName ]);
        });
    });

    var conf = {
        watch: watchConf,
        concatTS: concatTsConf,
        concatJS: concatJsConf,
        ts: typescriptConf,
        jshint: jshintConf,
        uglify: uglifyConf,
        cssmin: cssMinConf
    };

    grunt.initConfig(conf);

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks("grunt-ts");
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    grunt.registerTask('run', function() {
        runOnly.forEach(function(taskConf){
            grunt.task.run(taskConf.join(':')); 
        });
    });
};
