import pygame


class BoardSurface(pygame.sprite.Sprite):
    def __init__(self, board, size, colors):
        super(BoardSurface, self).__init__()
        self.surf = pygame.Surface((size, size))

        self.__board = board
        self.__colors = colors

        self.__cell_radius = 0.9 * size / (2 * max(board.size))
        self.__cell_margin = 0.2 * self.__cell_radius
        self.__x_offset = (size - board.size[1] * 2 * self.__cell_radius - (board.size[1] - 1) * self.__cell_margin) / 2
        self.__y_offset = (size - board.size[0] * 2 * self.__cell_radius - (board.size[0] - 1) * self.__cell_margin) / 2

        self.__selected = None

    def draw(self):
        self.surf.fill(self.__colors[0])

        for pos in self.__board.cells:
            center_x = self.__x_offset + pos[1] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            center_y = self.__y_offset + pos[0] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            pygame.draw.circle(self.surf, self.__colors[1], [center_x, center_y], self.__cell_radius, 3)

            if self.__board.cells[pos]:
                pygame.draw.circle(self.surf, self.__colors[1], [center_x, center_y], 0.6 * self.__cell_radius)

        if self.__selected:
            pos = self.__selected
            center_x = self.__x_offset + pos[1] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            center_y = self.__y_offset + pos[0] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            pygame.draw.circle(self.surf, self.__colors[3], [center_x, center_y], self.__cell_radius, 3)
            pygame.draw.circle(self.surf, self.__colors[3], [center_x, center_y], 0.6 * self.__cell_radius)

    def mouse_highlight(self, mouse_pos):
        for pos in self.__board.cells:
            center_x = self.__x_offset + pos[1] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            center_y = self.__y_offset + pos[0] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            if (mouse_pos[0] - center_x) ** 2 + (mouse_pos[1] - center_y) ** 2 < self.__cell_radius ** 2:
                pygame.draw.circle(self.surf, self.__colors[2], [center_x, center_y], self.__cell_radius, 3)

    def mouse_click(self, mouse_pos):
        clicked = None
        for pos in self.__board.cells:
            center_x = self.__x_offset + pos[1] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            center_y = self.__y_offset + pos[0] * (2 * self.__cell_radius + self.__cell_margin) + self.__cell_radius
            if (mouse_pos[0] - center_x) ** 2 + (mouse_pos[1] - center_y) ** 2 < self.__cell_radius ** 2:
                clicked = pos
                break

        if not clicked:
            return

        if not self.__selected:
            self.__selected = clicked
        elif self.__selected == clicked:
            self.__selected = None
        elif self.__board.move(self.__selected, clicked):
            self.__selected = None
