from board_template import templates


class Board:
    def __init__(self, template_name='minimum', empty=None):
        assert template_name in templates.keys()
        template = templates[template_name]

        self.__size = template.size

        self.__cells = {}
        for cell in template.cells:
            self.__cells[cell] = 1

        self.__cells[empty or template.cells.default_empty] = 0

    @property
    def size(self):
        return self.__size

    def move(self, what, where):
        pass

    def check_failed(self):
        pass

    def check_solved(self):
        pass





