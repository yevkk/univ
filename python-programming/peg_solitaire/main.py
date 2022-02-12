import pygame

pygame.init()
screen = pygame.display.set_mode((800, 600))


running = True
while running:
    for event in pygame.event.get():
        if event.type == QUIT:
            running = False

    # screen.blit(board.surf, (40, 40))
    pygame.display.flip()
    