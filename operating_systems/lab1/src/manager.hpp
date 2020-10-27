#ifndef LAB_MANAGER_HPP
#define LAB_MANAGER_HPP

#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN
#endif

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#include <string>

namespace spos::lab1 {

    class Manager {
    public:
        enum RunExitCode {
            SUCCESS, WSA_STARTUP_FAILED, SOCKET_CONNECTION_ERROR
        };

        Manager(std::string op_name, int x_arg);

        RunExitCode run();

    private:
        static std::pair<SOCKET, std::string> _connectSocket();

        static PROCESS_INFORMATION _runWorker(const std::string& command_line);

        //TODO: short circuit check : check if value is null-value for op
        //TODO: evaluate : takes to arguments, returns result of op
        //TODO: async wait for result : checks sockets for results, if one is ready do check of short-circuit
        //TODO: exit process

        int _x_arg;
        std::string _op_name;
        PROCESS_INFORMATION _f_process_info;
        PROCESS_INFORMATION _g_process_info;
        SOCKET _f_listen_socket;
        SOCKET _g_listen_socket;
        std::string _f_port;
        std::string _g_port;
    };

} //namespace spos::lab1

#endif //LAB_MANAGER_HPP
