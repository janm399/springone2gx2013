#ifndef rings_h
#define rings_h

#include <opencv2/opencv.hpp>
#include <vector>
#include <boost/optional.hpp>

namespace eigengo { namespace sogx {
	
	class RingDetector {
	private:
		bool detectGpu(const cv::Mat &image);
		bool detectCpu(const cv::Mat &image);
	public:
		bool detect(const cv::Mat &image);
	};
			
}
}


#endif