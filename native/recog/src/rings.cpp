#include "rings.h"

using namespace eigengo::sogx;

bool RingDetector::detectCpu(const cv::Mat &image) {
	using namespace cv;
	
	Mat dst;
	std::vector<Vec3f> circles;

	cvtColor(image, dst, COLOR_RGB2GRAY);
	GaussianBlur(dst, dst, Size(9, 9), 2, 2);
	threshold(dst, dst, 150, 255, THRESH_BINARY);
	GaussianBlur(dst, dst, Size(3, 3), 3, 3);
	HoughCircles(dst, circles, HOUGH_GRADIENT,
				 1,    // dp
				 60,   // min dist
				 200,  // canny1
				 20,   // canny2
				 30,   // min radius
				 100   // max radius
				 );

	int ringCount = 0;
	for (size_t i = 0; i < circles.size(); i++ ) {
		Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
		if (dst.at<uchar>(center) > 150) ringCount++;
	}
	
	
#ifdef TEST
	Mat x(image);
	for (size_t i = 0; i < circles.size(); i++ ) {
		Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
		int radius = cvRound(circles[i][2]);
		// draw the circle center
		circle(x, center, 3, Scalar(0,255,0), -1, 8, 0 );
		// draw the circle outline
		circle(x, center, radius, Scalar(0,0,255), 3, 8, 0 );
	}
	cv::imwrite("/Users/janmachacek/x1.png", x);
	cv::imwrite("/Users/janmachacek/x2.png", dst);
#endif
	
	return ringCount;
}

#ifdef GPU
bool RingDetector::detectGpu(const cv::Mat &cpuImage) {
	cv::gpu::GpuMat image(cpuImage);
	cv::gpu::GpuMat dst;
	cv::gpu::GpuMat circlesMat;
	
	cv::gpu::cvtColor(image, dst, CV_BGR2GRAY);
	cv::gpu::GaussianBlur(dst, dst, cv::Size(3, 3), 2, 2);
	cv::gpu::Canny(dst, dst, 1000, 1700, 5);
	cv::gpu::GaussianBlur(dst, dst, cv::Size(9, 9), 3, 3);
	cv::gpu::HoughCircles(dst, circlesMat, CV_HOUGH_GRADIENT,
				 1,    // dp
				 40,   // min dist
				 100,  // canny1
				 105,  // canny2
				 10,   // min radius
				 200   // max radius
				 );
	
	
	return circlesMat.size() == 1;
}
#endif

bool RingDetector::detect(const cv::Mat &image) {
#ifdef GPU
	if (cv::gpu::getCudaEnabledDeviceCount() > 0) return detectGpu(image);
#endif
	return detectCpu(image);
}
