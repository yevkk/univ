import pygame
from board_surface import BoardSurface
from board import Board
from constants import *


class Button(pygame.sprite.Sprite):
    def __init__(self, size, text):
        super(Button, self).__init__()
        self.surf = pygame.Surface(size)
        self.surf.fill(BG_COLOR)

        self.__text = text
        self.__font = pygame.font.Font(FONT_FILE, 30)

        self.__is_mouse_over = False

    @property
    def is_mouse_over(self):
        return self.__is_mouse_over

    def draw(self):
        color = HIGHLIGHT_COLOR_1 if self.is_mouse_over else WHITE

        pygame.draw.rect(self.surf, color, self.surf.get_rect(),  4, 20)

        text = self.__font.render(self.__text, True, color)
        text_rect = text.get_rect()
        text_rect.center = (self.surf.get_width() / 2, self.surf.get_height() / 2)
        self.surf.blit(text, text_rect)

    def mouse_highlight(self, mouse_pos):
        self.__is_mouse_over = (
                0 <= mouse_pos[0] <= self.surf.get_width() and
                0 <= mouse_pos[1] <= self.surf.get_height()
        )


class GamePanel(pygame.sprite.Sprite):
    def __init__(self, size):
        super(GamePanel, self).__init__()
        self.surf = pygame.Surface(size)

        self.__board = None
        self.__template_name = ''
        self.__BOARD_OFFSET = (100, 150)

        self.__undo_button = Button((120, 80), 'Undo')
        self.__UNDO_BUTTON_OFFSET = (150, 610)

        self.__menu_button = Button((120, 80), 'Menu')
        self.__MENU_BUTTON_OFFSET = (330, 610)

        self.__message_font = pygame.font.Font(FONT_FILE, 40)

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
        message_rect.center = (300, 100)

        self.__board.draw()
        self.__mouse_over(mouse_pos)

        self.__undo_button.draw()
        self.__menu_button.draw()

        self.surf.blit(message, message_rect)
        self.surf.blit(self.__board.surf, self.__BOARD_OFFSET)
        self.surf.blit(self.__undo_button.surf, self.__UNDO_BUTTON_OFFSET)
        self.surf.blit(self.__menu_button.surf, self.__MENU_BUTTON_OFFSET)

    def new_board(self, template_name):
        self.__template_name = template_name
        self.__board = BoardSurface(Board(template_name), 400, [BG_COLOR, WHITE, HIGHLIGHT_COLOR_1, HIGHLIGHT_COLOR_2])

    def mouse_click(self, mouse_pos):
        self.__board.mouse_click(self.__board_mouse_pos(mouse_pos))
        if self.__undo_button.is_mouse_over:
            self.__board.undo()

    def __board_mouse_pos(self, mouse_position):
        return mouse_position[0] - self.__BOARD_OFFSET[0], mouse_position[1] - self.__BOARD_OFFSET[1]

    def __undo_button_pos(self, mouse_position):
        return mouse_position[0] - self.__UNDO_BUTTON_OFFSET[0], mouse_position[1] - self.__UNDO_BUTTON_OFFSET[1]

    def __menu_button_pos(self, mouse_position):
        return mouse_position[0] - self.__MENU_BUTTON_OFFSET[0], mouse_position[1] - self.__MENU_BUTTON_OFFSET[1]

    def __mouse_over(self, mouse_pos):
        self.__board.mouse_highlight(self.__board_mouse_pos(mouse_pos))
        self.__undo_button.mouse_highlight(self.__undo_button_pos(mouse_pos))
        self.__menu_button.mouse_highlight(self.__menu_button_pos(mouse_pos))


class MenuPanel(pygame.sprite.Sprite):
    pass