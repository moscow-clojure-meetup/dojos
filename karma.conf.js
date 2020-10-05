module.exports = function (config) {
    let root = 'public/js/karma'
    let junitOutputDir = "public/js/karma/junit"

    config.set({
        frameworks: ['cljs-test'],
        browsers: ['ChromeHeadless'],
        basePath: './',
        files: [
            root + '/test.js'
        ],
        plugins: [
            'karma-cljs-test',
            'karma-chrome-launcher',
            'karma-junit-reporter'
        ],
        colors: true,
        logLevel: config.LOG_INFO,
        client: {
            args: ['shadow.test.karma.init'],
            singleRun: true
        },
        // the default configuration
        junitReporter: {
            outputDir: junitOutputDir + '/karma', // results will be saved as outputDir/browserName.xml
            outputFile: undefined, // if included, results will be saved as outputDir/browserName/outputFile
            suite: '' // suite will become the package name attribute in xml testsuite element
        }
    })
}
