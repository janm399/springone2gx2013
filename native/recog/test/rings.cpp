#include "rtest.h"
#include "rings.h"

using namespace eigengo::sogx;

class RingDetectorTest : public OpenCVTest {
protected:
	RingDetector detector;
};

TEST_F(RingDetectorTest, TwoCoins) {
	auto image = load("coins2.png");
	EXPECT_FALSE(detector.detect(image));
}

TEST_F(RingDetectorTest, ThreeCoins) {
	auto image = load("coins3.png");
	EXPECT_FALSE(detector.detect(image));
}

TEST_F(RingDetectorTest, FourCoins) {
	auto image = load("coins4.png");
	EXPECT_FALSE(detector.detect(image));
}

TEST_F(RingDetectorTest, DamagedFrameWith2Coins) {
	auto image = load("coins2_f1.png");
	EXPECT_FALSE(detector.detect(image));
}

TEST_F(RingDetectorTest, NoCoins) {
	auto image = load("xb.jpg");
	EXPECT_FALSE(detector.detect(image));
}

TEST_F(RingDetectorTest, TheOneRing) {
	auto image = load("onering.png");
	EXPECT_TRUE(detector.detect(image));
}

TEST_F(RingDetectorTest, TheRealOneRing) {
	auto image = load("onering2.png");
	EXPECT_TRUE(detector.detect(image));
}
