import pygame
from board_surface import BoardSurface
from board import Board

BG_COLOR = (18, 18, 24)
WHITE = (255, 255, 255)
HIGHLIGHT_COLOR_1 = (0, 215, 135)
HIGHLIGHT_COLOR_2 = (255, 200, 0)

FONT_FILE = 'Cambria.ttf'


class Button(pygame.sprite.Sprite):
    def __init__(self, size, text):
        super(Button, self).__init__()
        self.surf = pygame.Surface(size)

        self.__text = text
        self.__message_font = pygame.font.SysFont(FONT_FILE, 20)

    def draw(self, mouse_over):
        if self.surf.get_rect().collidepoint(pygame.mouse.get_pos()):
            self.surf.fill(HIGHLIGHT_COLOR_1)
        else:
            self.surf.fill(WHITE)

    def is_mouse_over(self):
        return self.surf.get_rect().collidepoint(pygame.mouse.get_pos())


class GamePanel(pygame.sprite.Sprite):
    def __init__(self, size):
        super(GamePanel, self).__init__()
        self.surf = pygame.Surface(size)

        self.__board = None
        self.__template_name = ''
        self.__BOARD_OFFSET = (100, 200)

        self.__message_font = pygame.font.SysFont(FONT_FILE, 40)

    def update(self, mouse_pos):
        self.surf.fill(BG_COLOR)

        if self.__board.check_solved():
            message_text, message_color = 'Solved!', HIGHLIGHT_COLOR_1
        elif self.__board.check_failed():
            message_text, message_color = 'Failed!', HIGHLIGHT_COLOR_2
        else:
            message_text, message_color = self.__template_name.title(), WHITE
        message = self.__message_font.render(message_text, True, message_color)
        message_rect = message.get_rect()
        message_rect.center = (300, 150)

        self.__board.draw()
        self.__mouse_over(mouse_pos)

        self.surf.blit(message, message_rect)
        self.surf.blit(self.__board.surf, self.__BOARD_OFFSET)

    def new_board(self, template_name):
        self.__template_name = template_name
        self.__board = BoardSurface(Board(template_name), 400, [BG_COLOR, WHITE, HIGHLIGHT_COLOR_1, HIGHLIGHT_COLOR_2])

    def mouse_click(self, mouse_pos):
        self.__board.mouse_click(self.__board_mouse_pos(mouse_pos))

    def __board_mouse_pos(self, mouse_position):
        return mouse_position[0] - self.__BOARD_OFFSET[0], mouse_position[1] - self.__BOARD_OFFSET[1]

    def __mouse_over(self, mouse_pos):
        self.__board.mouse_highlight(self.__board_mouse_pos(mouse_pos))


class MenuPanel(pygame.sprite.Sprite):
    pass
