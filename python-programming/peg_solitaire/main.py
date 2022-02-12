import pygame
from pygame.locals import *
from board_surface import BoardSurface
from board import Board

BG_COLOR = (18, 18, 24)
WHITE = (255, 255, 255)
HIGHLIGHT_COLOR_1 = (0, 215, 135)
HIGHLIGHT_COLOR_2 = (255, 200, 0)

BOARD_OFFSET = (100, 200)


def board_mouse_pos(mouse_position):
    return mouse_position[0] - BOARD_OFFSET[0], mouse_position[1] - BOARD_OFFSET[1]


pygame.init()
screen = pygame.display.set_mode((600, 800))
screen.fill(BG_COLOR)

board = BoardSurface(Board('english style'), 400, [BG_COLOR, WHITE, HIGHLIGHT_COLOR_1, HIGHLIGHT_COLOR_2])

running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.MOUSEBUTTONDOWN:
            board.mouse_click(board_mouse_pos(pygame.mouse.get_pos()))

        if event.type == QUIT:
            running = False

    board.draw()
    board.mouse_highlight(board_mouse_pos(pygame.mouse.get_pos()))

    screen.blit(board.surf, BOARD_OFFSET)
    pygame.display.flip()
