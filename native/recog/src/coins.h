#ifndef coins_h
#define coins_h

#include <opencv2/opencv.hpp>
#include <vector>

namespace eigengo { namespace akka {

	struct Point {
		int x;
		int y;
	};

	struct Coin {
		Point center;
		int radius;
	};
	
	class CoinCounter {
	private:
		std::vector<Coin> countGpu(const cv::Mat &image);
		std::vector<Coin> countCpu(const cv::Mat &image);
	public:
		std::vector<Coin> count(const cv::Mat &image);
	};
		
}
}


#endif