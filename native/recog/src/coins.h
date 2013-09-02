#ifndef coins_h
#define coins_h

#include <opencv2/opencv.hpp>
#include <vector>
#include <boost/optional.hpp>

namespace eigengo { namespace sogx {

	struct Point {
		int x;
		int y;
		
		bool operator==(const Point &rhs) const;
	};

	struct Coin {
		Point center;
		int radius;
	};
	
	struct CoinResult {
		std::vector<Coin> coins;
#ifdef WITH_RINGS
		bool hasRing;
#endif
	};
	
	class CoinCounter {
	private:
		CoinResult countGpu(const cv::Mat &image);
		CoinResult countCpu(const cv::Mat &image);
	public:
		CoinResult count(const cv::Mat &image);
	};
			
}
}


#endif