from board_template import templates


class Board:
    def __init__(self, template_name='minimum', empty=None):
        assert template_name in templates.keys()
        template = templates[template_name]

        self.__size = template.size

        self.__cells = {}
        for cell in template.cells:
            self.__cells[cell] = 1

        self.__cells[empty or template.default_empty] = 0

    @property
    def size(self):
        return self.__size

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
                return True

        return False

    def check_failed(self):
        pass

    def check_solved(self):
        pass

    def __str__(self):
        string = ''
        for i in range(self.size[0]):
            for j in range(self.size[1]):
                string += str(self.__cells.get((i, j), ' '))
            string += '\n'
        return string
