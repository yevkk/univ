import pygame
from board_surface import BoardSurface
from board import Board

BG_COLOR = (18, 18, 24)
WHITE = (255, 255, 255)
HIGHLIGHT_COLOR_1 = (0, 215, 135)
HIGHLIGHT_COLOR_2 = (255, 200, 0)


class GamePanel(pygame.sprite.Sprite):
    def __init__(self, size):
        super(GamePanel, self).__init__()
        self.surf = pygame.Surface(size)
        self.surf.fill(BG_COLOR)

        self.__board = None;
        self.__BOARD_OFFSET = (100, 200)

    def update(self, mouse_pos):
        self.__board.draw()
        self.__mouse_over(mouse_pos)
        self.surf.blit(self.__board.surf, self.__BOARD_OFFSET)

    def new_board(self, template_name):
        self.__board = BoardSurface(Board(template_name), 400, [BG_COLOR, WHITE, HIGHLIGHT_COLOR_1, HIGHLIGHT_COLOR_2])

    def mouse_click(self, mouse_pos):
        self.__board.mouse_click(self.__board_mouse_pos(mouse_pos))

    def __board_mouse_pos(self, mouse_position):
        return mouse_position[0] - self.__BOARD_OFFSET[0], mouse_position[1] - self.__BOARD_OFFSET[1]

    def __mouse_over(self, mouse_pos):
        self.__board.mouse_highlight(self.__board_mouse_pos(mouse_pos))


class MenuPanel(pygame.sprite.Sprite):
    pass
