
OBJS = AMQP_BIND.OBJ, AMQP_CONSUMER.OBJ, AMQP_EXCHANGE_DECLARE.OBJ, AMQP_LISTEN.OBJ,-
 AMQP_LISTENQ.OBJ, AMQP_PRODUCER.OBJ, AMQP_SENDSTRING.OBJ, AMQP_UNBIND.OBJ, UTILS.OBJ,-
 PLATFORM_UTILS.OBJ

COM_OBJS = UTILS.OBJ, PLATFORM_UTILS.OBJ

REAL_TARGETS = AMQP_BIND.EXE AMQP_CONSUMER.EXE AMQP_EXCHANGE_DECLARE.EXE AMQP_LISTEN.EXE AMQP_LISTENQ.EXE AMQP_PRODUCER.EXE AMQP_SENDSTRING.EXE AMQP_UNBIND.EXE

.INCLUDE [-.vms]INCLUDE.MMS

CFLAGS = $(OPTFLAGS)/DEFINE=($(DEFS))/INCLUDE=($(INC),"../vms","../librabbitmq")
LDFLAGS = /TRACE

AMQP_BIND.EXE : AMQP_BIND.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_CONSUMER.EXE : AMQP_CONSUMER.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_EXCHANGE_DECLARE.EXE : AMQP_EXCHANGE_DECLARE.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_LISTEN.EXE : AMQP_LISTEN.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_LISTENQ.EXE : AMQP_LISTENQ.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_PRODUCER.EXE : AMQP_PRODUCER.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_SENDSTRING.EXE : AMQP_SENDSTRING.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

AMQP_UNBIND.EXE : AMQP_UNBIND.OBJ, $(COM_OBJS)
	LINK$(LDFLAGS)/EXE=$(MMS$TARGET) $(MMS$SOURCE_LIST),[-.librabbitmq]rabbitmq.olb/lib

	
PLATFORM_UTILS.OBJ : [.UNIX]PLATFORM_UTILS.C
PLATFORM_UTILS.MMSD : [.UNIX]PLATFORM_UTILS.C
