#ifndef LAB_DB_H
#define LAB_DB_H

struct Account {
    unsigned int id;
    char nickname[32];
    char full_name[32];
    char country[32];
};

struct Post {
    unsigned int id;
    char title[32];
    float pulse;
};

struct IndexItem {
    unsigned int id;
    unsigned int record_number;
};

struct IndexMeta  {
    unsigned int size;
};

struct {
    struct IndexItem *data;
    unsigned int capacity;
    unsigned int size;
} mIndex;

void load();

#endif //LAB_DB_H
