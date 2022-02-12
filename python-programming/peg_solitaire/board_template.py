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


templates = {
    'minimum': BoardTemplate(
        (1, 3),
        [(0, i) for i in range(3)],
        (0, 0)
    ),

    'easy pinwheel': BoardTemplate(
        (4, 4),
        [(0, 2),
         *[(1, i) for i in range(0, 3)],
         *[(2, i) for i in range(1, 4)],
         (3, 1)
         ],
        (1, 0)
    ),

    'english style': BoardTemplate(
        (7, 7),
        [*[(0, i) for i in range(2, 5)],
         *[(1, i) for i in range(2, 5)],
         *[(2, i) for i in range(0, 7)],
         *[(3, i) for i in range(0, 7)],
         *[(4, i) for i in range(0, 7)],
         *[(5, i) for i in range(2, 5)],
         *[(6, i) for i in range(2, 5)]
         ],
        (3, 3)
    ),

    'european style': BoardTemplate(
        (7, 7),
        [*[(0, i) for i in range(2, 5)],
         *[(1, i) for i in range(1, 6)],
         *[(2, i) for i in range(0, 7)],
         *[(3, i) for i in range(0, 7)],
         *[(4, i) for i in range(0, 7)],
         *[(5, i) for i in range(1, 6)],
         *[(6, i) for i in range(2, 5)]
         ],
        (3, 3)
    )
}
