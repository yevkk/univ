#include "manager.hpp"

#include <vector>
#include <string>
#include <algorithm>
#include <future>
#include <chrono>
#include <iostream>

using namespace std::chrono;

namespace spos::lab1 {
    const auto PROMPT_PERIOD = 10s;

    Manager::Manager(std::string op_name, int x_arg) :
            _x_arg{x_arg}, _op_name{std::move(op_name)} {}

    std::pair<SOCKET, std::string> Manager::_connectSocket() {
        static int port = 27015;
        std::string port_str = std::to_string(port++);

        addrinfo *ai_ptr, hints;

        ZeroMemory(&hints, sizeof(hints));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_protocol = IPPROTO_TCP;
        hints.ai_flags = AI_PASSIVE;

        if (getaddrinfo(nullptr, port_str.c_str(), &hints, &ai_ptr)) {
            return {INVALID_SOCKET, ""};
        }

        SOCKET listen_socket = socket(ai_ptr->ai_family, ai_ptr->ai_socktype, ai_ptr->ai_protocol);
        if (listen_socket == INVALID_SOCKET) {
            freeaddrinfo(ai_ptr);
            return {INVALID_SOCKET, ""};
        }

        if (bind(listen_socket, ai_ptr->ai_addr, (int) ai_ptr->ai_addrlen) == SOCKET_ERROR) {
            freeaddrinfo(ai_ptr);
            closesocket(listen_socket);
            return {INVALID_SOCKET, ""};
        }

        freeaddrinfo(ai_ptr);

        return {listen_socket, port_str};
    }

    std::optional<PROCESS_INFORMATION> Manager::_runWorker(const std::string &command_line) {
        STARTUPINFO startup_info;
        PROCESS_INFORMATION process_info;

        ZeroMemory(&startup_info, sizeof(startup_info));
        startup_info.cb = sizeof(startup_info);
        ZeroMemory(&process_info, sizeof(process_info));

        if (!CreateProcess("worker.exe", (char *) command_line.c_str(),
                           nullptr, nullptr, false, 0, nullptr, nullptr,
                           &startup_info, &process_info)
                ) {
            return std::nullopt;
        }

        return process_info;
    }

    std::optional<std::string> Manager::_getFunctionResult(SOCKET listen_socket) {
        if (listen(listen_socket, SOMAXCONN) == SOCKET_ERROR) {
            closesocket(listen_socket);
            return std::nullopt;
        }

        SOCKET client_socket = accept(listen_socket, nullptr, nullptr);
        if (client_socket == INVALID_SOCKET) {
            closesocket(listen_socket);
            return std::nullopt;
        }

        char recv_buf[16];
        if (recv(client_socket, recv_buf, sizeof(recv_buf), 0) < 0) {
            closesocket(client_socket);
            return std::nullopt;
        }

        if (shutdown(client_socket, SD_SEND) == SOCKET_ERROR) {
            closesocket(client_socket);
            return std::nullopt;
        }

        closesocket(listen_socket);
        return recv_buf;
    }

    void Manager::_terminateUnfinished() {
        for (std::size_t i = 0; i < _sub_results.size(); i++) {
            if (_sub_results[i] != std::nullopt) {
                continue;
            }

            int exit_code;
            TerminateProcess(
                    OpenProcess(PROCESS_ALL_ACCESS, FALSE, _process_info[i].value().dwProcessId),
                    exit_code
            );
            CloseHandle(_process_info[i].value().hProcess);
            CloseHandle(_process_info[i].value().hThread);

            send(_listen_sockets[i], " ", (int) strlen(" "), 0);
        }
    }

    bool Manager::_shortCircuitCheck(const std::string &value_str) {
        if ((_op_name == "AND" && std::stoi(value_str) == 0) ||
            (_op_name == "OR" && std::stoi(value_str) == 1) ||
            (_op_name == "MIN" && std::stoi(value_str) == 0)) {
            return true;
        }
        return false;
    }

    void Manager::_shortCircuitEvaluate() {
        if (_op_name == "AND") {
            bool_result_ptr = std::make_unique<bool>(false);
        } else if (_op_name == "OR") {
            bool_result_ptr = std::make_unique<bool>(true);
        } else if (_op_name == "MIN") {
            int_result_ptr = std::make_unique<int>(0);
        }
    }

    void Manager::_resultEvaluate() {
        if (_op_name == "AND") {
            bool result = true;
            for (const auto &item : _sub_results) {
                result = result && std::stoi(item.value());
            }
            bool_result_ptr = std::make_unique<bool>(result);
        } else if (_op_name == "OR") {
            bool result = false;
            for (const auto &item : _sub_results) {
                result = result || std::stoi(item.value());
            }
            bool_result_ptr = std::make_unique<bool>(result);
        } else if (_op_name == "MIN") {
            int result = INT_MAX;
            for (const auto &item : _sub_results) {
                int int_item = std::stoi(item.value());
                if (int_item < result) {
                    result = int_item;
                }
            }
            int_result_ptr = std::make_unique<int>(result);
        }
    }

    Manager::RunExitCode Manager::_run() {
        std::atomic<bool> done = false;
        auto start_ts = system_clock::now();

        auto keyboard_listener = std::async(std::launch::async,
                                            [&done]() {
                                                while (true) {
                                                    std::this_thread::sleep_for(50ms);
                                                    if (done || (GetKeyState(VK_ESCAPE) & 0x8000)) {
                                                        break;
                                                    }
                                                }
                                            }
        );

        WSADATA wsa_data;
        if (WSAStartup(MAKEWORD(2, 2), &wsa_data)) {
            return WSA_STARTUP_FAILED;
        }

        auto[f_socket, f_port] = _connectSocket();
        auto[g_socket, g_port] = _connectSocket();

        _listen_sockets = {
                f_socket,
                g_socket
        };

        if (std::find_if(_listen_sockets.cbegin(),
                         _listen_sockets.cend(),
                         [](auto &sock) { return sock == INVALID_SOCKET; })
            != _listen_sockets.cend()) {
            WSACleanup();
            return SOCKET_CONNECTION_ERROR;
        }

        std::size_t counter = 0;
        auto f_future = std::async(std::launch::async, _getFunctionResult, _listen_sockets[counter++]);
        auto g_future = std::async(std::launch::async, _getFunctionResult, _listen_sockets[counter++]);

        counter = 0;
        std::vector<std::pair<std::future<OptionalString>, std::size_t>> func_futures;
        func_futures.emplace_back(std::move(f_future), counter++);
        func_futures.emplace_back(std::move(g_future), counter++);

        _process_info = {
                _runWorker(" " + _op_name + " f " + std::to_string(_x_arg) + " 127.0.0.1 " + f_port),
                _runWorker(" " + _op_name + " g " + std::to_string(_x_arg) + " 127.0.0.1 " + g_port)
        };

        if (std::find_if(_process_info.cbegin(),
                         _process_info.cend(),
                         [](auto &pi) { return !pi.has_value(); })
            != _process_info.cend()) {
            WSACleanup();
            return PROCESS_CREATION_FAILED;
        }

        _sub_results = decltype(_sub_results)(func_futures.size(), std::nullopt);
        bool prompt_enabled = true;
        auto next_prompt_ts = system_clock::now() + PROMPT_PERIOD;

        while (!func_futures.empty()) {
            if (keyboard_listener.wait_for(0s) == std::future_status::ready) {
                _terminateUnfinished();
                _exitRun(start_ts);
                return TERMINATED;
            }

            if (system_clock::now() > next_prompt_ts && prompt_enabled) {
                std::cout << "[TIME] " << duration_cast<seconds>(system_clock::now() - start_ts).count() << "s\n";
                std::cout << "Choose Option:\n \ta) continue\n \tb) continue without prompt\n \tc) stop\n";
                char input = ' ';
                while (input != 'a' && input != 'b' && input != 'c') {
                    std::cin >> input;
                    if (input == 'a') {
                        std::cout << "[INFO] Continued\n";
                    } else if (input == 'b') {
                        std::cout << "[INFO] Prompt disabled\n";
                        prompt_enabled = false;
                    } else if (input == 'c') {
                        done = true;
                    }
                }
                next_prompt_ts = system_clock::now() + PROMPT_PERIOD;
            }

            const auto ready_future_it = std::find_if(
                    func_futures.begin(),
                    func_futures.end(),
                    [](auto &fut) { return fut.first.wait_for(0s) == std::future_status::ready; });


            if (ready_future_it != func_futures.end()) {
                std::string result_str = ready_future_it->first.get().value();

                if (_shortCircuitCheck(result_str)) {
                    done = true;
                    _terminateUnfinished();
                    _shortCircuitEvaluate();
                    _exitRun(start_ts);
                    return SUCCESS_SC;
                }

                _sub_results[ready_future_it->second] = result_str;
                func_futures.erase(ready_future_it);
                CloseHandle(_process_info[ready_future_it->second].value().hProcess);
                CloseHandle(_process_info[ready_future_it->second].value().hThread);
            }
        }
        done = true;
        _resultEvaluate();

        _exitRun(start_ts);
        return SUCCESS;
    }

    void Manager::_exitRun(decltype(system_clock::now()) start_ts) {
        std::cout << "[TIME] " << duration_cast<seconds>(system_clock::now() - start_ts).count() << "s\n";
        WSACleanup();
        _process_info.clear();
        _listen_sockets.clear();
    }

    void Manager::run() {
        std::cout << "[INFO] argument: " << _x_arg << ", operation: " << _op_name << "\n";

        auto exit_code = _run();

        switch (exit_code) {
            case SUCCESS:
                std::cout << "[RESULT] ";
                _printResult(std::cout);
                std::cout << "\n";
                break;
            case SUCCESS_SC:
                std::cout << "[INFO] Short-circuit case\n";
                std::cout << "[RESULT] ";
                _printResult(std::cout);
                std::cout << "\n";
                break;
            case WSA_STARTUP_FAILED:
                std::cout << "[INFO] Unsuccessful start of using Winsock DLL\n";
                break;
            case SOCKET_CONNECTION_ERROR:
                std::cout << "[INFO] Sockets connection failed\n";
                break;
            case PROCESS_CREATION_FAILED:
                std::cout << "[INFO] Process creation failed\n";
                break;
            case TERMINATED:
                std::cout << "[INFO] Calculation terminated\n";
                break;
        }
    }
}
