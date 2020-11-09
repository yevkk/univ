#ifndef LAB_MANAGER_HXX
#define LAB_MANAGER_HXX

#include "manager.hpp"
#include "config.hpp"

#include <vector>
#include <string>
#include <algorithm>
#include <future>
#include <chrono>
#include <numeric>
#include <iostream>

using namespace std::chrono;

namespace spos::lab1 {

    template<CancellationType CT>
    Manager<CT>::Manager(std::string op_name, int x_arg) :
            _x_arg{x_arg}, _op_name{std::move(op_name)}, _ready{false} {}

    template<CancellationType CT>
    SOCKET Manager<CT>::_connectSocket(const std::string &port) {
        addrinfo *ai_ptr, hints;

        ZeroMemory(&hints, sizeof(hints));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_protocol = IPPROTO_TCP;
        hints.ai_flags = AI_PASSIVE;

        if (getaddrinfo(nullptr, port.c_str(), &hints, &ai_ptr)) {
            return INVALID_SOCKET;
        }

        SOCKET listen_socket = socket(ai_ptr->ai_family, ai_ptr->ai_socktype, ai_ptr->ai_protocol);
        if (listen_socket == INVALID_SOCKET) {
            freeaddrinfo(ai_ptr);
            return INVALID_SOCKET;
        }

        if (bind(listen_socket, ai_ptr->ai_addr, (int) ai_ptr->ai_addrlen) == SOCKET_ERROR) {
            freeaddrinfo(ai_ptr);
            closesocket(listen_socket);
            return INVALID_SOCKET;
        }

        freeaddrinfo(ai_ptr);

        return listen_socket;
    }

    template<CancellationType CT>
    std::optional<PROCESS_INFORMATION> Manager<CT>::_runWorker(const std::string &command_line) {
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

    template<CancellationType CT>
    std::optional<std::string> Manager<CT>::_getFunctionResult(SOCKET listen_socket) {
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

    template<CancellationType CT>
    void Manager<CT>::_terminateUnfinished() {
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

    template<CancellationType CT>
    bool Manager<CT>::_shortCircuitCheck(const std::string &value_str) {
        if ((_op_name == "AND" && std::stoi(value_str) == 0) ||
            (_op_name == "OR" && std::stoi(value_str) == 1) ||
            (_op_name == "MIN" && std::stoi(value_str) == 0)) {
            return true;
        }
        return false;
    }

    template<CancellationType CT>
    void Manager<CT>::_shortCircuitEvaluate() {
        if (_op_name == "AND") {
            bool_result_ptr = std::make_unique<bool>(false);
        } else if (_op_name == "OR") {
            bool_result_ptr = std::make_unique<bool>(true);
        } else if (_op_name == "MIN") {
            int_result_ptr = std::make_unique<int>(0);
        }
    }

    template<CancellationType CT>
    void Manager<CT>::_resultEvaluate() {
        if (_op_name == "AND") {
            bool_result_ptr = std::make_unique<bool>(
                    std::accumulate(_sub_results.begin(),
                                    _sub_results.end(),
                                    true,
                                    [](bool res, OptionalString item) { return res && std::stoi(item.value()); }
                    )
            );
        } else if (_op_name == "OR") {
            bool_result_ptr = std::make_unique<bool>(
                    std::accumulate(_sub_results.begin(),
                                    _sub_results.end(),
                                    false,
                                    [](bool res, OptionalString item) { return res || std::stoi(item.value()); }
                    )
            );
        } else if (_op_name == "MIN") {
            int_result_ptr = std::make_unique<int>(
                    std::accumulate(_sub_results.begin(),
                                    _sub_results.end(),
                                    INT_MAX,
                                    [](int res, OptionalString item) { return std::min(res, std::stoi(item.value())); }
                    )
            );
        }
    }

    template<CancellationType CT>
    typename Manager<CT>::RunExitCode Manager<CT>::_run() {
        _ready = false;
        auto start_ts = system_clock::now();

        auto cancellation_listener = std::async(std::launch::async,
                                                [this, start_ts = start_ts]() { return _cancellation(start_ts); }
        );

        WSADATA wsa_data;
        if (WSAStartup(MAKEWORD(2, 2), &wsa_data)) {
            return WSA_STARTUP_FAILED;
        }

        _listen_sockets = {
                _connectSocket(config::port_f),
                _connectSocket(config::port_g)
        };

        if (std::find_if(_listen_sockets.cbegin(),
                         _listen_sockets.cend(),
                         [](auto &sock) { return sock == INVALID_SOCKET; })
            != _listen_sockets.cend()) {
            _exitRun();
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
                _runWorker(" " + _op_name + " f " + std::to_string(_x_arg)),
                _runWorker(" " + _op_name + " g " + std::to_string(_x_arg))
        };

        if (std::find_if(_process_info.cbegin(),
                         _process_info.cend(),
                         [](auto &pi) { return !pi.has_value(); })
            != _process_info.cend()) {
            _exitRun();
            return PROCESS_CREATION_FAILED;
        }

        _sub_results = decltype(_sub_results)(func_futures.size(), std::nullopt);

        while (!func_futures.empty()) {
            if (cancellation_listener.wait_for(0s) == std::future_status::ready && cancellation_listener.get()) {
                break;
            }

            const auto ready_future_it = std::find_if(
                    func_futures.begin(),
                    func_futures.end(),
                    [](auto &fut) { return fut.first.wait_for(0s) == std::future_status::ready; });


            if (ready_future_it != func_futures.end()) {
                std::string result_str = ready_future_it->first.get().value();

                if (_shortCircuitCheck(result_str)) {
                    _ready = true;
                    _terminateUnfinished();
                    _shortCircuitEvaluate();
                    _exitRun();
                    return SUCCESS;
                }

                _sub_results[ready_future_it->second] = result_str;
                func_futures.erase(ready_future_it);
                CloseHandle(_process_info[ready_future_it->second].value().hProcess);
                CloseHandle(_process_info[ready_future_it->second].value().hThread);
            }
        }

        if (!func_futures.empty()) {
            _terminateUnfinished();
            _exitRun();

            if (!_sub_results[0].has_value()) {
                if (!_sub_results[1].has_value()) {
                    return TERMINATED;
                } else {
                    return TERMINATED_F_HANGS;
                }
            } else {
                if (!_sub_results[1].has_value()) {
                    return TERMINATED_G_HANGS;
                }
            }
        }

        _ready = true;
        _resultEvaluate();

        _exitRun();
        return SUCCESS;
    }

    template<CancellationType CT>
    bool Manager<CT>::_cancellation(decltype(system_clock::now()) start_ts) { return true; }

    template<>
    bool Manager<CancellationType::KEYBOARD>::_cancellation(decltype(system_clock::now()) start_ts) {
        while (true) {
            std::this_thread::sleep_for(100ms);
            if (_ready || (GetKeyState(VK_ESCAPE) & 0x8000)) {
                break;
            }
        }

        return true;
    }

    template<>
    bool Manager<CancellationType::PROMPT>::_cancellation(decltype(system_clock::now()) start_ts) {
        const auto PROMPT_PERIOD = 5s;

        auto next_prompt_ts = system_clock::now() + PROMPT_PERIOD;
        while (true) {
            std::this_thread::sleep_for(1000ms);
            if (_ready) {
                return true;
            }
            if (system_clock::now() > next_prompt_ts) {
                std::cout << "[TIME] " << duration_cast<seconds>(system_clock::now() - start_ts).count() << "s\n";
                std::cout << "Choose Option:\n \ta) continue\n \tb) continue without prompt\n \tc) stop\n";
                char input = ' ';
                while (input != 'a' && input != 'b' && input != 'c') {
                    std::cin >> input;
                    if (input == 'a') {
                        std::cout << "[INFO] Continued\n";
                        next_prompt_ts = system_clock::now() + PROMPT_PERIOD;
                    } else if (input == 'b') {
                        std::cout << "[INFO] Prompt disabled\n";
                        return false;
                    } else if (input == 'c') {
                        return true;
                    }
                }
            }
        }
    }

    template<CancellationType CT>
    void Manager<CT>::_exitRun() {
        WSACleanup();
        _process_info.clear();
        _listen_sockets.clear();
    }

    template<CancellationType CT>
    void Manager<CT>::run() {
        std::cout << "[INFO] argument: " << _x_arg << ", operation: " << _op_name << "\n";

        auto exit_code = _run();

        switch (exit_code) {
            case SUCCESS:
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
            case TERMINATED_F_HANGS:
                std::cout << "[INFO] Calculation terminated (f hangs)\n";
                break;
            case TERMINATED_G_HANGS:
                std::cout << "[INFO] Calculation terminated (g hangs)\n";
                break;
        }
    }
}

#endif //LAB_MANAGER_HXX