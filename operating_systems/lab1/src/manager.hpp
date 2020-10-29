#ifndef LAB_MANAGER_HPP
#define LAB_MANAGER_HPP

#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN
#endif

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#include <string>
#include <optional>

namespace spos::lab1 {

    class Manager {
    public:
        enum RunExitCode {
            SUCCESS, WSA_STARTUP_FAILED, SOCKET_CONNECTION_ERROR, PROCESS_CREATION_FAILED
        };

        Manager(std::string op_name, int x_arg);

        RunExitCode run();

    private:
        using OptionalString = std::optional<std::string>;

        static auto _connectSocket() -> std::pair<SOCKET, std::string>;

        static auto _runWorker(const std::string &command_line) -> std::optional<PROCESS_INFORMATION>;

        static auto _getFunctionResult(SOCKET listen_socket) -> OptionalString;

        //TODO: short circuit check : check if value is null-value for op
        //TODO: evaluate : takes to arguments, returns result of op
        //TODO: exit process

        int _x_arg;
        std::string _op_name;
        std::optional<PROCESS_INFORMATION> _f_process_info;
        std::optional<PROCESS_INFORMATION> _g_process_info;
        SOCKET _f_listen_socket;
        SOCKET _g_listen_socket;
    };

} //namespace spos::lab1

#endif //LAB_MANAGER_HPP
