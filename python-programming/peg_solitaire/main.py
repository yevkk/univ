import pygame
from pygame.locals import *
from board_surface import BoardSurface
from board import Board

BG_COLOR = (18, 18, 24)
WHITE = (255, 255, 255)
HIGHLIGHT_COLOR_1 = (255, 51, 0)

BOARD_OFFSET = (100, 200)

pygame.init()
screen = pygame.display.set_mode((600, 800))
screen.fill(BG_COLOR)

board = BoardSurface(Board('english style'), 400, [BG_COLOR, WHITE, HIGHLIGHT_COLOR_1])

running = True
while running:
    for event in pygame.event.get():
        if event.type == QUIT:
            running = False

    board.draw()

    mouse = pygame.mouse.get_pos()
    board.mouse_highlight((mouse[0] - BOARD_OFFSET[0], mouse[1] - BOARD_OFFSET[1]))
    
    screen.blit(board.surf, BOARD_OFFSET)
    pygame.display.flip()
