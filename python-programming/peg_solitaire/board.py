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


class Board:
    def __init__(self, template_name='minimum', empty=None):
        assert template_name in templates.keys()
        template = templates[template_name]

        self.__size = template.size

        self.__cells = {}
        for cell in template.cells:
            self.__cells[cell] = 1

        self.__cells[empty or template.default_empty] = 0

        self.__history = []

    @property
    def size(self):
        return self.__size

    @property
    def cells(self):
        return self.__cells

    def move(self, from_position, to_position):
        if (from_position in self.__cells.keys()) and (to_position in self.__cells.keys()):
            if from_position[0] == to_position[0]:
                eq_i, neq_i = 0, 1
            elif from_position[1] == to_position[1]:
                eq_i, neq_i = 1, 0
            else:
                return False

            mid_position = (
                int((from_position[0] + to_position[0]) / 2),
                int((from_position[1] + to_position[1]) / 2)
            )

            if (
                    abs(from_position[neq_i] - to_position[neq_i]) == 2 and
                    self.__cells[from_position] == 1 and
                    self.__cells[to_position] == 0 and
                    self.__cells[mid_position] == 1
            ):
                self.__cells[from_position] = 0
                self.__cells[to_position] = 1
                self.__cells[mid_position] = 0

                self.__history.append((from_position, to_position, mid_position))
                return True

        return False

    def check_failed(self):
        def check_neighbors(pos):
            return True if (
                    self.__cells.get((pos[0] + 1, pos[1]), 0) == 1 or
                    self.__cells.get((pos[0], pos[1] + 1), 0) == 1 or
                    self.__cells.get((pos[0] - 1, pos[1]), 0) == 1 or
                    self.__cells.get((pos[0], pos[1] - 1), 0) == 1
            ) else False

        return (
                not any([check_neighbors(pos) for pos in self.__cells if self.__cells[pos] == 1]) and
                not self.check_solved()
        )

    def check_solved(self):
        return len([pos for pos in self.__cells if self.__cells[pos] == 1]) == 1

    def undo(self):
        if self.__history:
            from_position, to_position, mid_position = self.__history.pop()
            self.__cells[from_position] = 1
            self.__cells[to_position] = 0
            self.__cells[mid_position] = 1

    def __str__(self):
        string = ''
        for i in range(self.size[0]):
            for j in range(self.size[1]):
                string += str(self.__cells.get((i, j), ' '))
            string += '\n'
        return string
