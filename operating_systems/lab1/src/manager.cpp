#include "manager.hpp"

#include <string>

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
            WSACleanup();
            return {INVALID_SOCKET, ""};
        }

        SOCKET listen_socket = socket(ai_ptr->ai_family, ai_ptr->ai_socktype, ai_ptr->ai_protocol);
        if (listen_socket == INVALID_SOCKET) {
            freeaddrinfo(ai_ptr);
            WSACleanup();
            return {INVALID_SOCKET, ""};
        }

        if (bind(listen_socket, ai_ptr->ai_addr, (int) ai_ptr->ai_addrlen) == SOCKET_ERROR) {
            freeaddrinfo(ai_ptr);
            closesocket(listen_socket);
            WSACleanup();
            return {INVALID_SOCKET, ""};
        }

        freeaddrinfo(ai_ptr);

        return {listen_socket, port_str};
    }

    Manager::RunExitCode Manager::run() {
        WSADATA wsa_data;
        if (WSAStartup(MAKEWORD(2, 2), &wsa_data)) {
            return WSA_STARTUP_FAILED;
        }

        auto [f_socket, f_port] = _connectSocket();
        auto [g_socket, g_port] = _connectSocket();
        _f_listen_socket = f_socket;
        _f_port = f_port;
        _g_listen_socket = g_socket;
        _g_port = g_port;

        if (_f_listen_socket == INVALID_SOCKET || _g_listen_socket == INVALID_SOCKET) {
            return SOCKET_CONNECTION_ERROR;
        }

        return SUCCESS;
    }
}
