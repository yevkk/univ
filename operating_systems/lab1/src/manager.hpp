#ifndef LAB_MANAGER_HPP
#define LAB_MANAGER_HPP

#ifndef WIN32_LEAN_AND_MEAN
#define WIN32_LEAN_AND_MEAN
#endif

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#include <vector>
#include <string>
#include <optional>
#include <memory>

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

        auto _shortCircuitCheck(const std::string& value_str) -> bool;

        void _shortCircuitEvaluate();

        void _resultEvaluate();

        void _terminateUnfinished();


        //TODO: short circuit check : check if value is null-value for op
        //TODO: evaluate : takes to arguments, returns result of op

        int _x_arg;
        std::string _op_name;
        std::vector<std::optional<PROCESS_INFORMATION>> _process_info;
        std::vector<SOCKET> _listen_sockets;
        std::vector<OptionalString> _sub_results;

        std::unique_ptr<bool> bool_result_ptr;
    };

} //namespace spos::lab1

#endif //LAB_MANAGER_HPP
