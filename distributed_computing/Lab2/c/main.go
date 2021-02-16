package main

import (
	"fmt"
	"math"
)

type Monk struct {
	monastery string
	chi       int
}

func (m *Monk) String() string {
	return fmt.Sprintf("%v, %v", m.monastery, m.chi)
}

func round(monks []*Monk, chValue chan *CompetitionTreeNode) {
	if len(monks) == 1 {
		chValue <- &CompetitionTreeNode{nil, nil, monks[0]}
	} else {
		chLeft, chRight := make(chan *CompetitionTreeNode), make(chan *CompetitionTreeNode)
		go round(monks[:len(monks)/2], chLeft)
		go round(monks[len(monks)/2:], chRight)

		monkLeft, monkRight := <-chLeft, <-chRight

		if monkLeft.key.chi > monkRight.key.chi {
			chValue <- &CompetitionTreeNode{monkLeft, monkRight, monkLeft.key}
		} else {
			chValue <- &CompetitionTreeNode{monkLeft, monkRight, monkRight.key}
		}

		close(chValue)
	}
}

func competition(monks []*Monk) *CompetitionTreeNode {
	ch := make(chan *CompetitionTreeNode)
	go round(createMonks(), ch)
	return <-ch
}

func createMonks() []*Monk {
	return []*Monk{
		{"Guanyin", 11},
		{"Guanyin", 10},
		{"Guanyin", 12},
		{"Guanyin", 5},
		{"Guanyan", 7},
		{"Guanyan", 14},
		{"Guanyan", 9},
		{"Guanyan", 8},
		{"Guanyan", 4},
	}
}

func main() {
	monks := createMonks()
	tree := competition(monks)
	tree.printNode(int(math.Ceil(math.Log2(float64(len(monks))))))
}
