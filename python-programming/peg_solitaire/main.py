import pygame
from pygame.locals import *
from board_surface import BoardSurface
from board import Board

BG_COLOR = (18, 18, 24)
WHITE = (255, 255, 255)

pygame.init()
screen = pygame.display.set_mode((600, 800))
screen.fill(BG_COLOR)

board = BoardSurface(Board('english style'), 400, [BG_COLOR, WHITE])

running = True
while running:
    for event in pygame.event.get():
        if event.type == QUIT:
            running = False

    board.draw()
    screen.blit(board.surf, (100, 200))
    pygame.display.flip()
