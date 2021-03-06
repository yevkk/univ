#include "demofuncs.hpp"
#include "config.hpp"
#include <winsock2.h>
#include <ws2tcpip.h>
#include <iostream>
#include <any>

namespace spos::lab1::utils {
    SOCKET connectIPv4Socket(const std::string &ip, const std::string &port) {
        WSADATA wsa_data;
        int e_result;

        e_result = WSAStartup(MAKEWORD(2, 2), &wsa_data);
        if (e_result) {
            return INVALID_SOCKET;
        }

        addrinfo *ai_ptr, hints;

        ZeroMemory(&hints, sizeof(hints));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_protocol = IPPROTO_TCP;

        e_result = getaddrinfo(ip.c_str(), port.c_str(), &hints, &ai_ptr);
        if (e_result) {
            WSACleanup();
            return INVALID_SOCKET;
        }

        SOCKET connect_socket;
        while (ai_ptr) {
            connect_socket = socket(ai_ptr->ai_family, ai_ptr->ai_socktype, ai_ptr->ai_protocol);
            if (connect_socket == INVALID_SOCKET) {
                freeaddrinfo(ai_ptr);
                WSACleanup();
                return INVALID_SOCKET;
            }

            e_result = connect(connect_socket, ai_ptr->ai_addr, ai_ptr->ai_addrlen);
            if (e_result == SOCKET_ERROR) {
                closesocket(connect_socket);
                connect_socket = INVALID_SOCKET;
                continue;
            }

            break;
        }

        freeaddrinfo(ai_ptr);

        return connect_socket;
    }

    int disconnectSocket(SOCKET socket_arg) {
        if (shutdown(socket_arg, SD_SEND) == SOCKET_ERROR) {
            closesocket(socket_arg);
            WSACleanup();
            return 1;
        }

        closesocket(socket_arg);
        WSACleanup();

        return 0;
    }

    std::any fWrapper(demo::op_group operation, int arg) {
        switch (operation) {
            case demo::AND:
                return demo::f_func<demo::AND>(arg);
            case demo::OR:
                return demo::f_func<demo::OR>(arg);
            case demo::INT:
                return demo::f_func<demo::INT>(arg);
        }
        return nullptr;
    }

    std::any gWrapper(demo::op_group operation, int arg) {
        switch (operation) {
            case demo::AND:
                return demo::g_func<demo::AND>(arg);
            case demo::OR:
                return demo::g_func<demo::OR>(arg);
            case demo::INT:
                return demo::g_func<demo::INT>(arg);
        }
        return nullptr;
    }
} //namespace spos::lab1::utils


/*
argv:
    [1]operation_name
    [2]function_id
    [3]x_argument,
*/

int main(int argc, char *argv[]) {
    using namespace spos::lab1;

    SOCKET connect_socket = utils::connectIPv4Socket(config::ip, strcmp(argv[2], "f") ? config::port_g : config::port_f);
    if (connect_socket == INVALID_SOCKET) {
        WSACleanup();
        return 1;
    }

    demo::op_group operation;

    if (!strcmp(argv[1], "AND")) {
        operation = demo::AND;
    } else if (!strcmp(argv[1], "OR")) {
        operation = demo::OR;
    } else if (!strcmp(argv[1], "MIN")) {
        operation = demo::INT;
    }

    std::any result;
    if (!strcmp(argv[2], "f")) {
        result = utils::fWrapper(operation, std::strtol(argv[3], nullptr, 10));
    } else if (!strcmp(argv[2], "g")) {
        result = utils::gWrapper(operation, std::strtol(argv[3], nullptr, 10));
    }

    const char *send_buf;
    if (operation == demo::AND || operation == demo::OR) {
        send_buf = std::to_string(std::any_cast<bool>(result)).c_str();
    } else if (operation == demo::INT) {
        send_buf = std::to_string(std::any_cast<int>(result)).c_str();
    }

    if (send(connect_socket, send_buf, (int) strlen(send_buf), 0) == SOCKET_ERROR) {
        closesocket(connect_socket);
        WSACleanup();
        return 1;
    }

    return utils::disconnectSocket(connect_socket);
}
