
#Building
To build the application you will need the following tidbits:

* cmake    (OS X: ``brew install cmake``)
* Boost    (OS X: ``brew install boost``)
* RabbitMQ (OS X: ``brew install rabbitmq``)
* OpenCV

##OpenCV
As of 8th August, to build OpenCV, clone the repository from [https://github.com/Itseez/opencv](https://github.com/Itseez/opencv), then apply the changes in [PR 1244](https://github.com/Itseez/opencv/pull/1244). 

Then follow the usual cmake dance: create sub-directory, say ``build`` in the root of the project, change into it and run ``cmake ..; cmake --build .; sudo make install``. 
