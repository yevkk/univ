package main

import "fmt"

type Monk struct {
	monastery string
	chi       int
}

func round(monks []Monk, ch chan Monk) {
	if len(monks) == 1 {
		ch <- monks[0]
	} else {
		chLeft := make(chan Monk)
		chRight := make(chan Monk)
		go round(monks[:len(monks)/2], chLeft)
		go round(monks[:len(monks)/2], chRight)

		monkLeft := <-chLeft
		monkRight := <-chRight

		if monkLeft.chi > monkRight.chi {
			ch <- monkLeft
		} else {
			ch <- monkRight
		}
	}
}

func competition(monks []Monk) string {
	ch := make(chan Monk)
	go round(createMonks(), ch)
	return (<-ch).monastery
}

func createMonks() []Monk {
	return []Monk{
		{"Guanyin", 15},
		{"Guanyin", 10},
		{"Guanyin", 12},
		{"Guanyin", 5},
		{"Guanyan", 7},
		{"Guanyan", 14},
		{"Guanyan", 10},
		{"Guanyan", 8},
	}
}

func main() {
	monks := createMonks();
	fmt.Printf(competition(monks))
}
