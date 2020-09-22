#include "db.h"

#include <stdio.h>
#include <stdlib.h>

const char M_INDEX_FILENAME[] = "index_m.ind";
const char M_DATA_FILENAME[] = "data_m.fl";
const char S_DATA_FILENAME[] = "data_s.fl";
const unsigned int INDEX_INITIAL_SIZE = 32;

void load() {
    FILE *m_data_file = fopen(M_DATA_FILENAME, "ab");
    FILE *s_data_file = fopen(S_DATA_FILENAME, "ab");
    fclose(m_data_file);
    fclose(s_data_file);

    mIndex.size = 0;
    mIndex.capacity = INDEX_INITIAL_SIZE;
    mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);

    FILE *m_index_file = fopen(M_INDEX_FILENAME, "rb");
    if (m_index_file == NULL) {
        fclose(m_index_file);
        m_index_file = fopen(M_INDEX_FILENAME, "ab");
        mIndex.size = 0;
        mIndex.capacity = INDEX_INITIAL_SIZE;
        mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);
    } else {
        struct IndexMeta meta;
        fread(&meta, sizeof(struct IndexMeta), 1, m_index_file);

        mIndex.size = meta.size;
        int tmp;
        for (tmp = 2; tmp < meta.size; tmp *= 2);
        mIndex.capacity = (tmp > INDEX_INITIAL_SIZE) ? tmp : INDEX_INITIAL_SIZE;
        mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);

        for (unsigned int i = 0; i < mIndex.size; i++) {
            fread(mIndex.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}
