# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/Cellar/cmake/2.8.11.2/bin/cmake

# The command to remove a file.
RM = /usr/local/Cellar/cmake/2.8.11.2/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The program to use to edit the cache.
CMAKE_EDIT_COMMAND = /usr/local/Cellar/cmake/2.8.11.2/bin/ccmake

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c

# Include any dependencies generated for this target.
include examples/CMakeFiles/amqp_rpc_sendstring_client.dir/depend.make

# Include the progress variables for this target.
include examples/CMakeFiles/amqp_rpc_sendstring_client.dir/progress.make

# Include the compile flags for this target's objects.
include examples/CMakeFiles/amqp_rpc_sendstring_client.dir/flags.make

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/flags.make
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o: examples/amqp_rpc_sendstring_client.c
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o   -c /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/amqp_rpc_sendstring_client.c

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.i"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/amqp_rpc_sendstring_client.c > CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.i

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.s"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/amqp_rpc_sendstring_client.c -o CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.s

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.requires:
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.requires

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.provides: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.requires
	$(MAKE) -f examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build.make examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.provides.build
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.provides

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.provides.build: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/flags.make
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o: examples/utils.c
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o   -c /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/utils.c

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.i"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/utils.c > CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.i

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.s"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/utils.c -o CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.s

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.requires:
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.requires

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.provides: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.requires
	$(MAKE) -f examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build.make examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.provides.build
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.provides

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.provides.build: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/flags.make
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o: examples/unix/platform_utils.c
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/CMakeFiles $(CMAKE_PROGRESS_3)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o   -c /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/unix/platform_utils.c

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.i"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/unix/platform_utils.c > CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.i

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.s"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/unix/platform_utils.c -o CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.s

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.requires:
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.requires

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.provides: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.requires
	$(MAKE) -f examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build.make examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.provides.build
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.provides

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.provides.build: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o

# Object files for target amqp_rpc_sendstring_client
amqp_rpc_sendstring_client_OBJECTS = \
"CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o" \
"CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o" \
"CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o"

# External object files for target amqp_rpc_sendstring_client
amqp_rpc_sendstring_client_EXTERNAL_OBJECTS =

examples/amqp_rpc_sendstring_client: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o
examples/amqp_rpc_sendstring_client: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o
examples/amqp_rpc_sendstring_client: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o
examples/amqp_rpc_sendstring_client: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build.make
examples/amqp_rpc_sendstring_client: librabbitmq/librabbitmq.1.0.1.dylib
examples/amqp_rpc_sendstring_client: /usr/lib/libssl.dylib
examples/amqp_rpc_sendstring_client: /usr/lib/libcrypto.dylib
examples/amqp_rpc_sendstring_client: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking C executable amqp_rpc_sendstring_client"
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/amqp_rpc_sendstring_client.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build: examples/amqp_rpc_sendstring_client
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/build

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/requires: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/amqp_rpc_sendstring_client.c.o.requires
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/requires: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/utils.c.o.requires
examples/CMakeFiles/amqp_rpc_sendstring_client.dir/requires: examples/CMakeFiles/amqp_rpc_sendstring_client.dir/unix/platform_utils.c.o.requires
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/requires

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/clean:
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples && $(CMAKE_COMMAND) -P CMakeFiles/amqp_rpc_sendstring_client.dir/cmake_clean.cmake
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/clean

examples/CMakeFiles/amqp_rpc_sendstring_client.dir/depend:
	cd /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples /Users/janmachacek/Talks/springone2gx2013/native/rabbitmq-c/examples/CMakeFiles/amqp_rpc_sendstring_client.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : examples/CMakeFiles/amqp_rpc_sendstring_client.dir/depend

