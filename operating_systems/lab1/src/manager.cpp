#include "manager.hpp"

#include <string>
#include <future>
//#include <iostream>

namespace spos::lab1 {
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

        return recv_buf;
    }

    Manager::RunExitCode Manager::run() {
        WSADATA wsa_data;
        if (WSAStartup(MAKEWORD(2, 2), &wsa_data)) {
            return WSA_STARTUP_FAILED;
        }

        auto[f_socket, f_port] = _connectSocket();
        auto[g_socket, g_port] = _connectSocket();
        _f_listen_socket = f_socket;
        _g_listen_socket = g_socket;

        if (_f_listen_socket == INVALID_SOCKET || _g_listen_socket == INVALID_SOCKET) {
            WSACleanup();
            return SOCKET_CONNECTION_ERROR;
        }

        auto f_future = std::async(std::launch::async, _getFunctionResult, _f_listen_socket);
        auto g_future = std::async(std::launch::async, _getFunctionResult, _g_listen_socket);

        _f_process_info = _runWorker(" " + _op_name + " f " + std::to_string(_x_arg) + " 127.0.0.1 " + f_port);
        _g_process_info = _runWorker(" " + _op_name + " g " + std::to_string(_x_arg) + " 127.0.0.1 " + g_port);

        if (!_f_process_info.has_value() || !_g_process_info.has_value()) {
            WSACleanup();
            return PROCESS_CREATION_FAILED;
        }

//        while (!f_future.valid() || !g_future.valid()) {
//        }


//        std::cout << "\n f: " << std::stoi(f_future.get().value()) << std::endl;
//        std::cout << "\n g: " << std::stoi(g_future.get().value()) << std::endl;


        CloseHandle(_f_process_info.value().hProcess);
        CloseHandle(_f_process_info.value().hThread);
        CloseHandle(_g_process_info.value().hProcess);
        CloseHandle(_g_process_info.value().hThread);

        return SUCCESS;
    }
}
