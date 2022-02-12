class BoardTemplate:
    def __init__(self, size, cells, default_empty):
        self.__size = size
        self.__cells = cells

        assert default_empty in cells, 'Default empty cell is not in cells'
        self.__default_empty = default_empty

    @property
    def size(self):
        return self.__size

    @property
    def cells(self):
        return self.__cells

    @property
    def default_empty(self):
        return self.__default_empty



