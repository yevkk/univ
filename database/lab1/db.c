#include "db.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

const char M_INDEX_FILENAME[] = "index_m.ind";
const char M_DATA_FILENAME[] = "data_m.fl";
const char S_DATA_FILENAME[] = "data_s.fl";
const char M_GC_FILENAME[] = "gc_m.fl";
const char S_GC_FILENAME[] = "gc_s.fl";
const unsigned INDEX_INITIAL_SIZE = 32;

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

    FILE *m_gc_file = fopen(M_GC_FILENAME, "ab");
    fclose(m_gc_file);
    FILE *s_gc_file = fopen(S_GC_FILENAME, "ab");
    fclose(s_gc_file);

    m_index.max_id = m_meta.max_id;
    m_index.size = m_meta.size;
    int tmp;
    for (tmp = 2; tmp < m_meta.size; tmp *= 2);
    m_index.capacity = (tmp > INDEX_INITIAL_SIZE) ? tmp : INDEX_INITIAL_SIZE;
    m_index.data = (struct IndexItem *) malloc(m_index.capacity);

    FILE *m_index_file = fopen(M_INDEX_FILENAME, "rb");
    if (m_index_file == NULL) {
        m_index_file = fopen(M_INDEX_FILENAME, "ab");
    } else {
        for (unsigned int i = 0; i < m_index.size; i++) {
            fread(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}

void insert_m(const char nickname[32], const char fullname[32], const char country[32]) {
    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb+");

    struct DataMeta m_meta;
    fseek(m_data_file, 0, SEEK_SET);
    fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    m_meta.size++;
    m_index.max_id = ++m_meta.max_id;
    fseek(m_data_file, 0, SEEK_SET);
    fwrite(&m_meta, sizeof(struct DataMeta), 1, m_data_file);

    struct Account account = {m_index.max_id, *nickname, *fullname, *country};
    fseek(m_data_file, 0, SEEK_END);
    fwrite(&account, sizeof(struct Account), 1, m_data_file);
    int tmp  = -1;
    fwrite(&tmp, sizeof(int), 1, m_data_file);

    FILE* m_gc_file = fopen(M_GC_FILENAME, "ab");
    bool gc_tmp = true;
    fwrite(&(gc_tmp), sizeof(bool), 1, m_gc_file);
    fclose(m_gc_file);

    struct IndexItem newIndexItem = {account.id, m_meta.size};
    if (m_index.size == m_index.capacity) {
        struct IndexItem *new_data;
        m_index.capacity *= 2;
        new_data = (struct IndexItem *) malloc(m_index.capacity);
        for (unsigned i = 0; i < m_index.size; i++) {
            new_data[i] = m_index.data[i];
        }
        free(m_index.data);
        m_index.data = new_data;
    }
    m_index.data[m_index.size++] = newIndexItem;

    fclose(m_data_file);
}

