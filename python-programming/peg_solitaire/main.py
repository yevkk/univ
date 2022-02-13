import pygame
from pygame.locals import *
from panels import GamePanel

WINDOW_SIZE = (600, 800)

pygame.init()
screen = pygame.display.set_mode(WINDOW_SIZE)
pygame.display.set_caption('Peg Solitaire (Lab 1)')

clock = pygame.time.Clock()

game_panel = GamePanel(WINDOW_SIZE)
game_panel.new_board('english style')

running = True
while running:
    clock.tick(30)
    for event in pygame.event.get():
        if event.type == pygame.MOUSEBUTTONDOWN:
            game_panel.mouse_click(pygame.mouse.get_pos())

        if event.type == QUIT:
            running = False

    game_panel.update(pygame.mouse.get_pos())
    screen.blit(game_panel.surf, (0, 0))
    pygame.display.flip()
