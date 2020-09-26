#ifndef LAB_DB_H
#define LAB_DB_H

#include <stdbool.h>

struct Account {
    unsigned id;
    char nickname[32];
    char fullname[32];
    char country[32];
};

struct Post {
    unsigned id;
    char title[32];
    float pulse;
};

struct IndexItem {
    unsigned id;
    unsigned record_no;
};

struct DataMeta  {
    unsigned size;
    unsigned max_id;
};

struct {
    struct IndexItem *data;
    unsigned int capacity;
    unsigned int size;
    unsigned int max_id;
} m_index;

void load();

void insert_m(const char nickname[32], const char fullname[32], const char country[32]);

#endif //LAB_DB_H
