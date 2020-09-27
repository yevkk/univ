#include "db.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

const char M_INDEX_FILENAME[] = "index_m.ind";
const char M_DATA_FILENAME[] = "data_m.fl";
const char S_DATA_FILENAME[] = "data_s.fl";

void load() {
    struct DataMeta m_meta = {0, 0};
    struct DataMeta s_meta = {0, 0};

    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");
    if (m_data_file == NULL) {
        m_data_file = fopen(M_DATA_FILENAME, "ab");
        fwrite(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    } else {
        fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    }
    fclose(m_data_file);

    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
    if (s_data_file == NULL) {
        s_data_file = fopen(S_DATA_FILENAME, "ab");
        fwrite(&s_meta, sizeof(s_meta), 1, s_data_file);
    }
    fclose(s_data_file);

    m_index.max_id = m_meta.max_id;
    m_index.size = m_meta.size;

    FILE *m_index_file = fopen("index_m.ind", "rb");
    if (m_index_file == NULL) {
        m_index_file = fopen("index_m.ind", "ab");
    } else {
        for (unsigned i = 0; i < m_index.size; i++) {
            fread(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}

void save_index() {
    FILE *m_index_file = fopen("index_m.ind", "wb");

    for (unsigned i = 0; i < m_index.size; i++) {
        fwrite(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
    }

    fclose(m_index_file);
}

int insert_m(const char nickname[32], const char fullname[32], const char country[32]) {
    if (m_index.size == INDEX_MAX_SIZE) {
        return 1;
    }

    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb+");

    struct DataMeta m_meta;
    fseek(m_data_file, 0, SEEK_SET);
    fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    m_meta.size++;
    m_index.max_id = ++m_meta.max_id;
    fseek(m_data_file, 0, SEEK_SET);
    fwrite(&m_meta, sizeof(struct DataMeta), 1, m_data_file);

    struct Account account;
    account.id = m_index.max_id;
    strcpy(account.nickname, nickname);
    strcpy(account.fullname, fullname);
    strcpy(account.country, country);

    fseek(m_data_file, 0, SEEK_END);
    fwrite(&account, sizeof(struct Account), 1, m_data_file);
    bool valid = true;
    fwrite(&valid, sizeof(bool), 1, m_data_file);
    int s_record_no = -1;
    fwrite(&s_record_no, sizeof(int), 1, m_data_file);

    fclose(m_data_file);

    struct IndexItem newIndexItem = {account.id, m_meta.size};
    m_index.data[m_index.size++] = newIndexItem;

    return 0;
}

void ut_m(bool print_deleted) {
    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");

    struct DataMeta m_meta;
    fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);

    for (unsigned i = 0; i < m_meta.size; i++) {
        struct Account account;
        int s_record_no;
        bool valid;
        fread(&account, sizeof(struct Account), 1, m_data_file);
        fread(&valid, sizeof(bool), 1, m_data_file);
        fread(&s_record_no, sizeof(int), 1, m_data_file);

        if (valid || print_deleted) {
            printf("%d)\n", i + 1);
            printf("\t id: %d \n", account.id);
            printf("\t nickname: %s \n", account.nickname);
            printf("\t full_name: %s \n", account.fullname);
            printf("\t country: %s \n", account.country);
            printf("\t [first s record no.: %d] \n", s_record_no);
            if (print_deleted) {
                printf("\t [state: %s] \n", (valid) ? "valid" : "deleted");
            }
        }
    }

    fclose(m_data_file);
}
