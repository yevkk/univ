import pygame
from pygame.locals import *
from panels import GamePanel, MenuPanel
from constants import *

pygame.init()
screen = pygame.display.set_mode(WINDOW_SIZE)
pygame.display.set_caption('Peg Solitaire (Lab 1)')
pygame.display.set_icon(pygame.image.load('src/icon.jpg'))

clock = pygame.time.Clock()


on_game = False

game_panel = GamePanel(WINDOW_SIZE)
menu_panel = MenuPanel(WINDOW_SIZE)

start_events = {
    START_MIN: 'minimum',
    START_EASY_PW: 'easy pinwheel',
    START_ENG_STYLE: 'english style',
    START_EUR_STYLE: 'european style'
}

running = True
while running:
    clock.tick(30)
    for event in pygame.event.get():
        if event.type == pygame.MOUSEBUTTONDOWN:
            if on_game:
                game_panel.mouse_click(pygame.mouse.get_pos())
            else:
                menu_panel.mouse_click(pygame.mouse.get_pos())
            pass
        elif event.type == OPEN_MENU:
            on_game = False
        elif event.type in start_events:
            game_panel.new_board(start_events[event.type])
            on_game = True
        elif event.type == QUIT:
            running = False

    if on_game:
        game_panel.update(pygame.mouse.get_pos())
        screen.blit(game_panel.surf, (0, 0))
    else:
        menu_panel.update(pygame.mouse.get_pos())
        screen.blit(menu_panel.surf, (0, 0))

    pygame.display.flip()
