#Building
To build the application you will need the following tidbits:

* cmake    (OS X: ``brew install cmake``)
* Boost    (OS X: ``brew install boost``)
* RabbitMQ (OS X: ``brew install rabbitmq``)
* RabbitMQ-C library
* RabbitMQ-CPP library
* OpenCV

###rabbitmq-c
To build, do the ``cmake`` dance: in some sub-directory of ``rabbitmq-c``, run ``cmake ..``, followed by ``cmake --build .`` and ``sudo make install``.

###rabbitmq-cpp
To build, do the ``cmake`` dance: in some sub-directory of ``rabbitmq-cpp``, run ``cmake ..``, followed by ``cmake --build .`` and ``sudo make install``.

Remember that the standard C++ library must match in all the C++ projects. I am using C++11, with ``set(CMAKE_CXX_FLAGS "-std=c++11 -stdlib=libc++")`` in the ``CMakeLists.txt`` files.

###OpenCV
As of 8th August, to build OpenCV, clone the repository from [https://github.com/Itseez/opencv](https://github.com/Itseez/opencv), then apply the changes in [PR 1244](https://github.com/Itseez/opencv/pull/1244). 

You can do so by applying this patch:

```patch
diff --git a/modules/stitching/src/motion_estimators.cpp b/modules/stitching/src/motion_estimators.cpp
index abd43b1..b09253c 100644
--- a/modules/stitching/src/motion_estimators.cpp
+++ b/modules/stitching/src/motion_estimators.cpp
@@ -259,7 +259,7 @@ bool BundleAdjusterBase::estimate(const std::vector<ImageFeatures> &features,
     bool ok = true;
     for (int i = 0; i < cam_params_.rows; ++i)
     {
-        if (isnan(cam_params_.at<double>(i,0)))
+        if (std::isnan(cam_params_.at<double>(i,0)))
         {
             ok = false;
             break;
diff --git a/modules/ts/include/opencv2/ts/ts_perf.hpp b/modules/ts/include/opencv2/ts/ts_perf.hpp
index 29e440d..a6deac5 100644
--- a/modules/ts/include/opencv2/ts/ts_perf.hpp
+++ b/modules/ts/include/opencv2/ts/ts_perf.hpp
@@ -476,10 +476,17 @@ CV_EXPORTS void PrintTo(const Size& sz, ::std::ostream* os);
     TEST_P(fixture##_##name, name /*perf*/){ RunPerfTestBody(); }\
     INSTANTIATE_TEST_CASE_P(/*none*/, fixture##_##name, params);\
     void fixture##_##name::PerfTestBody()
+    
+#if defined(_MSC_VER) && (_MSC_VER <= 1400)
+#define CV_PERF_TEST_MAIN_INTERNALS_ARGS(...)  \
+    while (++argc >= (--argc,-1)) {__VA_ARGS__; break;} /*this ugly construction is needed for VS 2005*/
+#else
+#define CV_PERF_TEST_MAIN_INTERNALS_ARGS(...)  \
+    __VA_ARGS__;
+#endif
 
-
-#define CV_PERF_TEST_MAIN_INTERNALS(modulename, impls, ...) \
-    while (++argc >= (--argc,-1)) {__VA_ARGS__; break;} /*this ugly construction is needed for VS 2005*/\
+#define CV_PERF_TEST_MAIN_INTERNALS(modulename, impls, ...)  \
+    CV_PERF_TEST_MAIN_INTERNALS_ARGS(__VA_ARGS__) \
     ::perf::Regression::Init(#modulename);\
     ::perf::TestBase::Init(std::vector<std::string>(impls, impls + sizeof impls / sizeof *impls),\
                            argc, argv);\
```

Then follow the usual cmake dance: create sub-directory, say ``build`` in the root of the project, change into it and run ``cmake ..; cmake --build .; sudo make install``. 
