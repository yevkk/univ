#ifndef LAB_DB_H
#define LAB_DB_H

#include <stdbool.h>

#define INDEX_MAX_SIZE 32

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

struct MIndex{
    struct IndexItem data[INDEX_MAX_SIZE];
    unsigned int size;
    unsigned int max_id;
} m_index;

void load();

void save_index();

int get_m_record_no(unsigned id);

int get_s_record_no(unsigned id);

int get_s_of_m_record_no(unsigned m_id, unsigned id);

struct Account *get_m(unsigned id);

struct Post *get_s(unsigned id);

struct Post *get_s_of_m(unsigned m_id, unsigned id);

int insert_m(const char nickname[32], const char fullname[32], const char country[32]);

int insert_s(unsigned m_id, const char title[32], float pulse);

void ut_m (bool print_deleted);

void ut_s (bool print_deleted);

#endif //LAB_DB_H
