#include "main.h"
#include "im.h"
#include "jzon.h"

using namespace eigengo::sogx;

Main::Main(const std::string queue, const std::string exchange, const std::string routingKey) :
RabbitRpcServer::RabbitRpcServer(queue, exchange, routingKey) {
	
}

std::string Main::handleMessage(const AmqpClient::BasicMessage::ptr_t message, const AmqpClient::Channel::ptr_t channel) {
	Jzon::Object responseJson;
	try {
		ImageMessage imageMessage(message);
		
		Jzon::Array coinsJson;
		//Jzon::Array facesJson;
		auto imageData = imageMessage.headImage();
		auto imageMat = cv::imdecode(cv::Mat(imageData), 1);
		// ponies & unicorns
		auto coins = coinCounter.count(imageMat);
		
		for (auto i = coins.begin(); i != coins.end(); ++i) {
			Jzon::Object coinJson;
			Jzon::Object centerJson;
			centerJson.Add("x", i->center.x);
			centerJson.Add("y", i->center.y);
			coinJson.Add("center", centerJson);
			coinJson.Add("radius", i->radius);
			coinsJson.Add(coinJson);
		}
		//responseJson.Add("faces", facesJson);
		responseJson.Add("coins", coinsJson);
		responseJson.Add("succeeded", true);
	} catch (std::exception &e) {
		// bantha poodoo!
		std::cerr << e.what() << std::endl;
		responseJson.Add("succeeded", false);
	} catch (...) {
		// bantha poodoo!
		responseJson.Add("succeeded", false);
	}

#ifdef DEBUG
	std::cout << message->ReplyTo() << std::endl;
#endif
	Jzon::Writer writer(responseJson, Jzon::NoFormat);
	writer.Write();

	return writer.GetResult();
}

void Main::inThreadInit() {
#ifdef GPU
	using namespace cv::gpu;
	int deviceCount = getCudaEnabledDeviceCount();
	if (deviceCount > 0) {
		setDevice(0);
	}
#endif
}

int main(int argc, char** argv) {
	Main main("sogx.recog.queue", "sogx.exchange", "sogx.recog.key");
	main.runAndJoin(8);
	return 0;
}